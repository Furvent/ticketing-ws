package fr.eql.ticketing.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import fr.eql.ticketing.entity.Group;
import fr.eql.ticketing.entity.Ticket;

public interface TicketRepository extends JpaRepository<Ticket, Long>{
	public List<Ticket> findByGroup(Group group);
}
