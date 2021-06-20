package fr.eql.ticketing.service;

import java.util.List;

import fr.eql.ticketing.entity.Task;
import fr.eql.ticketing.entity.Ticket;
import fr.eql.ticketing.entity.User;

public interface TaskService {
	public Task save(Task task);
	public List<Task> saveAll(List<Task> tasks);
	public List<Task> getAllTasks();
	public Task getTaskById(Long taskId);
	public void delete(Task task);
	public Task update(Task task);
	public List<Task> getTasksByTicket(Ticket ticket);
	public List<Task> getTasksByUser(User user);
	public List<Task> getTaskByUserAndTicket(User user, Ticket ticket);
}
