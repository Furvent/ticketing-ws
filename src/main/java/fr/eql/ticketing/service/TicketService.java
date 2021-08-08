package fr.eql.ticketing.service;

import java.util.List;

import fr.eql.ticketing.entity.Ticket;

public interface TicketService {
	public Ticket save(Ticket ticket);

	public Ticket getTicketById(Long ticketId);
	
	public List<Ticket> getAllTickets();
	
	public void delete(Ticket ticket);
}
