package fr.eql.ticketing.service;

import java.util.List;

import fr.eql.ticketing.entity.StatusHistory;
import fr.eql.ticketing.entity.Ticket;

public interface StatusHistoryService {
	public StatusHistory save(StatusHistory statusHistory);
	public List<StatusHistory> saveAll(List<StatusHistory> statusHistoryList);
	public List<StatusHistory> getAllStatusHistories();
	public StatusHistory getStatusHistoryById(Long logId);
	public void delete(StatusHistory statusHistory);
	public StatusHistory update(StatusHistory statusHistory);
	public List<StatusHistory> getStatusHistoriesFromThisTicket(Ticket ticket);
	public StatusHistory getLastStatusHistoryFromThisTicket(Ticket ticket);
}
