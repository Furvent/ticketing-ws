package fr.eql.ticketing.controller.rest.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import fr.eql.ticketing.controller.rest.dto.read.GroupDashboardData;
import fr.eql.ticketing.controller.rest.dto.read.GroupData;
import fr.eql.ticketing.controller.rest.dto.read.PublicUser;
import fr.eql.ticketing.controller.rest.dto.read.StatusData;
import fr.eql.ticketing.controller.rest.dto.read.TicketData;
import fr.eql.ticketing.entity.Group;
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

}
