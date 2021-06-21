package fr.eql.ticketing.service;

import java.util.List;

import fr.eql.ticketing.entity.Group;
import fr.eql.ticketing.entity.Ticket;

public interface TicketService {
	public Ticket save(Ticket ticket);

	public List<Ticket> getAllTickets();

	public List<Ticket> getTicketsWithGroup(Group group);

	public Ticket getTicketById(Long ticketId);

	public void delete(Ticket ticket);

	public Ticket update(Ticket ticket);
}
