package fr.eql.ticketing.restController;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import fr.eql.ticketing.entity.Ticket;
import fr.eql.ticketing.restController.dto.read.PublicUser;
import fr.eql.ticketing.restController.dto.read.StatusData;
import fr.eql.ticketing.restController.dto.rest.TicketData;
import fr.eql.ticketing.service.TicketService;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(value = "/ticket", headers = "Accept=application/json")
public class TicketRestController {
	TicketService ticketService;

	public TicketRestController(TicketService ticketService) {
		this.ticketService = ticketService;
	}

	@GetMapping("")
	public ResponseEntity<?> getAllTickets() {
		try {
			List<Ticket> allTicketsEntities = this.ticketService.getAllTickets();
			List<TicketData> allTicketsData = allTicketsEntities.stream()
					.map(ticketEntity -> this.createTicketDataFromTicketEntity(ticketEntity))
					.collect(Collectors.toList());
			return new ResponseEntity<List<TicketData>>(allTicketsData, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	private TicketData createTicketDataFromTicketEntity(Ticket ticketEntity) {
		// Find ticket history
		List<StatusData> history = ticketEntity.getStatusHistory().stream()
				.map(activity -> new StatusData(activity.getStatus().getLabel(), activity.getStatusDate()))
				.collect(Collectors.toList());
		// Find users on task
		List<PublicUser> usersOnTask = ticketEntity.getTasks().stream().map(task -> task.getUser())
				.map(user -> new PublicUser(user.getId(), user.getPseudo())).collect(Collectors.toList());
		return new TicketData(ticketEntity, history, usersOnTask);
	}
}
