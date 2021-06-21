package fr.eql.ticketing.service;

import fr.eql.ticketing.entity.Ticket;

public interface TicketService {
	public Ticket save(Ticket ticket);

	public Ticket getTicketById(Long ticketId);
	
	public void delete(Ticket ticket);
}
