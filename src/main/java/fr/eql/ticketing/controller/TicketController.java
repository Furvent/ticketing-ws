package fr.eql.ticketing.controller;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.view.RedirectView;

import fr.eql.ticketing.controller.form.TicketForm;
import fr.eql.ticketing.entity.Status;
import fr.eql.ticketing.entity.StatusHistory;
import fr.eql.ticketing.entity.Task;
import fr.eql.ticketing.entity.Ticket;
import fr.eql.ticketing.service.GroupService;
import fr.eql.ticketing.service.MembershipService;
import fr.eql.ticketing.service.StatusHistoryService;
import fr.eql.ticketing.service.StatusService;
import fr.eql.ticketing.service.TaskService;
import fr.eql.ticketing.service.TicketService;
import fr.eql.ticketing.service.UserService;

@Controller
@SessionAttributes(value = { "groupSelectedByUserId" })
public class TicketController {

	TicketService service;
	StatusService statusService;
	GroupService groupService;
	MembershipService membershipService;
	UserService userService;
	StatusHistoryService historyService;
	TaskService taskService;

	public TicketController(TicketService service, StatusService statusService, GroupService groupService,
			MembershipService membershipService, UserService userService, StatusHistoryService historyService,
			TaskService taskService) {
		this.service = service;
		this.statusService = statusService;
		this.groupService = groupService;
		this.membershipService = membershipService;
		this.userService = userService;
		this.historyService = historyService;
		this.taskService = taskService;
	}

	@GetMapping("/list-tickets")
	public String displayUsers(Model model) {
		List<Ticket> tickets = service.getAllTickets();
		model.addAttribute("tickets", tickets);
		return "ticketsDebug";
	}

	@PostMapping("/addNewTicket")
	public String addNewTicket() {
		return "GeneralDashboard";
	}

	// Méthode pour tester la selection sur une liste déroulante
	@GetMapping("/test/test-SelectMenu")
	public String testMenu(Model model) {
		List<Status> status = new ArrayList<Status>();
		status = statusService.getAllStatus();
		model.addAttribute("status", status);
		Status selectedStatus = new Status();
		model.addAttribute("aStatus", selectedStatus);
		return "/test/test-SelectMenu";
	}

	// Page renvoyée par le test
	@PostMapping("/create-new-ticket")
	public RedirectView testMenu2(@ModelAttribute("ticketForm") TicketForm ticketForm, Model model) {
		List<Long> idUsers = ticketForm.getIdUsers();
		String selectedGroupId = (String) model.getAttribute("groupSelectedByUserId");
		String ticketTitle = ticketForm.getTitle();
		String description = ticketForm.getDescription();
		Set<StatusHistory> statusHistorys = new HashSet<StatusHistory>();
		model.addAttribute("idUsers", idUsers);
		model.addAttribute("ticketTitle", ticketTitle);
		// Ajout du nouveau ticket en BDD
		// Create ticket
		Ticket ticket = new Ticket();
		model.addAttribute("description", description);
		ticket.setTitle(ticketTitle);
		ticket.setDetails(description);
		ticket.setGroup(groupService.getGroupById(Long.parseLong(selectedGroupId)));
		service.save(ticket);

		// Create status historic
		Long idStatus = 1L;
		if (idUsers.size() > 0) {
			statusHistorys.add(saveStatusHistory(idStatus, ticket));
			idStatus = 2L;
		}

		// create task
		Set<Task> tasks = new HashSet<Task>();
		for (Long idUser : idUsers) {
			Task task = new Task(userService.getUserWithId(idUser), ticket, LocalDateTime.now());
			taskService.save(task);
			tasks.add(task);
		}
		statusHistorys.add(saveStatusHistory(idStatus, ticket));
		ticket.setStatusHistory(statusHistorys);
		ticket.setTasks(tasks);
		service.save(ticket);
		return new RedirectView("/group?groupId=" + selectedGroupId);
	}

	private StatusHistory saveStatusHistory(Long idStatus, Ticket ticket) {
		Status status = statusService.getStatusById(idStatus);
		StatusHistory statusHistory = new StatusHistory(status, ticket, LocalDateTime.now());
		historyService.save(statusHistory);
		return statusHistory;
	}
}
