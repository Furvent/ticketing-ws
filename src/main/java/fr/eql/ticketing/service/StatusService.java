package fr.eql.ticketing.service;

import fr.eql.ticketing.entity.Status;

public interface StatusService {
	public Status save(Status status);

	public Status getStatusById(Long statusId);

	public Status getStatusByLabel(String label);
}
