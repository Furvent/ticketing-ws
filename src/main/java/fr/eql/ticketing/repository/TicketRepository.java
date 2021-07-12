package fr.eql.ticketing.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import fr.eql.ticketing.entity.Group;
import fr.eql.ticketing.entity.Ticket;

public interface TicketRepository extends JpaRepository<Ticket, Long>{
	public Optional<Ticket> findByPublicId(String publicId);
	
	public List<Ticket> findByGroup(Group group);
}
