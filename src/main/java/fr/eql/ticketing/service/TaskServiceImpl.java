package fr.eql.ticketing.service;

import java.util.List;

import org.springframework.stereotype.Service;

import fr.eql.ticketing.entity.Task;
import fr.eql.ticketing.entity.Ticket;
import fr.eql.ticketing.entity.User;
import fr.eql.ticketing.repository.TaskRepository;

@Service
public class TaskServiceImpl implements TaskService {
	TaskRepository repository;

	public TaskServiceImpl(TaskRepository repository) {
		this.repository = repository;
	}

	public void setRepository(TaskRepository repository) {
		this.repository = repository;
	}

	@Override
	public Task save(Task task) {
		return repository.save(task);
	}
	
	@Override
	public List<Task> saveAll(List<Task> tasks) {
		return repository.saveAll(tasks);
	}

	@Override
	public List<Task> getAllTasks() {
		return repository.findAll();
	}

	@Override
	public Task getTaskById(Long taskId) {
		return repository.findById(taskId).get();
	}

	@Override
	public void delete(Task task) {
		repository.delete(task);
	}

	@Override
	public Task update(Task task) {
		return repository.save(task);
	}

	@Override
	public List<Task> getTasksByTicket(Ticket ticket) {
		return repository.findByTicket(ticket);
	}

	@Override
	public List<Task> getTasksByUser(User user) {
		return repository.findByUser(user);
	}

	@Override
	public List<Task> getTaskByUserAndTicket(User user, Ticket ticket) {
		return repository.findByUserAndTicket(user, ticket);
	}

}
