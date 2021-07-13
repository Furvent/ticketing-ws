package fr.eql.ticketing.restController;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import fr.eql.ticketing.TicketingApplication;
import fr.eql.ticketing.entity.Group;
import fr.eql.ticketing.entity.Membership;
import fr.eql.ticketing.entity.User;
import fr.eql.ticketing.repository.GroupRepository;
import fr.eql.ticketing.repository.MembershipRepository;
import fr.eql.ticketing.repository.UserRepository;
import fr.eql.ticketing.restController.dto.create.NewGroup;
import fr.eql.ticketing.restController.dto.read.GeneralDashboardData;
import fr.eql.ticketing.restController.dto.read.GroupData;
import fr.eql.ticketing.restController.dto.read.PrivateUser;
import fr.eql.ticketing.restController.dto.update.UpdatedUser;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = TicketingApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase
public class GeneralDashboardRestControllerIT {

	@Autowired
	private TestRestTemplate restTemplate;

	@Autowired
	private GroupRepository groupRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private MembershipRepository membershipRepository;

	@BeforeEach
	void cleanDbBeforeEachTest() {
		membershipRepository.deleteAll();
		groupRepository.deleteAll();
		userRepository.deleteAll();

	}

	@Test
	void itShouldReturnGeneralDasboardDataWithTwoGroups() {
		final String groupName1 = "Ein Group";
		final String groupName2 = "Another Group";
		// Add 3 users.
		User mimi = new User("mimi", "123", "mimi92", LocalDateTime.now());
		User boby = new User("boby", "123", "boby12", LocalDateTime.now());
		User billy = new User("billy", "123", "billy12", LocalDateTime.now());
		userRepository.save(mimi);
		userRepository.save(boby);
		userRepository.save(billy);

		// create groups for testing
		Group group1 = new Group(groupName1, mimi, LocalDateTime.now());
		Group group2 = new Group(groupName2, billy, LocalDateTime.now());
		groupRepository.save(group1);
		groupRepository.save(group2);

		// Add memberships to group 1
		membershipRepository.save(new Membership(mimi, group1, LocalDateTime.now()));
		membershipRepository.save(new Membership(boby, group1, LocalDateTime.now()));

		// Add memberships to group 2
		membershipRepository.save(new Membership(billy, group2, LocalDateTime.now()));
		membershipRepository.save(new Membership(mimi, group2, LocalDateTime.now()));

		Long mimiId = mimi.getId();

		// Call the service
		HttpEntity<Long> request = new HttpEntity<>(mimiId);
		String url = "/private/dashboard";
		ResponseEntity<GeneralDashboardData> response = restTemplate.postForEntity(url, request,
				GeneralDashboardData.class);
		assertTrue(response.getBody().getGroupsData().size() == 2);
		assertTrue(response.getStatusCode() == HttpStatus.OK);
		response.getBody().getGroupsData().forEach(groupData -> {
			assertTrue(groupData.getUsers().size() == 2);
			if (groupData.getId() == group1.getId()) {
				assertTrue(groupData.getName().equals(groupName1));
				assertTrue(groupData.getCreatorId() == mimi.getId());
			}
			if (groupData.getId() == group2.getId()) {
				assertTrue(groupData.getName().equals(groupName2));
				assertTrue(groupData.getCreatorId() == billy.getId());
			}
		});
	}

	@Test
	void itShouldReturnGeneralDasboardDataWithOneGroup() {
		// Add 3 users.
		User mimi = new User("mimi", "123", "mimi92", LocalDateTime.now());
		User boby = new User("boby", "123", "boby12", LocalDateTime.now());
		User billy = new User("billy", "123", "billy12", LocalDateTime.now());
		userRepository.save(mimi);
		userRepository.save(boby);
		userRepository.save(billy);

		// create groups for testing
		Group group1 = new Group("Ein Group", mimi, LocalDateTime.now());
		Group group2 = new Group("Another Group", billy, LocalDateTime.now());
		groupRepository.save(group1);
		groupRepository.save(group2);

		// Add memberships to group 1
		membershipRepository.save(new Membership(mimi, group1, LocalDateTime.now()));
		membershipRepository.save(new Membership(boby, group1, LocalDateTime.now()));

		// Add memberships to group 2
		membershipRepository.save(new Membership(mimi, group2, LocalDateTime.now()));
		membershipRepository.save(new Membership(billy, group2, LocalDateTime.now()));

		Long bobyId = boby.getId();

		// Call the service
		HttpEntity<Long> request = new HttpEntity<>(bobyId);
		String url = "/private/dashboard";
		ResponseEntity<GeneralDashboardData> response = restTemplate.postForEntity(url, request,
				GeneralDashboardData.class);
		assertTrue(response.getBody().getGroupsData().size() == 1);
		assertTrue(response.getStatusCode() == HttpStatus.OK);
	}

	@Test
	void itShouldNotReturnGeneralDasboardData() {
		// Add 3 users.
		User mimi = new User("mimi", "123", "mimi92", LocalDateTime.now());
		User boby = new User("boby", "123", "boby12", LocalDateTime.now());
		User billy = new User("billy", "123", "billy12", LocalDateTime.now());
		userRepository.save(mimi);
		userRepository.save(boby);
		userRepository.save(billy);

		// create groups for testing
		Group group1 = new Group("Ein Group", mimi, LocalDateTime.now());
		Group group2 = new Group("Another Group", billy, LocalDateTime.now());
		groupRepository.save(group1);
		groupRepository.save(group2);

		// Add memberships to group 1
		membershipRepository.save(new Membership(mimi, group1, LocalDateTime.now()));
		membershipRepository.save(new Membership(boby, group1, LocalDateTime.now()));

		// Add memberships to group 2
		membershipRepository.save(new Membership(mimi, group2, LocalDateTime.now()));
		membershipRepository.save(new Membership(billy, group2, LocalDateTime.now()));

		// Call the service
		HttpEntity<Long> request = new HttpEntity<>(5L);
		String url = "/private/dashboard";
		ResponseEntity<GeneralDashboardData> response = restTemplate.postForEntity(url, request,
				GeneralDashboardData.class);
		assertNull(response.getBody());
		assertTrue(response.getStatusCode() == HttpStatus.BAD_REQUEST);
	}

