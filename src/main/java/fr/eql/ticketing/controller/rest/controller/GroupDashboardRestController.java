package fr.eql.ticketing.controller.rest.controller;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import fr.eql.ticketing.controller.rest.dto.create.NewTicket;
import fr.eql.ticketing.controller.rest.dto.read.GroupDashboardData;
import fr.eql.ticketing.controller.rest.dto.read.GroupData;
import fr.eql.ticketing.controller.rest.dto.read.PublicUser;
import fr.eql.ticketing.controller.rest.dto.read.StatusData;
import fr.eql.ticketing.controller.rest.dto.read.TicketData;
import fr.eql.ticketing.controller.rest.dto.update.UpdatedTicket;
import fr.eql.ticketing.entity.Group;
import fr.eql.ticketing.entity.Status;
import fr.eql.ticketing.entity.StatusHistory;
import fr.eql.ticketing.entity.Task;
import fr.eql.ticketing.entity.Ticket;
import fr.eql.ticketing.entity.User;
import fr.eql.ticketing.enums.TicketStatus;
import fr.eql.ticketing.exception.restController.InvalidNewDataPostException;
import fr.eql.ticketing.service.GroupService;
import fr.eql.ticketing.service.MembershipService;
import fr.eql.ticketing.service.StatusHistoryService;
import fr.eql.ticketing.service.StatusService;
import fr.eql.ticketing.service.TaskService;
import fr.eql.ticketing.service.TicketService;
import fr.eql.ticketing.service.UserService;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(value = "/private/group", headers = "Accept=application/json")
public class GroupDashboardRestController {
	UserService userService;
	GroupService groupService;
	MembershipService membershipService;
	TaskService taskService;
	TicketService ticketService;
	StatusHistoryService statusHistoryService;
	StatusService statusService;

	public GroupDashboardRestController(UserService userService, GroupService groupService,
			MembershipService membershipService, TaskService taskService, TicketService ticketService,
			StatusHistoryService statusHistoryService, StatusService statusService) {
		this.userService = userService;
		this.groupService = groupService;
		this.membershipService = membershipService;
		this.taskService = taskService;
		this.ticketService = ticketService;
		this.statusHistoryService = statusHistoryService;
		this.statusService = statusService;
	}

