package fr.eql.ticketing.restController;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import fr.eql.ticketing.entity.Group;
import fr.eql.ticketing.enums.EntityType;
import fr.eql.ticketing.exception.restController.InvalidNewDataPostException;
import fr.eql.ticketing.restController.dto.read.CommentToDisplay;
import fr.eql.ticketing.restController.dto.read.CommentsToGet;
import fr.eql.ticketing.restController.dto.read.GroupDashboardData;
import fr.eql.ticketing.restController.dto.read.GroupData;
import fr.eql.ticketing.restController.dto.read.PublicUser;
import fr.eql.ticketing.restController.dto.read.TicketData;
import fr.eql.ticketing.service.GroupService;
import fr.eql.ticketing.service.MembershipService;
import fr.eql.ticketing.service.UserService;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(value = "/private/user-group", headers = "Accept=application/json")
public class UserGroupRestController {
	UserService userService;
	GroupService groupService;
	MembershipService membershipService;

	public UserGroupRestController(UserService userService, GroupService groupService,
			MembershipService membershipService) {
		this.userService = userService;
		this.groupService = groupService;
		this.membershipService = membershipService;
	}

	@PostMapping("")
	public ResponseEntity<?> getAllUsersFromApp() {
		try {
			List<PublicUser> allAppUsers = userService.getAllUsers().stream()
					.map(userEntity -> new PublicUser(userEntity.getId(), userEntity.getPseudo()))
					.collect(Collectors.toList());
			return new ResponseEntity<List<PublicUser>>(allAppUsers, HttpStatus.OK);

		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}
