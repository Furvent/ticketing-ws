package fr.eql.ticketing.service;

import java.util.List;

import fr.eql.ticketing.entity.StatusHistory;
import fr.eql.ticketing.entity.Ticket;

public interface StatusHistoryService {
	public StatusHistory save(StatusHistory statusHistory);

	public List<StatusHistory> saveAll(List<StatusHistory> statusHistoryList);

	public StatusHistory getStatusHistoryById(Long logId);
	
	public List<StatusHistory> getAllStatusHistoryByTicket(Ticket ticket);

}
