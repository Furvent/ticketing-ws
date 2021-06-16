package fr.eql.ticketing.controller.rest.controller;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import fr.eql.ticketing.controller.rest.dto.create.NewGroup;
import fr.eql.ticketing.controller.rest.dto.read.GeneralDashboardData;
import fr.eql.ticketing.controller.rest.dto.read.GroupData;
import fr.eql.ticketing.controller.rest.dto.read.PublicUser;
import fr.eql.ticketing.controller.rest.dto.update.UpdatedUser;
import fr.eql.ticketing.entity.Group;
import fr.eql.ticketing.entity.Membership;
import fr.eql.ticketing.entity.User;
import fr.eql.ticketing.exception.restController.EntityNotFoundException;
import fr.eql.ticketing.exception.restController.InvalidNewDataPostException;
import fr.eql.ticketing.service.GroupService;
import fr.eql.ticketing.service.MembershipService;
import fr.eql.ticketing.service.UserService;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(value = "/private/dashboard", headers = "Accept=application/json")
public class GeneralDashboardRestController {
	private GroupService groupService;
	private UserService userService;
	private MembershipService membershipService;

	public GeneralDashboardRestController(GroupService groupService, UserService userService,
			MembershipService membershipService) {
		this.groupService = groupService;
		this.userService = userService;
		this.membershipService = membershipService;
	}

	@PostMapping("")
	public ResponseEntity<?> getGeneralDashboardData(@RequestBody long userId) {
		try {
			// Check
			if (!userService.checkIfUserExistWithThisId(userId)) {
				throw new EntityNotFoundException("Cannot find user with id: " + userId);
			}
			User userEntity = userService.getUserWithId(userId);
			// Get user group
			List<Membership> memberships = membershipService.getMembershipsWithUser(userEntity);
			// Get all userGroup entities
			List<Group> userGroups = memberships.stream().map(membership -> membership.getGroup())
					.collect(Collectors.toList());
			// Prepare groups data to send back
			List<GroupData> groupsData = new ArrayList<GroupData>();
			userGroups.forEach((group) -> {
				// List of users to this group, get from memberships and transform into
				// PublicUser (to get id and pseudo only)
				List<PublicUser> usersGroup = group.getMemberships().stream().map(membership -> membership.getUser())
						.map(user -> new PublicUser(user.getId(), user.getPseudo())).collect(Collectors.toList());
				// Transform this users list into a list of PublicUser
				groupsData.add(new GroupData(group, usersGroup));
			});
			GeneralDashboardData dataToSend = new GeneralDashboardData(userEntity, groupsData);
			return new ResponseEntity<GeneralDashboardData>(dataToSend, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}

	@PostMapping("/addGroup")
	public ResponseEntity<?> postNewGroup(@RequestBody NewGroup newGroup) {
		try {
			if (newGroup.getName() == "") {
				throw new InvalidNewDataPostException("Group's name cannot be empty");
			}
			if (newGroup.getCreatorId() < 1) {
				throw new InvalidNewDataPostException("Group creator's id is invalid");
			}
			User creatorUser = userService.getUserWithId(newGroup.getCreatorId());
			Group groupEntity = new Group(newGroup.getName(), creatorUser, LocalDateTime.now());
			groupService.save(groupEntity);
			return new ResponseEntity<Long>(groupEntity.getId(), HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}

	@PutMapping("/updateUser")
	public ResponseEntity<?> updateUserData(@RequestBody UpdatedUser userNewData) {
		try {
			// TODO: rework if using spring security. Actually no check on login validity
			User userEntity = userService.getUserWithUsername(userNewData.getLogin());
			if (!userNewData.getNewPassword().isEmpty()) {
				if (!userNewData.getOldPassword().equals(userEntity.getPassword())) {
					throw new InvalidNewDataPostException("Old password invalid");
				} else {
					userEntity.setPassword(userNewData.getNewPassword());
				}
			}
			if (!userNewData.getNewPseudo().isEmpty()) {
				userEntity.setPseudo(userNewData.getNewPseudo());
			}
			userService.save(userEntity);
			return new ResponseEntity<>(HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
		}
	}

}
