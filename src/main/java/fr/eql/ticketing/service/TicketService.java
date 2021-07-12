package fr.eql.ticketing.service;

import fr.eql.ticketing.entity.Ticket;

public interface TicketService {
	public Ticket save(Ticket ticket);

	public Ticket getTicketWithId(Long ticketId);
	
	public Ticket getTicketWithPublicId(String publicId);
	
	public void delete(Ticket ticket);
}
