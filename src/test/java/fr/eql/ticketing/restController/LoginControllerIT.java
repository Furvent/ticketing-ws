package fr.eql.ticketing.restController;

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
import org.springframework.test.context.junit.jupiter.SpringExtension;

import fr.eql.ticketing.TicketingApplication;
import fr.eql.ticketing.entity.User;
import fr.eql.ticketing.repository.UserRepository;
import fr.eql.ticketing.restController.dto.create.LoginForm;
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
	void cleanDbAfterTest() {
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
		PrivateUser response = restTemplate.postForObject(url, request, PrivateUser.class);
		assertTrue(response.getPseudo().equals("mimi92"));
	}

}
