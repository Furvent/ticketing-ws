package fr.eql.ticketing.restController;

import java.time.LocalDateTime;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
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
import fr.eql.ticketing.entity.Status;
import fr.eql.ticketing.entity.StatusHistory;
import fr.eql.ticketing.entity.Ticket;
import fr.eql.ticketing.entity.User;
import fr.eql.ticketing.enums.TicketStatus;
import fr.eql.ticketing.repository.GroupRepository;
import fr.eql.ticketing.repository.MembershipRepository;
import fr.eql.ticketing.repository.StatusHistoryRepository;
import fr.eql.ticketing.repository.StatusRepository;
import fr.eql.ticketing.repository.TaskRepository;
import fr.eql.ticketing.repository.TicketRepository;
import fr.eql.ticketing.repository.UserRepository;
import fr.eql.ticketing.restController.dto.update.UpdatedUser;


@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = TicketingApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase
public class GroupDashboardRestControllerIT {
	
    @Autowired
    private TestRestTemplate restTemplate;
    
    @Autowired
    private GroupRepository groupRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private MembershipRepository membershipRepository;
    
    @Autowired
    private TaskRepository taskRepository;
    
    @Autowired
    private TicketRepository ticketRepository;
    
    @Autowired
    private StatusHistoryRepository statusHistoryRepository;
    
    @Autowired
    private StatusRepository statusRepository;
    
	@AfterEach
	void cleanDbAfterTest() {
		statusHistoryRepository.deleteAll();
		taskRepository.deleteAll();
		ticketRepository.deleteAll();
		membershipRepository.deleteAll();
		groupRepository.deleteAll();
		userRepository.deleteAll();
	}
	@BeforeEach
	void createDb() {
		Status statusOpened = new Status(TicketStatus.OPENED);
		Status statusAllocated = new Status(TicketStatus.ALLOCATED);
		Status statusDone = new Status(TicketStatus.DONE);
		Status statusClosed = new Status(TicketStatus.CLOSED);
		Status statusCanceled = new Status(TicketStatus.CANCELED);
		statusRepository.save(statusOpened);
		statusRepository.save(statusAllocated);
		statusRepository.save(statusDone);
		statusRepository.save(statusClosed);
		statusRepository.save(statusCanceled);
	}
	@Disabled
	@Test
	void checkIfGetGroups() {
		
		//Create users for a group
		User mimi = new User("mimi", "123", "mimi92", LocalDateTime.now());
		userRepository.save(mimi);
		User boby = new User("boby", "123", "boby12", LocalDateTime.now());
		userRepository.save(boby);
		
		//create a group
		Group group1 = new Group("Ein Group", mimi, LocalDateTime.now());
		groupRepository.save(group1);
		
		//add users to group
		//Add memberships
		System.out.println("add memberships");
		membershipRepository.save(new Membership(mimi, group1, LocalDateTime.now()));
		membershipRepository.save(new Membership(boby, group1, LocalDateTime.now()));
		
		//create tickets
		Status statusAllocated = statusRepository.findById(1L).orElse(null);
		
		Ticket firstTicket = new Ticket("This is a ticket", "My first Ticket", group1);
		ticketRepository.save(firstTicket);
		System.out.println("Save first ticket");
		
		StatusHistory statusHistory = new StatusHistory(statusAllocated, firstTicket, LocalDateTime.now());
		statusHistoryRepository.save(statusHistory);
		Ticket SecondTicket = new Ticket("This is another ticket", "My second Ticket", group1);
		ticketRepository.save(firstTicket);
		/*
		statusHistoryRepository.save(new StatusHistory(statusAllocated, SecondTicket, LocalDateTime.now()));
		*/
		String url = "/private/group/";
		HttpEntity<Long> request = new HttpEntity<>(1L);	
		String response = restTemplate.postForObject(url, request, String.class);
		System.out.println(response);
	
		// TODO: see https://stackoverflow.com/questions/631598/how-to-use-junit-to-test-asynchronous-processes
		
	}
    
}
