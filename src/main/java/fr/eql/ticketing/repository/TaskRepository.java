package fr.eql.ticketing.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import fr.eql.ticketing.entity.Task;
import fr.eql.ticketing.entity.Ticket;
import fr.eql.ticketing.entity.User;

public interface TaskRepository extends JpaRepository<Task, Long> {
	public List<Task> findByTicket(Ticket ticket);

	public List<Task> findByUser(User user);

	public List<Task> findByUserAndTicket(User user, Ticket ticket);
}
