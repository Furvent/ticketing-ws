package fr.eql.ticketing.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;

import fr.eql.ticketing.entity.StatusHistory;
import fr.eql.ticketing.entity.Task;
import fr.eql.ticketing.entity.Ticket;
import fr.eql.ticketing.service.StatusHistoryService;
import fr.eql.ticketing.service.TaskService;
import fr.eql.ticketing.service.TicketService;

@Controller
@SessionAttributes(value = { "groupSelectedByUserId" })
public class TicketDisplayController {

	TicketService ticketService;
	TaskService taskService;
	StatusHistoryService statusHistoryService;

	public TicketDisplayController(TicketService ticketService, TaskService taskService,
			StatusHistoryService statusHistoryService) {
		super();
		this.ticketService = ticketService;
		this.taskService = taskService;
		this.statusHistoryService = statusHistoryService;
	}

	@GetMapping("ticket")
	public String displayTicketWithId(Model model, @RequestParam(name = "ticketId", required = true) String ticketId) {
		// On veut le détail du ticket
		Ticket ticket = ticketService.getTicketById(Long.parseLong(ticketId));
		model.addAttribute("ticketTitle", ticket.getTitle());
		model.addAttribute("ticketDetails", ticket.getDetails());
		// On veut les pseudos des personnes qui bossent dessus (Task)
		List<Task> tasksLinkedWithTicket = taskService.getTasksByTicket(ticket);
		List<String> usersPseudoWorkingOnTicket = new ArrayList<String>();
		for (Task task : tasksLinkedWithTicket) {
			usersPseudoWorkingOnTicket.add(task.getUser().getPseudo());
		}
		model.addAttribute("usersPseudo", usersPseudoWorkingOnTicket);
		// On veut l'historique (Status, StatusHistory)
		List<StatusHistory> statusHistoricOfTicket = statusHistoryService.getStatusHistoriesFromThisTicket(ticket);
		model.addAttribute("history", statusHistoricOfTicket);
		return "ticket/details.html";
	}

}
