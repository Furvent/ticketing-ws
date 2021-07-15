package fr.eql.ticketing.restController;

import static org.junit.Assert.assertTrue;

import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.client.AsyncRestTemplate;

import fr.eql.ticketing.TicketingApplication;
import fr.eql.ticketing.entity.Group;
import fr.eql.ticketing.entity.Membership;
import fr.eql.ticketing.entity.Status;
import fr.eql.ticketing.entity.StatusHistory;
import fr.eql.ticketing.entity.Task;
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
import fr.eql.ticketing.restController.dto.read.GroupDashboardData;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = TicketingApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase
@TestInstance(Lifecycle.PER_CLASS)
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

	@BeforeAll
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

	@BeforeEach
	void cleanDbAfterTest() {
		statusHistoryRepository.deleteAll();
		taskRepository.deleteAll();
		ticketRepository.deleteAll();
		membershipRepository.deleteAll();
		groupRepository.deleteAll();
		userRepository.deleteAll();
	}

	@Test
	void itShouldReturnGroupDashboardData() throws InterruptedException {
		final String mockedTicketTitle1 = "title_1";
		final String mockedTicketTitle2 = "title_2";
		final String mockedTicketDescription1 = "description_1";
		final String mockedTicketDescription2 = "description_2";
		// Create users for a group
		User mimi = new User("mimi", "123", "mimi92", LocalDateTime.now());
		userRepository.save(mimi);
		User boby = new User("boby", "123", "boby12", LocalDateTime.now());
		userRepository.save(boby);
		User sarah = new User("sarah", "123", "sarah42", LocalDateTime.now());
		userRepository.save(sarah);

		// Create a group
		Group group1 = new Group("Ein Group", mimi, LocalDateTime.now());
		groupRepository.save(group1);
		final long group1Id = group1.getId();

		// Add users to group
		// Add memberships
		membershipRepository.save(new Membership(mimi, group1, LocalDateTime.now()));
		membershipRepository.save(new Membership(boby, group1, LocalDateTime.now()));
		membershipRepository.save(new Membership(sarah, group1, LocalDateTime.now()));

		// Create tickets
		// Get status ref
		Status statusOpened = statusRepository.findByLabel(TicketStatus.OPENED).get();
		Status statusAllocated = statusRepository.findByLabel(TicketStatus.ALLOCATED).get();

		Ticket ticket1 = new Ticket(mockedTicketTitle1, mockedTicketDescription1, group1);
		Ticket ticket2 = new Ticket(mockedTicketTitle2, mockedTicketDescription2, group1);
		ticketRepository.save(ticket1);
		ticketRepository.save(ticket2);
		// Create statusHistory opened and join to ticket
		StatusHistory statusHistoryTicket1Opened = new StatusHistory(statusOpened, ticket1, LocalDateTime.now());
		StatusHistory statusHistoryTicket2Opened = new StatusHistory(statusOpened, ticket2, LocalDateTime.now());
		statusHistoryRepository.save(statusHistoryTicket1Opened);
		statusHistoryRepository.save(statusHistoryTicket2Opened);
		// Create Task and statusHistory allocated and joined to ticket 2
		Task taskBetweenMimiAndTicket2 = new Task(mimi, ticket2, LocalDateTime.now().plusSeconds(1));
		StatusHistory statusHistoryTicket2Allocated = new StatusHistory(statusOpened, ticket2, LocalDateTime.now().plusSeconds(1));
		taskRepository.save(taskBetweenMimiAndTicket2);
		statusHistoryRepository.save(statusHistoryTicket2Allocated);
		// Use to refresh data in tickets entities, because no EntityManager in repository...
		// TODO: see https://stackoverflow.com/questions/45491551/refresh-and-fetch-an-entity-after-save-jpa-spring-data-hibernate
//		ticket1 = ticketRepository.findById(ticket1.getId()).get();
//		ticket2 = ticketRepository.findById(ticket2.getId()).get();

		String url = "/private/group/";
		HttpEntity<Long> request = new HttpEntity<>(group1Id);
		ResponseEntity<GroupDashboardData> response = restTemplate.postForEntity(url, request,
				GroupDashboardData.class);
		// Test returned code
		assertTrue(response.getStatusCode() == HttpStatus.OK);
		// Test GroupData in response
		assertTrue(response.getBody().getGroupData().getId() == group1Id);
		assertTrue(response.getBody().getGroupData().getCreatorId() == mimi.getId());
		assertTrue(response.getBody().getGroupData().getName().equals(group1.getName()));
		assertTrue(response.getBody().getGroupData().getUsers().size() == 3);
		response.getBody().getGroupData().getUsers().forEach(user -> {
			assertTrue(user.getId() == mimi.getId() || user.getId() == boby.getId() || user.getId() == sarah.getId());
			assertTrue(user.getPseudo().equals(mimi.getPseudo()) || user.getPseudo().equals(boby.getPseudo())
					|| user.getPseudo().equals(sarah.getPseudo()));
		});
		//Test Tickets data in response
		assertTrue(response.getBody().getTicketsData().size() == 2);
		response.getBody().getTicketsData().forEach(ticket -> {
			if (ticket.getId() == ticket1.getId()) {
				assertTrue(ticket.getTitle().equals(mockedTicketTitle1));
				assertTrue(ticket.getDescription().equals(mockedTicketDescription1));
				assertTrue(ticket.getHistory().size() == 1);
				ticket.getHistory().forEach(status -> {
					assertTrue(status.getLabel().equals(TicketStatus.OPENED));
				});
			} else if (ticket.getId() == ticket2.getId()) {
				assertTrue(ticket.getTitle().equals(mockedTicketTitle2));
				assertTrue(ticket.getDescription().equals(mockedTicketDescription2));
				assertTrue(ticket.getHistory().size() == 2);
				ticket.getHistory().forEach(status -> {
					assertTrue(status.getLabel().equals(TicketStatus.OPENED) || status.getLabel().equals(TicketStatus.ALLOCATED));
				});
				assertTrue(ticket.getUsersOnTask().size() == 1);
				assertTrue(ticket.getUsersOnTask().iterator().next().getPseudo().equals(mimi.getPseudo()));
			}
		});

	}

}
