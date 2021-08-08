package fr.eql.ticketing.restController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import fr.eql.ticketing.entity.Group;
import fr.eql.ticketing.entity.Membership;
import fr.eql.ticketing.entity.User;
import fr.eql.ticketing.exception.restController.InvalidNewDataPostException;
import fr.eql.ticketing.restController.dto.create.UserIdGroupIdForm;
import fr.eql.ticketing.restController.dto.read.PublicUser;
import fr.eql.ticketing.service.GroupService;
import fr.eql.ticketing.service.MembershipService;
import fr.eql.ticketing.service.UserService;

@CrossOrigin(origins = "*")
@RequestMapping(value = "/private/user-group", headers = "Accept=application/json")
@Deprecated
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

	@GetMapping("")
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
	
	@PostMapping("/add-user")
	public ResponseEntity<?> addUserToGroup(@RequestBody UserIdGroupIdForm userIdGroupIdForm) {
		try {
			// Check if group exist
			Group groupEntity = groupService.getGroupById(userIdGroupIdForm.getGroupId());
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

}
