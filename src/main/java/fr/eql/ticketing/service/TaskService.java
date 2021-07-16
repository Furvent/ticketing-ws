package fr.eql.ticketing.service;

import java.util.List;

import fr.eql.ticketing.entity.Task;
import fr.eql.ticketing.entity.Ticket;

public interface TaskService {
	public Task save(Task task);

	public List<Task> saveAll(List<Task> tasks);

	public Task getTaskById(Long taskId);
	
	public List<Task> getAllTaskByTicket(Ticket ticket);

}
