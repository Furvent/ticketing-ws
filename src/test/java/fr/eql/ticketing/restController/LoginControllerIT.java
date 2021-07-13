package fr.eql.ticketing.restController;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import fr.eql.ticketing.TicketingApplication;
import fr.eql.ticketing.entity.User;
import fr.eql.ticketing.repository.UserRepository;
import fr.eql.ticketing.restController.dto.create.LoginForm;
import fr.eql.ticketing.restController.dto.create.NewUser;
import fr.eql.ticketing.restController.dto.read.PrivateUser;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = TicketingApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase
public class LoginControllerIT {
	@Autowired
	private TestRestTemplate restTemplate;

	@Autowired
	private UserRepository userRepository;

	@BeforeEach
	void cleanDbBeforeEachTest() {
		userRepository.deleteAll();
	}

	@Test
	void itShouldLogin() {
		User mimi = new User("mimi", "123", "mimi92", LocalDateTime.now());
		userRepository.save(mimi);

		// Call the service
		LoginForm loginForm = new LoginForm();
		loginForm.setPassword(mimi.getPassword());
		loginForm.setUsername(mimi.getUsername());
		HttpEntity<LoginForm> request = new HttpEntity<>(loginForm);
		String url = "/public/login";
		ResponseEntity<PrivateUser> response = restTemplate.postForEntity(url, request, PrivateUser.class);
		assertTrue(response.getBody().getPseudo().equals("mimi92"));
	}

	@Test
	void itShouldNotLoginBecauseBadPassword() {
		User mimi = new User("mimi", "123", "mimi92", LocalDateTime.now());
		userRepository.save(mimi);

		// Call the service
		LoginForm loginForm = new LoginForm();
		loginForm.setPassword("badPassword");
		loginForm.setUsername(mimi.getUsername());
		HttpEntity<LoginForm> request = new HttpEntity<>(loginForm);
		String url = "/public/login";
		ResponseEntity<PrivateUser> response = restTemplate.postForEntity(url, request, PrivateUser.class);
		assertNull(response.getBody());
		assertTrue(response.getStatusCode() == HttpStatus.UNAUTHORIZED);
	}

	@Test
	void itShouldNotLoginBecauseBadUsername() {
		User mimi = new User("mimi", "123", "mimi92", LocalDateTime.now());
		userRepository.save(mimi);

		// Call the service
		LoginForm loginForm = new LoginForm();
		loginForm.setPassword(mimi.getPassword());
		loginForm.setUsername("badUsername");
		HttpEntity<LoginForm> request = new HttpEntity<>(loginForm);
		String url = "/public/login";
		ResponseEntity<PrivateUser> response = restTemplate.postForEntity(url, request, PrivateUser.class);
		assertNull(response.getBody());
		assertTrue(response.getStatusCode() == HttpStatus.UNAUTHORIZED);
	}

	@Test
	void itShouldNotLoginBecauseUsernameIsMissing() {
		User mimi = new User("mimi", "123", "mimi92", LocalDateTime.now());
		userRepository.save(mimi);

		// Call the service
		LoginForm loginForm = new LoginForm();
		loginForm.setPassword(mimi.getPassword());
		loginForm.setUsername("");
		HttpEntity<LoginForm> request = new HttpEntity<>(loginForm);
		String url = "/public/login";
		ResponseEntity<PrivateUser> response = restTemplate.postForEntity(url, request, PrivateUser.class);
		assertNull(response.getBody());
		assertTrue(response.getStatusCode() == HttpStatus.UNAUTHORIZED);
	}

	@Test
	void itShouldNotLoginBecausePasswordIsMissing() {
		User mimi = new User("mimi", "123", "mimi92", LocalDateTime.now());
		userRepository.save(mimi);

		// Call the service
		LoginForm loginForm = new LoginForm();
		loginForm.setPassword("");
		loginForm.setUsername(mimi.getPassword());
		HttpEntity<LoginForm> request = new HttpEntity<>(loginForm);
		String url = "/public/login";
		ResponseEntity<PrivateUser> response = restTemplate.postForEntity(url, request, PrivateUser.class);
		assertNull(response.getBody());
		assertTrue(response.getStatusCode() == HttpStatus.UNAUTHORIZED);
	}

