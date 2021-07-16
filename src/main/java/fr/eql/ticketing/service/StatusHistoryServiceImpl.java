package fr.eql.ticketing.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import fr.eql.ticketing.entity.StatusHistory;
import fr.eql.ticketing.entity.Ticket;
import fr.eql.ticketing.repository.StatusHistoryRepository;

@Service
public class StatusHistoryServiceImpl implements StatusHistoryService {
	private StatusHistoryRepository repository;

	public StatusHistoryServiceImpl(StatusHistoryRepository repository) {
		this.repository = repository;
	}

	@Override
	public StatusHistory save(StatusHistory statusHistory) {
		return repository.save(statusHistory);
	}
	
	@Override
	public List<StatusHistory> saveAll(List<StatusHistory> statusHistoryList) {
		return repository.saveAll(statusHistoryList);
	}

	@Override
	public StatusHistory getStatusHistoryById(Long logId) {
		Optional<StatusHistory> statusHistory = repository.findById(logId);
		return statusHistory.isPresent() ? statusHistory.get() : null;
	}

	@Override
	public List<StatusHistory> getAllStatusHistoryByTicket(Ticket ticket) {
		return repository.findByTicket(ticket);
	}

}
