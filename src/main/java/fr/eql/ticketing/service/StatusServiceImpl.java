package fr.eql.ticketing.service;

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

	@Override
	public Status save(Status status) {
		return repository.save(status);
	}

	@Override
	public Status getStatusById(Long statusId) {
		Optional<Status> status = repository.findById(statusId);
		return status.isPresent() ? status.get() : null;
	}

	@Override
	public Status getStatusByLabel(String label) {
		Optional<Status> status = repository.findByLabel(label);
		return status.isPresent() ? status.get() : null;
	}
}