	@Test
	void itShouldCreateUser() {
		final String mockedUsername = "username";
		final String mockedPassword = "password";
		final String mockedPseudo = "pseudo";

		NewUser newUser = new NewUser();
		newUser.setUsername(mockedUsername);
		newUser.setPassword(mockedPassword);
		newUser.setPseudo(mockedPseudo);

		// Call the service
		HttpEntity<NewUser> request = new HttpEntity<>(newUser);
		String url = "/public/login/create-user";
		ResponseEntity<PrivateUser> response = restTemplate.postForEntity(url, request, PrivateUser.class);
		assertTrue(response.getBody().getPseudo().equals(mockedPseudo));
		assertNotNull(userRepository.findByUsername(mockedUsername).get());
		assertTrue(response.getStatusCode() == HttpStatus.OK);
	}

	@Test
	void itShouldNotCreateUserBecauseUsernameAlreadyExist() {
		final String mockedUsername = "username";
		final String mockedPassword = "password";
		final String mockedPseudo = "pseudo";

		final String mockedPassword2 = "password2";
		final String mockedPseudo2 = "pseudo2";

		User userAlreadyStored = new User(mockedUsername, mockedPassword, mockedPseudo, LocalDateTime.now());
		userRepository.save(userAlreadyStored);

		NewUser newUser = new NewUser();
		newUser.setUsername(mockedUsername);
		newUser.setPassword(mockedPassword2);
		newUser.setPseudo(mockedPseudo2);

		// Call the service
		HttpEntity<NewUser> request = new HttpEntity<>(newUser);
		String url = "/public/login/create-user";
		ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);
		assertTrue(response.getBody().equals("Username already used"));
		assertTrue(response.getStatusCode() == HttpStatus.BAD_REQUEST);
	}

	@Test
	void itShouldNotCreateUserBecauseUsernameIsMissing() {
		final String mockedUsername = "";
		final String mockedPassword = "password";
		final String mockedPseudo = "pseudo";

		NewUser newUser = new NewUser();
		newUser.setUsername(mockedUsername);
		newUser.setPassword(mockedPassword);
		newUser.setPseudo(mockedPseudo);

		// Call the service
		HttpEntity<NewUser> request = new HttpEntity<>(newUser);
		String url = "/public/login/create-user";
		ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);
		assertTrue(response.getBody().equals("Username is empty"));
		assertTrue(response.getStatusCode() == HttpStatus.BAD_REQUEST);
	}

	@Test
	void itShouldNotCreateUserBecausePasswordIsMissing() {
		final String mockedUsername = "username";
		final String mockedPassword = "";
		final String mockedPseudo = "pseudo";

		NewUser newUser = new NewUser();
		newUser.setUsername(mockedUsername);
		newUser.setPassword(mockedPassword);
		newUser.setPseudo(mockedPseudo);

		// Call the service
		HttpEntity<NewUser> request = new HttpEntity<>(newUser);
		String url = "/public/login/create-user";
		ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);
		assertTrue(response.getBody().equals("Password is empty"));
		assertTrue(response.getStatusCode() == HttpStatus.BAD_REQUEST);
	}

	@Test
	void itShouldNotCreateUserBecausePseudoIsMissing() {
		final String mockedUsername = "username";
		final String mockedPassword = "password";
		final String mockedPseudo = "";

		NewUser newUser = new NewUser();
		newUser.setUsername(mockedUsername);
		newUser.setPassword(mockedPassword);
		newUser.setPseudo(mockedPseudo);

		// Call the service
		HttpEntity<NewUser> request = new HttpEntity<>(newUser);
		String url = "/public/login/create-user";
		ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);
		assertTrue(response.getBody().equals("Pseudo is empty"));
		assertTrue(response.getStatusCode() == HttpStatus.BAD_REQUEST);
	}

}
