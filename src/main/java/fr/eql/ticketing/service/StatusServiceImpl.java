package fr.eql.ticketing.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import fr.eql.ticketing.entity.Status;
import fr.eql.ticketing.repository.StatusRepository;

@Service
public class StatusServiceImpl implements StatusService{
	StatusRepository repository;
	
	public StatusServiceImpl(StatusRepository repository) {
		this.repository = repository;
	}
	
	public void setRepository(StatusRepository repository) {
		this.repository = repository;
	}

	@Override
	public Status save(Status status) {
		return repository.save(status);
	}

	@Override
	public List<Status> getAllStatus() {
		return repository.findAll();
	}

	@Override
	public Status getStatusById(Long statusId) {
		return repository.findById(statusId).get();
	}

	@Override
	public void delete(Status status) {
		repository.delete(status);
	}

	@Override
	public Status update(Status status) {
		return repository.save(status);
	}

	@Override
	public Status getStatusByLabel(String label) {
		Optional<Status> status = repository.findByLabel(label);
		return status.isPresent() ? status.get() : null;
	}
}
