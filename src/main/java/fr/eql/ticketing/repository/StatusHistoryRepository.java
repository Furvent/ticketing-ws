package fr.eql.ticketing.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import fr.eql.ticketing.entity.StatusHistory;
import fr.eql.ticketing.entity.Ticket;

public interface StatusHistoryRepository extends JpaRepository<StatusHistory, Long>{
	public List<StatusHistory> findByTicket(Ticket ticket);
}
