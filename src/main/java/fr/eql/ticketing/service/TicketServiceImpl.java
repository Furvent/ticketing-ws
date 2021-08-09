package fr.eql.ticketing.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.eql.ticketing.entity.Group;
import fr.eql.ticketing.entity.Status;
import fr.eql.ticketing.entity.StatusHistory;
import fr.eql.ticketing.entity.Task;
import fr.eql.ticketing.entity.Ticket;
import fr.eql.ticketing.entity.User;
import fr.eql.ticketing.enums.TicketStatus;
import fr.eql.ticketing.exception.restController.InvalidNewDataPostException;
import fr.eql.ticketing.repository.TicketRepository;
import fr.eql.ticketing.restController.dto.create.NewTicket;
import fr.eql.ticketing.restController.dto.read.PublicUser;
import fr.eql.ticketing.restController.dto.update.UpdatedTicket;

@Service
public class TicketServiceImpl implements TicketService {
	private TicketRepository repository;

	@Autowired
	private GroupService groupService;
	@Autowired
	private StatusService statusService;
	@Autowired
	private StatusHistoryService statusHistoryService;
	@Autowired
	private UserService userService;
	@Autowired
	private TaskService taskService;

	public TicketServiceImpl(TicketRepository repository) {
		this.repository = repository;
	}

	@Override
	public Ticket create(NewTicket newTicket) {
		// Checks
		if (newTicket.getTitle().isEmpty()) {
			throw new InvalidNewDataPostException("New ticket's title cannot be empty");
		}
		if (newTicket.getDescription().isEmpty()) {
			throw new InvalidNewDataPostException("New ticket's description cannot be empty");
		}
		if (!groupService.checkIfGroupExistWithThisId(newTicket.getGroupId())) {
			throw new InvalidNewDataPostException("Invalid group id");
		}
		Group ticketGroup = groupService.getGroupById(newTicket.getGroupId());
		// Add ticket and if user is on task, add relation
		Ticket ticketEntity = new Ticket(newTicket.getTitle(), newTicket.getDescription(), ticketGroup);
		// We save ticket to get id
		this.save(ticketEntity);
		// Add Status history for creation
		Status statusOpened = statusService.getStatusByLabel(TicketStatus.OPENED);
		StatusHistory statusHistoryEntity = new StatusHistory(statusOpened, ticketEntity, LocalDateTime.now());
		statusHistoryService.save(statusHistoryEntity);
		ticketEntity.getStatusHistory().add(statusHistoryEntity);
		return ticketEntity;
	}

	@Override
	public Ticket update(UpdatedTicket updatedTicket, String ticketId) {
		Ticket ticketEntity = this.getTicketById(updatedTicket.getTicketId());
		if (ticketEntity == null) {
			throw new InvalidNewDataPostException(
					"Ticket with id - " + updatedTicket.getTicketId() + " - doesn't exist.");
		}
		if (updatedTicket.getUsersOnTask().size() > 0) {
			this.addTaskAndHistoryOnTicketWithPublicUsers(ticketEntity, updatedTicket.getUsersOnTask());
		}
		if (!updatedTicket.getNewDescription().isEmpty()) {
			ticketEntity.setDescription(updatedTicket.getNewDescription());
		}
		if (!updatedTicket.getNewTitle().isEmpty()) {
			ticketEntity.setTitle(updatedTicket.getNewTitle());
		}
		if (!updatedTicket.getNewStatus().isEmpty()) {
			try {
				this.addNewStatusOnUpdatedTicket(ticketEntity, updatedTicket.getNewStatus());
			} catch (Exception e) {
				throw e;
			}
		}
		this.save(ticketEntity);
		return ticketEntity;
	}

	@Override
	public Ticket save(Ticket ticket) {
		return repository.save(ticket);
	}

	@Override
	public Ticket getTicketById(Long ticketId) {
		Optional<Ticket> ticket = repository.findById(ticketId);
		return ticket.isPresent() ? ticket.get() : null;
	}

	@Override
	public List<Ticket> getAllTickets() {
		return repository.findAll();
	}

	@Override
	public void delete(Long ticketId) {
		Ticket ticket = this.getTicketById(ticketId);
		if (ticket == null) {
			throw new InvalidNewDataPostException(
					"Ticket with id -" + ticketId + "- doesn't exist.");
		}
		this.addNewStatusOnUpdatedTicket(ticket, TicketStatus.CANCELED);
	}

	private void addTaskAndHistoryOnTicketWithPublicUsers(Ticket ticket, List<PublicUser> publicUsers)
			throws InvalidNewDataPostException {
		// Get all users entities from the list of public users collected in request
		List<User> usersOnTaskCollected = this.getUsersFromPublicUsers(publicUsers);
		// If missing user entity, throws exception
		if (usersOnTaskCollected.size() != publicUsers.size()) {
			throw new InvalidNewDataPostException("Cannot found users entities to add them on task");
		}
		// Users already on task
		List<User> usersAlreadyOnTask = ticket.getTasks().stream().map(task -> task.getUser())
				.collect(Collectors.toList());
		// Create a list with only new users to add on task
		List<User> usersToAddOnTask = usersOnTaskCollected.stream().filter(userCollected -> {
			return !usersAlreadyOnTask.contains(userCollected);
		}).collect(Collectors.toList());
		// For each user to add, add a task and add a new status on the ticket
		List<Task> tasks = new ArrayList<Task>();
		List<StatusHistory> history = new ArrayList<StatusHistory>();
		Status statusAllocated = statusService.getStatusByLabel(TicketStatus.ALLOCATED);
		usersToAddOnTask.forEach(userOnTask -> {
			LocalDateTime now = LocalDateTime.now().plusSeconds(1);
			// Create task
			Task task = new Task(userOnTask, ticket, now);
			tasks.add(task);
			// Create statusHistory
			StatusHistory statusHistory = new StatusHistory(statusAllocated, ticket, now);
			history.add(statusHistory);
			// Add Task and statusHistory on ticketEntity
			ticket.getStatusHistory().add(statusHistory);
			ticket.getTasks().add(task);
		});
		// Save new entities
		taskService.saveAll(tasks);
		statusHistoryService.saveAll(history);
	}

	private List<User> getUsersFromPublicUsers(List<PublicUser> publicUsers) {
		return userService.getMultipleUsersWithIds(
				publicUsers.stream().map(publicUser -> publicUser.getId()).collect(Collectors.toList()));
	}

	private void addNewStatusOnUpdatedTicket(Ticket ticket, String statusLabel) {
		// TODO: Check if last status in history is the same of the new label status
		if (this.isStatusLabelValidWhenUpdatingTicket(statusLabel)) {
			Status status = statusService.getStatusByLabel(statusLabel);
			StatusHistory statusHistory = new StatusHistory(status, ticket, LocalDateTime.now().plusSeconds(2));
			statusHistoryService.save(statusHistory);
		} else {
			throw new InvalidNewDataPostException("Cannot add status - " + statusLabel + " - on a ticket updated");
		}
	}

	/**
	 * Status label cannot be TicketStatus.OPENED or ALLOCATED or a value not in the
	 * enum
	 * 
	 * @return
	 */
	private boolean isStatusLabelValidWhenUpdatingTicket(String label) {
		return TicketStatus.isValid(label) && !label.equals(TicketStatus.OPENED)
				&& !label.equals(TicketStatus.ALLOCATED);
	}

}
