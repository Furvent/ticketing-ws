package fr.eql.ticketing.controller.rest.controller;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import fr.eql.ticketing.controller.rest.dto.create.LoginForm;
import fr.eql.ticketing.controller.rest.dto.create.NewUser;
import fr.eql.ticketing.entity.User;
import fr.eql.ticketing.exception.restController.InvalidNewDataPostException;
import fr.eql.ticketing.service.UserService;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(value = "/public/login", headers = "Accept=application/json")
public class LoginController {
	private UserService userService;

	public LoginController(UserService userService) {
		this.userService = userService;
	}
	
	@PostMapping("/create-user")
	public ResponseEntity<?> createUSer(@RequestBody NewUser newUser) {
		String errorMessageToUser = "";
		try {
			// List of Checks
			if (newUser.getLogin().isEmpty()) {
				errorMessageToUser = "Your login is empty";
				throw new InvalidNewDataPostException("Can't create new user because login is empty");
			}
			if (userService.checkIfUserExistWithThisLogin(newUser.getLogin())) {
				errorMessageToUser = "Login already used";
				throw new InvalidNewDataPostException("Can't create new user because login already exists");
			}
			if (newUser.getPassword().isEmpty()) {
				errorMessageToUser = "Your password is empty";
				throw new InvalidNewDataPostException("Can't create new user because password is empty");
			}
			if (newUser.getPseudo().isEmpty()) {
				errorMessageToUser = "Your pseudo is empty";
				throw new InvalidNewDataPostException("Can't create new user because pseudo is empty");
			}
			// If code reaches here, we can save new user
			User newUserEntity = new User(newUser.getLogin(), newUser.getPassword(), newUser.getPseudo(), LocalDateTime.now());
			userService.save(newUserEntity);
			return new ResponseEntity<>(HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<String>(errorMessageToUser, HttpStatus.BAD_REQUEST);
		}
	}
	
//	@PostMapping("/login")
//	public ResponseEntity<?> loginUser(@RequestBody LoginForm loginForm) {
//		try {
//			if (loginForm.getLogin().isEmpty() || loginForm.getPassword().isEmpty()) {
//				throw new InvalidNewDataPostException("When user try to login, missing login or password");
//			}
//		} catch (Exception e) {
//			// TODO: handle exception
//		}
//	}

}