	@PostMapping("")
	public ResponseEntity<?> getGroupDashboardData(@RequestBody long groupId) {
		try {
			// Check if group exist with id sent
			Group group = groupService.getGroupById(groupId);
			if (group == null) {
				throw new InvalidNewDataPostException("No group with id: " + groupId);
			}
			// Gathers all datum to create GroupDashBoardData. First, GroupData and then the
			// list of TicketsData
			// Find group's users and transform them into PublicUser
			List<PublicUser> groupUsers = group.getMemberships().stream().map(membership -> membership.getUser())
					.map(user -> new PublicUser(user.getId(), user.getPseudo())).collect(Collectors.toList());
			GroupData groupData = new GroupData(group, groupUsers);
			// Find group's tickets and transform its into TicketData;
			List<TicketData> groupTickets = group.getTickets().stream().map(ticket -> {
				// Find ticket history
				List<StatusData> history = ticket.getStatusHistory().stream()
						.map(activity -> new StatusData(activity.getStatus().getLabel(), activity.getStatusDate()))
						.collect(Collectors.toList());
				// Find users on task
				List<PublicUser> usersOnTask = ticket.getTasks().stream().map(task -> task.getUser())
						.map(user -> new PublicUser(user.getId(), user.getPseudo())).collect(Collectors.toList());
				return new TicketData(ticket, history, usersOnTask);
			}).collect(Collectors.toList());
			// Now we can instantiate GroupDashboardData
			GroupDashboardData groupDashboardData = new GroupDashboardData(groupData, groupTickets);
			return new ResponseEntity<GroupDashboardData>(groupDashboardData, HttpStatus.OK);

		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}

	@PostMapping("/add-ticket")
	public ResponseEntity<?> addTicket(@RequestBody NewTicket newTicket) {
		try {
			// Checks
			if (newTicket.getTitle().isEmpty()) {
				throw new InvalidNewDataPostException("New ticket's title cannot be empty");
			}
			if (newTicket.getDescription().isEmpty()) {
				throw new InvalidNewDataPostException("New ticket's description cannot be empty");
			}
			if (!groupService.checkIfGroupExistWithThisId(newTicket.getGroupId())) {
				throw new InvalidNewDataPostException("Invalid group id");
			}
			Group ticketGroup = groupService.getGroupById(newTicket.getGroupId());
			// Add ticket and if user is on task, add relation
			Ticket ticketEntity = new Ticket(newTicket.getDescription(), newTicket.getTitle(), ticketGroup);
			// We save ticket to get id
			ticketService.save(ticketEntity);
			// If user on task
			if (newTicket.getUsersOnTask().size() > 0) {
				// TODO: add verification, users belong to that group ?
				try {
					this.addTaskAndHistoryOnTicketWithPublicUsers(ticketEntity, newTicket.getUsersOnTask());
				} catch (Exception e) {
					// Remove the ticket because bad data sent
					ticketService.delete(ticketEntity);
					throw e;
				}
			}
			return new ResponseEntity<Long>(ticketEntity.getId(), HttpStatus.OK);

		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}

	@PostMapping("/update-ticket")
	public ResponseEntity<?> updateTicket(@RequestBody UpdatedTicket updatedTicket) {
		try {
			// Check if ticket exist
			Ticket ticketEntity = ticketService.getTicketById(updatedTicket.getTicketId());
			if (ticketEntity == null) {
				throw new InvalidNewDataPostException(
						"Ticket with id -" + updatedTicket.getTicketId() + "- doesn't exist.");
			}
			if (!updatedTicket.getNewDescription().isEmpty()) {
				ticketEntity.setDetails(updatedTicket.getNewDescription());
			}
			if (!updatedTicket.getNewTitle().isEmpty()) {
				ticketEntity.setTitle(updatedTicket.getNewTitle());
			}
			if (updatedTicket.getUsersOnTask().size() > 0) {
				// TODO: add verification, users belong to that group ?
				try {
					this.addTaskAndHistoryOnTicketWithPublicUsers(ticketEntity, updatedTicket.getUsersOnTask());
				} catch (Exception e) {
					// Remove the ticket because bad data sent
					ticketService.delete(ticketEntity);
					throw e;
				}
			}
			if (!updatedTicket.getNewStatus().isEmpty()) {
				try {
					this.addNewStatusOnTicket(ticketEntity, updatedTicket.getNewStatus());
				} catch (Exception e) {
					// TODO: handle exception
				}
			}
			return new ResponseEntity<>(HttpStatus.OK);

		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}

	private void addNewStatusOnTicket(Ticket ticket, String statusLabel) {
		// TODO: Check if last status in history is the same of the new label status
		if (this.isStatusLabelValidWhenUpdatingTicket(statusLabel)) {
			Status status = statusService.getStatusByLabel(statusLabel);
			StatusHistory statusHistory = new StatusHistory(status, ticket, LocalDateTime.now());
			statusHistoryService.save(statusHistory);
		} else {
			throw new InvalidNewDataPostException("Cannot add status -" + statusLabel + "- on a ticket updated");
		}
	}

	/**
	 * Status label cannot be TicketStatus.OPENED or ALLOCATED or a value not in the
	 * enum
	 * 
	 * @return
	 */
	private boolean isStatusLabelValidWhenUpdatingTicket(String label) {
		return TicketStatus.isValid(label) && !label.equals(TicketStatus.OPENED)
				&& !label.equals(TicketStatus.ALLOCATED);
	}

	private void addTaskAndHistoryOnTicketWithPublicUsers(Ticket ticket, List<PublicUser> publicUsers)
			throws InvalidNewDataPostException {
		// Get all users entities from the list of public users
		List<User> usersEntitiesOnTask = this.getUsersFromPublicUsers(publicUsers);
		// If missing user entity, throws exception
		if (usersEntitiesOnTask.size() != publicUsers.size()) {
			throw new InvalidNewDataPostException("Cannot found users entities to add them on task");
		}
		// For each user entity, add a task and add a new status on the ticket
		List<Task> tasks = new ArrayList<Task>();
		List<StatusHistory> history = new ArrayList<StatusHistory>();
		usersEntitiesOnTask.forEach(userOnTask -> {
			LocalDateTime now = LocalDateTime.now();
			// Create task
			Task task = new Task(userOnTask, ticket, now);
			tasks.add(task);
			// Create statusHistory
			Status status = statusService.getStatusByLabel(TicketStatus.ALLOCATED);
			StatusHistory statusHistory = new StatusHistory(status, ticket, now);
			history.add(statusHistory);
		});
		// Save new entities
		taskService.saveAll(tasks);
		statusHistoryService.saveAll(history);
	}

	private List<User> getUsersFromPublicUsers(List<PublicUser> publicUsers) {
		return userService.getMultipleUsersWithIds(
				publicUsers.stream().map(publicUser -> publicUser.getId()).collect(Collectors.toList()));
	}

}
