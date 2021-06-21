package fr.eql.ticketing.service;

import java.util.List;

import fr.eql.ticketing.entity.StatusHistory;

public interface StatusHistoryService {
	public StatusHistory save(StatusHistory statusHistory);

	public List<StatusHistory> saveAll(List<StatusHistory> statusHistoryList);

	public StatusHistory getStatusHistoryById(Long logId);

}
