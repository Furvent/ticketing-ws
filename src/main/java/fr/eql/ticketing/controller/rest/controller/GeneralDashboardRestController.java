package fr.eql.ticketing.controller.rest.controller;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import fr.eql.ticketing.controller.rest.dto.create.NewGroup;
import fr.eql.ticketing.controller.rest.dto.update.UpdatedUser;
import fr.eql.ticketing.entity.Group;
import fr.eql.ticketing.entity.User;
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
			User userEntity = userService.getUserWithLogin(userNewData.getLogin());
			if (!userNewData.getNewPassword().isEmpty()) {
				System.err.println("userEntityPwd: " + userEntity.getPassword());
				System.err.println("userNewDataPwd: " + userNewData.getOldPassword());
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
