package fr.eql.ticketing.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import fr.eql.ticketing.entity.Task;
import fr.eql.ticketing.repository.TaskRepository;

@Service
public class TaskServiceImpl implements TaskService {
	private TaskRepository repository;

	public TaskServiceImpl(TaskRepository repository) {
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
	public Task getTaskById(Long taskId) {
		Optional<Task> task = repository.findById(taskId);
		return task.isPresent() ? task.get() : null;
	}

}
