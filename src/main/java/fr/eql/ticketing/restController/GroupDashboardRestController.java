package fr.eql.ticketing.restController;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import fr.eql.ticketing.entity.Group;
import fr.eql.ticketing.entity.Membership;
import fr.eql.ticketing.entity.Status;
import fr.eql.ticketing.entity.StatusHistory;
import fr.eql.ticketing.entity.Task;
import fr.eql.ticketing.entity.Ticket;
import fr.eql.ticketing.entity.User;
import fr.eql.ticketing.enums.EntityType;
import fr.eql.ticketing.enums.TicketStatus;
import fr.eql.ticketing.exception.restController.InvalidNewDataPostException;
import fr.eql.ticketing.restController.dto.create.NewTicket;
import fr.eql.ticketing.restController.dto.create.UserIdGroupIdForm;
import fr.eql.ticketing.restController.dto.read.CommentToDisplay;
import fr.eql.ticketing.restController.dto.read.CommentsToGet;
import fr.eql.ticketing.restController.dto.read.GroupDashboardData;
import fr.eql.ticketing.restController.dto.read.GroupData;
import fr.eql.ticketing.restController.dto.read.PublicUser;
import fr.eql.ticketing.restController.dto.read.StatusData;
import fr.eql.ticketing.restController.dto.read.TicketData;
import fr.eql.ticketing.restController.dto.update.UpdatedTicket;
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
			Group group = groupService.getGroupWithId(groupId);
			if (group == null) {
				throw new InvalidNewDataPostException("No group with id: " + groupId);
			}
			// Gathers all datum to create GroupDashBoardData. First, GroupData and then the
			// list of TicketsData
			// Find group's users and transform them into PublicUser
			List<PublicUser> groupPublicUsers = group.getMemberships().stream().map(membership -> membership.getUser())
					.map(user -> new PublicUser(user.getId(), user.getPseudo())).collect(Collectors.toList());
			GroupData groupData = new GroupData(group, groupPublicUsers);
			// Find group's tickets and transform its into TicketData;
			List<TicketData> groupTickets = group.getTickets().stream().map(ticket -> {
				return this.createTicketDataFromTicketEntity(ticket);
			}).collect(Collectors.toList());

			// Find Group's comments
			CommentsToGet commentsToGetGroup = new CommentsToGet(group.getId(), EntityType.GROUP.name());
			List<CommentToDisplay> commentsToDisplayGroup = setUpAndGetCommentsToDisplay(commentsToGetGroup);

			// Now we can instantiate GroupDashboardData
			GroupDashboardData groupDashboardData = new GroupDashboardData(groupData, groupTickets,
					commentsToDisplayGroup);
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
			if (!groupService.checkIfGroupExistsWithThisId(newTicket.getGroupId())) {
				throw new InvalidNewDataPostException("Invalid group id");
			}
			Group ticketGroup = groupService.getGroupWithId(newTicket.getGroupId());
			// Add ticket and if user is on task, add relation
			Ticket ticketEntity = new Ticket(UUID.randomUUID().toString(), newTicket.getDescription(), newTicket.getTitle(), ticketGroup);
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
			// Add Status history for creation
			Status statusOpened = statusService.getStatusByLabel(TicketStatus.OPENED);
			StatusHistory statusHistoryEntity = new StatusHistory(statusOpened, ticketEntity, LocalDateTime.now());
			statusHistoryService.save(statusHistoryEntity);
			// TODO: handle problem, in the TicketData send back, there is no history or users on task
			Ticket ticketEntity2 = ticketService.getTicketWithId(ticketEntity.getId());
			TicketData ticketData = this.createTicketDataFromTicketEntity(ticketEntity2);
			return new ResponseEntity<TicketData>(ticketData, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}

	@PostMapping("/update-ticket")
	public ResponseEntity<?> updateTicket(@RequestBody UpdatedTicket updatedTicket) {
		try {
			// TODO: Maybe work with hibernate transaction, because Ticket is modify but if
			// exception is throw when adding status, modifications stayed
			// Check if ticket exist
			Ticket ticketEntity = ticketService.getTicketWithId(updatedTicket.getTicketId());

			if (ticketEntity == null) {
				throw new InvalidNewDataPostException(
						"Ticket with id -" + updatedTicket.getTicketId() + "- doesn't exist.");
			}
			if (updatedTicket.getUsersOnTask().size() > 0) {
				// TODO: add verification, users belong to that group ?
				this.addTaskAndHistoryOnTicketWithPublicUsers(ticketEntity, updatedTicket.getUsersOnTask());
			}
			if (!updatedTicket.getNewDescription().isEmpty()) {
				ticketEntity.setDescription(updatedTicket.getNewDescription());
			}
			if (!updatedTicket.getNewTitle().isEmpty()) {
				ticketEntity.setTitle(updatedTicket.getNewTitle());
			}
			if (!updatedTicket.getNewStatus().isEmpty()) {
				try {
					this.addNewStatusOnTicket(ticketEntity, updatedTicket.getNewStatus());
				} catch (Exception e) {
					throw e;
				}
			}
			ticketService.save(ticketEntity);
			return new ResponseEntity<>(HttpStatus.OK);

		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}

	@PostMapping("/add-user")
	public ResponseEntity<?> addUserToGroup(@RequestBody UserIdGroupIdForm userIdGroupIdForm) {
		try {
			// Check if group exist
			Group groupEntity = groupService.getGroupWithId(userIdGroupIdForm.getGroupId());
			if (groupEntity == null) {
				throw new InvalidNewDataPostException(
						"Group with id -" + userIdGroupIdForm.getGroupId() + "- doesn't exist.");
			}
			// Check if user exist
			User userEntity = userService.getUserWithId(userIdGroupIdForm.getUserId());
			if (userEntity == null) {
				throw new InvalidNewDataPostException(
						"User with id -" + userIdGroupIdForm.getUserId() + "- doesn't exist.");
			}
			// TODO: Check if user was a member but withdrawn from group
			// Create membership
			Membership newMembership = new Membership(userEntity, groupEntity, LocalDateTime.now());
			membershipService.save(newMembership);
			return new ResponseEntity<>(HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}

//	@PostMapping("/remove-user")
//	public ResponseEntity<?> removeUserFromGroup(@RequestBody UserIdGroupIdForm userIdGroupIdForm) {
//		try {
//			// Check if group exists
//			Group groupEntity = groupService.getGroupById(userIdGroupIdForm.getGroupId());
//			if (groupEntity == null) {
//				throw new InvalidNewDataPostException(
//						"Group with id -" + userIdGroupIdForm.getGroupId() + "- doesn't exist.");
//			}
//			// Check if user exists
//			User userEntity = userService.getUserWithId(userIdGroupIdForm.getUserId());
//			if (userEntity == null) {
//				throw new InvalidNewDataPostException(
//						"User with id -" + userIdGroupIdForm.getUserId() + "- doesn't exist.");
//			}
//			// Check if membership exists
//			Membership membershipEntity = membershipService.getMembershipWithUserAndGroup(userEntity, groupEntity);
//			if (membershipEntity == null) {
//				throw new InvalidNewDataPostException(
//						"Cannot remove user with id -" + userIdGroupIdForm.getUserId() + "- from group with id -"
//								+ userIdGroupIdForm.getGroupId() + "- because there is no membership between them.");
//			}
//			if (membershipEntity.getWithdrawalDate() != null) {
//				throw new InvalidNewDataPostException("Cannot remove user with id -" + userIdGroupIdForm.getUserId()
//						+ "- from group with id -" + userIdGroupIdForm.getGroupId()
//						+ "- because user was already removed at date " + membershipEntity.getWithdrawalDate() + ".");
//			}
//			membershipEntity.setWithdrawalDate(LocalDateTime.now());
//			membershipService.save(membershipEntity);
//			return new ResponseEntity<>(HttpStatus.OK);
//
//		} catch (Exception e) {
//			e.printStackTrace();
//			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
//		}
//	}

	private void addNewStatusOnTicket(Ticket ticket, String statusLabel) {
		// TODO: Check if last status in history is the same of the new label status
		if (this.isStatusLabelValidWhenUpdatingTicket(statusLabel)) {
			Status status = statusService.getStatusByLabel(statusLabel);
			StatusHistory statusHistory = new StatusHistory(status, ticket, LocalDateTime.now().plusSeconds(2));
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
		// Get all users entities from the list of public users collected in request
		List<User> usersOnTaskCollected = this.getUsersFromPublicUsers(publicUsers);
		// If missing user entity, throws exception
		if (usersOnTaskCollected.size() != publicUsers.size()) {
			throw new InvalidNewDataPostException("Cannot found users entities to add them on task");
		}
		// Users already on task
		List<User> usersAlreadyOnTask = ticket.getTasks().stream().map(task -> task.getUser()).collect(Collectors.toList());
		// Create a list with only new users to add on task
		List<User> usersToAddOnTask = usersOnTaskCollected.stream().filter(userCollected -> {
			return !usersAlreadyOnTask.contains(userCollected);
		}).collect(Collectors.toList());
		// For each user to add, add a task and add a new status on the ticket
		List<Task> tasks = new ArrayList<Task>();
		List<StatusHistory> history = new ArrayList<StatusHistory>();
		usersToAddOnTask.forEach(userOnTask -> {
			LocalDateTime now = LocalDateTime.now().plusSeconds(1);
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

	private TicketData createTicketDataFromTicketEntity(Ticket ticket) {
		// Find ticket history
		List<StatusData> history = ticket.getStatusHistory().stream()
				.map(activity -> new StatusData(activity.getStatus().getLabel(), activity.getStatusDate()))
				.collect(Collectors.toList());
		// Find users on task
		List<PublicUser> usersOnTask = ticket.getTasks().stream().map(task -> task.getUser())
				.map(user -> new PublicUser(user.getId(), user.getPseudo())).collect(Collectors.toList());
		// Find comments on ticket
		CommentsToGet commentsToGetTicket = new CommentsToGet(ticket.getId(), EntityType.TICKET.name());
		List<CommentToDisplay> commentsToDisplayTicket = setUpAndGetCommentsToDisplay(commentsToGetTicket);
		return new TicketData(ticket, history, usersOnTask, commentsToDisplayTicket);
	}

	private List<CommentToDisplay> setUpAndGetCommentsToDisplay(CommentsToGet commentsToGet) {
		String urlWSComment = "http://localhost:8083/api/public/comments";
		RestTemplate restTemplate = new RestTemplate();
		HttpEntity<CommentsToGet> request = new HttpEntity<>(commentsToGet);
		CommentToDisplay[] preResult = restTemplate.postForObject(urlWSComment, request, CommentToDisplay[].class);
		List<CommentToDisplay> result = new ArrayList<CommentToDisplay>(Arrays.asList(preResult));
		return result;
	}

}
