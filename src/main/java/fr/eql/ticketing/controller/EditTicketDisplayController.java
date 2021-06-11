package fr.eql.ticketing.controller;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.view.RedirectView;

import fr.eql.ticketing.controller.form.TicketForm;
import fr.eql.ticketing.entity.Group;
import fr.eql.ticketing.entity.Membership;
import fr.eql.ticketing.entity.Status;
import fr.eql.ticketing.entity.StatusHistory;
import fr.eql.ticketing.entity.Task;
import fr.eql.ticketing.entity.Ticket;
import fr.eql.ticketing.entity.User;
import fr.eql.ticketing.enums.TicketStatus;
import fr.eql.ticketing.service.GroupService;
import fr.eql.ticketing.service.MembershipService;
import fr.eql.ticketing.service.StatusHistoryService;
import fr.eql.ticketing.service.StatusService;
import fr.eql.ticketing.service.TaskService;
import fr.eql.ticketing.service.TicketService;
import fr.eql.ticketing.service.UserService;

@Controller
@SessionAttributes(value = { "isUserAdmin", "groupSelectedByUserId", "ticket", "tasksLinkedWithTicket" })
public class EditTicketDisplayController {

	TicketService ticketService;
	TaskService taskService;
	StatusService statusService;
	StatusHistoryService statusHistoryService;
	GroupService groupService;
	MembershipService membershipService;
	UserService userService;

	public EditTicketDisplayController(TicketService ticketService, TaskService taskService,
			StatusService statusService, StatusHistoryService statusHistoryService, GroupService groupService,
			MembershipService membershipService, UserService userService) {
		this.ticketService = ticketService;
		this.taskService = taskService;
		this.statusService = statusService;
		this.statusHistoryService = statusHistoryService;
		this.groupService = groupService;
		this.membershipService = membershipService;
		this.userService = userService;
	}

	@GetMapping({ "ticket-admin" })
	public String displayEditTicket(Model model, @RequestParam(name = "ticketId", required = true) String ticketId) {
		if ((boolean) model.getAttribute("isUserAdmin")) {
			Ticket ticket = ticketService.getTicketById(Long.parseLong(ticketId));
			// On ajoute un objet intermédiaire pour gérer le formulaire du ticket
			TicketForm ticketForm = new TicketForm();
			ticketForm.setIdUsers(new ArrayList<Long>());
			model.addAttribute("ticket", ticket);
			// On veut les pseudos des personnes qui bossent dessus (Task)
			List<Task> tasksLinkedWithTicket = taskService.getTasksByTicket(ticket);
			model.addAttribute("tasksLinkedWithTicket", tasksLinkedWithTicket);
			List<String> usersPseudosWorkingOnTicket = new ArrayList<String>();
			for (Task task : tasksLinkedWithTicket) {
				// On ajoute les pseudos pour les afficher
				usersPseudosWorkingOnTicket.add(task.getUser().getPseudo());
				// On ajoute les utilisateurs déjà en task sur le ticketForm, pour qu'ils soient
				// pré-sélectionnés
				ticketForm.getIdUsers().add(task.getUser().getId());
			}
			model.addAttribute("usersPseudo", usersPseudosWorkingOnTicket);
			// On veut l'historique (Status, StatusHistory)
			List<StatusHistory> statusHistoricOfTicket = statusHistoryService.getStatusHistoriesFromThisTicket(ticket);
			setOptionsRadioButtonsOnTicketForm(ticketForm, statusHistoricOfTicket);
			model.addAttribute("historic", statusHistoricOfTicket);
			// On ajoute les id des utilisateurs en task sur ce ticket, pour le ticketForm
			// On récupère tous les utilisateurs de ce groupe
			List<User> usersGroup = getUsersByGroup(model);
			model.addAttribute("usersGroup", usersGroup);
			ticketForm.setDescription("");
			System.err.println("ticketForm before send it to template: " + ticketForm);
			model.addAttribute("ticketForm", ticketForm);
			return "ticket/admin.html";
		} else { // Si utilisateur n'est pas admin
			return "/";
		}
	}

