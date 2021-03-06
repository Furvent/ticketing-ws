package fr.eql.ticketing.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import fr.eql.ticketing.entity.Ticket;
import fr.eql.ticketing.repository.TicketRepository;

@Service
public class TicketServiceImpl implements TicketService {
	private TicketRepository repository;

	public TicketServiceImpl(TicketRepository repository) {
		this.repository = repository;
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
	public void delete(Ticket ticket) {
		repository.delete(ticket);
	}

}
