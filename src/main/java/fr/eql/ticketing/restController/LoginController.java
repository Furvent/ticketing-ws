package fr.eql.ticketing.restController;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import fr.eql.ticketing.entity.User;
import fr.eql.ticketing.exception.restController.InvalidNewDataPostException;
import fr.eql.ticketing.restController.dto.create.LoginForm;
import fr.eql.ticketing.restController.dto.create.NewUser;
import fr.eql.ticketing.restController.dto.read.PrivateUser;
import fr.eql.ticketing.service.UserService;

@CrossOrigin(origins = "*")
@RequestMapping(value = "/public/login", headers = "Accept=application/json")
@Deprecated
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
			if (newUser.getUsername().isEmpty()) {
				errorMessageToUser = "Username is empty";
				throw new InvalidNewDataPostException("Can't create new user because login is empty");
			}
			if (userService.checkIfUserExistWithThisUsername(newUser.getUsername())) {
				errorMessageToUser = "Username already used";
				throw new InvalidNewDataPostException("Can't create new user because login already exists");
			}
			if (newUser.getPassword().isEmpty()) {
				errorMessageToUser = "Password is empty";
				throw new InvalidNewDataPostException("Can't create new user because password is empty");
			}
			if (newUser.getPseudo().isEmpty()) {
				errorMessageToUser = "Pseudo is empty";
				throw new InvalidNewDataPostException("Can't create new user because pseudo is empty");
			}
			// If code reaches here, we can save new user
			User newUserEntity = new User(newUser.getUsername(), newUser.getPassword(), newUser.getPseudo(), LocalDateTime.now());
			userService.save(newUserEntity);
			PrivateUser privateUser = new PrivateUser(newUserEntity);
			return new ResponseEntity<PrivateUser>(privateUser, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<String>(errorMessageToUser, HttpStatus.BAD_REQUEST);
		}
	}
	
	@PostMapping("")
	public ResponseEntity<?> loginUser(@RequestBody LoginForm loginForm) {
		try {
			if (loginForm.getUsername().isEmpty() || loginForm.getPassword().isEmpty()) {
				throw new InvalidNewDataPostException("When user try to login, missing login or password");
			}
			User userEntity = userService.getUserWithUsernameAndPassword(loginForm.getUsername(), loginForm.getPassword());
			if (userEntity == null) {
				throw new InvalidNewDataPostException("Login and / or password are wrong");
			}
			PrivateUser privateUser = new PrivateUser(userEntity);
			return new ResponseEntity<PrivateUser>(privateUser, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
		}
	}

}