	private void setOptionsRadioButtonsOnTicketForm(TicketForm ticketForm, List<StatusHistory> statusHistoricOfTicket) {
		boolean hasFoundStatusDone = false, hasFoundStatusClosed = false;
		ticketForm.setRadioButtonsOptions(new ArrayList<String>());
		for (StatusHistory statusHistory : statusHistoricOfTicket) {
			switch (statusHistory.getStatus().getLabel()) {
			case TicketStatus.DONE:
				hasFoundStatusDone = true;
				break;
			case TicketStatus.CLOSED:
				hasFoundStatusClosed = true;
				break;
			}
		}
		if (!hasFoundStatusDone)
			ticketForm.getRadioButtonsOptions().add(TicketStatus.DONE);
		if (!hasFoundStatusClosed)
			ticketForm.getRadioButtonsOptions().add(TicketStatus.CLOSED);
	}

	@PostMapping({ "/modifTicket" })
	public RedirectView editTicketbyPost(Model model, @ModelAttribute("ticketForm") TicketForm ticketForm) {
		Ticket ticket = (Ticket) model.getAttribute("ticket");
		System.err.println("ticket form in postMapping" + ticketForm);
		if (ticket == null) {
			return new RedirectView("/");
		}
		if (ticketForm.getTitle() != "") {
			ticket.setTitle(ticketForm.getTitle());
		}
		if (ticketForm.getDescription() != "") {
			ticket.setDetails(ticketForm.getDescription());
		}
		if (ticketForm.getIdUsers().size() > 0) {
			checkIfNewUserAddOnTaskAndStatusHistory(ticketForm.getIdUsers(), ticket, model);
		}
		if (ticketForm.getRadioSelected() != null && ticketForm.getRadioSelected() != "") {
			setNewStatusOnTicket(ticketForm.getRadioSelected(), ticket);
		}
		ticketService.save(ticket);
		return new RedirectView("ticket-admin?ticketId=" + ticket.getId());

	}

	private void checkIfNewUserAddOnTaskAndStatusHistory(List<Long> idUsersFromTicketForm, Ticket ticket, Model model) {
		// We must check if we have new user add on task
		List<Task> taskAlreadyOnTicket = (List<Task>) model.getAttribute("tasksLinkedWithTicket");
		List<Long> usersAlreadyOnTaskIds = new ArrayList<Long>();
		for (Task task : taskAlreadyOnTicket) {
			usersAlreadyOnTaskIds.add(task.getUser().getId());
		}
		for (Long id : idUsersFromTicketForm) {
			// Si un utilisateur n'est pas déjà attribué
			if (!usersAlreadyOnTaskIds.contains(id)) {
				// Appel en db non optimum
				// On récupère l'utilisateur en db à partir de son id
				User user = userService.getUserWithId(id);
				// On rajoute la relation task (user-ticket)
				Task task = new Task(user, ticket, LocalDateTime.now());
				// On rajoute un statut dans l'historique des statuts
				// Je veux trouver le bon statut en db
				Status allocatedStatus = statusService.getStatusByLabel(TicketStatus.ALLOCATED);
				// On créé un nouveau status history (lien ticket-status)
				StatusHistory statusHistory = new StatusHistory(allocatedStatus, ticket, LocalDateTime.now());
				// On sauve en db les nouvelles relations task et statusHistory
				taskService.save(task);
				statusHistoryService.save(statusHistory);
			}
		}
	}

	private List<User> getUsersByGroup(Model model) {
		// CACA
		Group group = groupService.getGroupById(Long.parseLong((String) model.getAttribute("groupSelectedByUserId")));
		List<Membership> memberships = membershipService.getMembershipsWithGroup(group);
		List<User> users = new ArrayList<User>();
		for (Membership membership : memberships) {
			users.add(membership.getUser());
		}
		return users;
	}

	private void setNewStatusOnTicket(String newStatus, Ticket ticket) {
		// On fait un switch pour s'assurer que le nom soit correcte (on pourrait
		// s'affranchir du switch mais si une mauvaise valeur de status est forcée par
		// l'utilisateur, plantage.
		Status status;
		StatusHistory statusHistory;
		switch (newStatus) {
		case TicketStatus.DONE:
			status = statusService.getStatusByLabel(TicketStatus.DONE);
			statusHistory = new StatusHistory(status, ticket, LocalDateTime.now());
			statusHistoryService.save(statusHistory);
			break;
		case TicketStatus.CLOSED:
			status = statusService.getStatusByLabel(TicketStatus.CLOSED);
			statusHistory = new StatusHistory(status, ticket, LocalDateTime.now());
			statusHistoryService.save(statusHistory);
			break;
		}
	}

}
