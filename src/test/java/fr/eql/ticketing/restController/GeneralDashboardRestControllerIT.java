package fr.eql.ticketing.restController;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
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
    
	@AfterEach
	void cleanDbAfterTest() {
		membershipRepository.deleteAll();
		groupRepository.deleteAll();
		userRepository.deleteAll();
		
	}
	
	//@Disabled
	@Test
	void checkIfGetGroups() {
		
		//Add 3 users.
		User mimi = new User("mimi", "123", "mimi92", LocalDateTime.now());
		userRepository.save(mimi);
		User boby = new User("boby", "123", "boby12", LocalDateTime.now());
		userRepository.save(boby);
		User billy = new User("billy", "123", "billy12", LocalDateTime.now());
		userRepository.save(billy);

		
		//create groups for testing
		Group group1 = new Group("Ein Group", mimi, LocalDateTime.now());
		groupRepository.save(group1);
		Group group2 = new Group("Another Group", billy, LocalDateTime.now());
		groupRepository.save(group2);
		
		
		//Add memberships
		membershipRepository.save(new Membership(mimi, group1, LocalDateTime.now()));
		membershipRepository.save(new Membership(boby, group1, LocalDateTime.now()));
		
		membershipRepository.save(new Membership(billy, group2, LocalDateTime.now()));
		membershipRepository.save(new Membership(mimi, group2, LocalDateTime.now()));
		System.out.println(mimi.getId());
		
		//Call the service
		HttpEntity<Long> request = new HttpEntity<>(1L);	
		String url = "/private/dashboard";
		GeneralDashboardData response = restTemplate.postForObject(url, request, GeneralDashboardData.class);
		assertTrue(response.getGroupsData().size() == 2);
	}
	
	@Test
	void checkIfCreateGroup() {
		
		User mimi = new User("mimi", "123", "mimi92", LocalDateTime.now());
		userRepository.save(mimi);
		
		NewGroup newGroup = new NewGroup();
		newGroup.setCreatorId(mimi.getId());
		newGroup.setName("TestGroup");
		
		String url = "/private/dashboard/addGroup/";
		HttpEntity<NewGroup> request = new HttpEntity<>(newGroup);	
		restTemplate.postForObject(url, request, GroupData.class);
		
		Group groupInDB = groupRepository.findAll().get(0);
		assertTrue(groupInDB.getName().equals(newGroup.getName()));
	}

	
	@Test
	void checkIfUpdate() {
		
		User mimi = new User("mimi", "123", "mimi92", LocalDateTime.now());
		userRepository.save(mimi);
		
		UpdatedUser updatedUser = new UpdatedUser();
		updatedUser.setOldPassword("123");
		updatedUser.setNewPassword("456");
		updatedUser.setNewPseudo("Joséphine");
		updatedUser.setUsername("mimi");
		
		String url = "/private/dashboard/updateUser/";
		HttpEntity<UpdatedUser> request = new HttpEntity<>(updatedUser);	
		restTemplate.put(url, request);
		User savedModifiedUser = userRepository.findByUsername("mimi").orElse(null);
		System.out.println(savedModifiedUser.getPseudo());
		System.out.println(updatedUser.getNewPseudo());
		assertTrue(savedModifiedUser.getPseudo().equals(updatedUser.getNewPseudo()));
		
		
	}
}