	@Test
	void itShouldCreateGroup() {
		final String groupName1 = "Ein Group";
		User mimi = new User("mimi", "123", "mimi92", LocalDateTime.now());
		userRepository.save(mimi);

		NewGroup newGroup = new NewGroup();
		newGroup.setCreatorId(mimi.getId());
		newGroup.setName(groupName1);

		String url = "/private/dashboard/addGroup/";
		HttpEntity<NewGroup> request = new HttpEntity<>(newGroup);
		ResponseEntity<GroupData> response = restTemplate.postForEntity(url, request, GroupData.class);

		Group groupInDB = groupRepository.findAll().get(0);
		assertTrue(groupInDB.getName().equals(newGroup.getName()));
		assertTrue(response.getBody().getName().equals(groupName1));
		assertTrue(response.getBody().getCreatorId() == mimi.getId());
	}

	@Test
	void itShouldNotCreateGroupBecauseMissingName() {
		final String groupName1 = "";
		User mimi = new User("mimi", "123", "mimi92", LocalDateTime.now());
		userRepository.save(mimi);

		NewGroup newGroup = new NewGroup();
		newGroup.setCreatorId(mimi.getId());
		newGroup.setName(groupName1);

		String url = "/private/dashboard/addGroup/";
		HttpEntity<NewGroup> request = new HttpEntity<>(newGroup);
		ResponseEntity<GroupData> response = restTemplate.postForEntity(url, request, GroupData.class);

		assertNull(response.getBody());
		assertTrue(response.getStatusCode() == HttpStatus.BAD_REQUEST);
		assertTrue(groupRepository.findAll().size() == 0);
	}

	@Test
	void itShouldNotCreateGroupBecauseWrongCreatorId() {
		final String groupName1 = "Ein Group";
		User mimi = new User("mimi", "123", "mimi92", LocalDateTime.now());
		userRepository.save(mimi);

		NewGroup newGroup = new NewGroup();
		newGroup.setCreatorId(4L);
		newGroup.setName(groupName1);

		String url = "/private/dashboard/addGroup/";
		HttpEntity<NewGroup> request = new HttpEntity<>(newGroup);
		ResponseEntity<GroupData> response = restTemplate.postForEntity(url, request, GroupData.class);

		assertNull(response.getBody());
		assertTrue(response.getStatusCode() == HttpStatus.BAD_REQUEST);
		assertTrue(groupRepository.findAll().size() == 0);
	}

	@Test
	void itShouldUpdateUserData() {
		final String username = "mimi";
		final String oldPassword = "123";
		final String newPassword = "321";
		final String oldPseudo = "mimi92";
		final String newPseudo = "29imim";

		User mimi = new User(username, oldPassword, oldPseudo, LocalDateTime.now());
		userRepository.save(mimi);

		UpdatedUser updatedUser = new UpdatedUser();
		updatedUser.setOldPassword(oldPassword);
		updatedUser.setNewPassword(newPassword);
		updatedUser.setNewPseudo(newPseudo);
		updatedUser.setUsername(username);

		String url = "/private/dashboard/updateUser/";
		HttpEntity<UpdatedUser> request = new HttpEntity<>(updatedUser);
		ResponseEntity<PrivateUser> response = restTemplate.exchange(url, HttpMethod.PUT, request, PrivateUser.class);
		assertTrue(response.getBody().getPseudo().equals(newPseudo));
		assertTrue(response.getBody().getUsername().equals(username));
		assertTrue(response.getBody().getId() == mimi.getId());
		assertTrue(response.getStatusCode() == HttpStatus.OK);

		Optional<User> mimiInDb = userRepository.findById(mimi.getId());
		assertNotNull(mimiInDb.get());
		assertTrue(mimiInDb.get().getPassword().equals(newPassword));
	}
	
	@Test
	void itShouldNotUpdateUserDataBecauseWrongOldPassword() {
		final String username = "mimi";
		final String oldPassword = "123";
		final String oldPasswordWrong = "124";
		final String newPassword = "321";
		final String oldPseudo = "mimi92";
		final String newPseudo = "29imim";

		User mimi = new User(username, oldPassword, oldPseudo, LocalDateTime.now());
		userRepository.save(mimi);

		UpdatedUser updatedUser = new UpdatedUser();
		updatedUser.setOldPassword(oldPasswordWrong);
		updatedUser.setNewPassword(newPassword);
		updatedUser.setNewPseudo(newPseudo);
		updatedUser.setUsername(username);

		String url = "/private/dashboard/updateUser/";
		HttpEntity<UpdatedUser> request = new HttpEntity<>(updatedUser);
		ResponseEntity<PrivateUser> response = restTemplate.exchange(url, HttpMethod.PUT, request, PrivateUser.class);
		assertTrue(response.getStatusCode() == HttpStatus.BAD_REQUEST);

		Optional<User> mimiInDb = userRepository.findById(mimi.getId());
		assertNotNull(mimiInDb.get());
		assertTrue(mimiInDb.get().getPassword().equals(oldPassword));
	}
}
