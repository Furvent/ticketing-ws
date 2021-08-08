package fr.eql.ticketing.service;

import java.util.List;

import fr.eql.ticketing.entity.Ticket;
import fr.eql.ticketing.restController.dto.create.NewTicket;
import fr.eql.ticketing.restController.dto.update.UpdatedTicket;

public interface TicketService {
	public Ticket save(Ticket ticket);
	
	public Ticket create(NewTicket newTicket);

	public Ticket getTicketById(Long ticketId);
	
	public Ticket update(UpdatedTicket updatedTicket, String ticketId);
	
	public void delete(Long ticketId);
	
	public List<Ticket> getAllTickets();
}
