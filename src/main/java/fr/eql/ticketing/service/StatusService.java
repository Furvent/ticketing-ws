package fr.eql.ticketing.service;

import java.util.List;

import fr.eql.ticketing.entity.Status;

public interface StatusService {
	public Status save(Status status);
	public List<Status> getAllStatus();
	public Status getStatusById(Long statusId);
	public void delete(Status status);
	public Status update(Status status);
	public Status getStatusByLabel(String label);
}
