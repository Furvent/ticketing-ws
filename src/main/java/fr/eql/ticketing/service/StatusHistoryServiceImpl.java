package fr.eql.ticketing.service;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.springframework.stereotype.Service;

import fr.eql.ticketing.entity.StatusHistory;
import fr.eql.ticketing.entity.Ticket;
import fr.eql.ticketing.repository.StatusHistoryRepository;

@Service
public class StatusHistoryServiceImpl implements StatusHistoryService {
	StatusHistoryRepository repository;

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
	public List<StatusHistory> getAllStatusHistories() {
		return repository.findAll();
	}

	@Override
	public StatusHistory getStatusHistoryById(Long logId) {
		return repository.findById(logId).get();
	}

	@Override
	public void delete(StatusHistory statusHistory) {
		repository.delete(statusHistory);
	}

	@Override
	public StatusHistory update(StatusHistory statusHistory) {
		return repository.save(statusHistory);
	}
	
	@Override
	public List<StatusHistory> getStatusHistoriesFromThisTicket(Ticket ticket) {
		return repository.findByTicket(ticket);
	}

	@Override
	public StatusHistory getLastStatusHistoryFromThisTicket(Ticket ticket) {
		// On récupère tous les statuts du ticket, et on renvoit le dernier en date;
		List<StatusHistory> statusHistories = repository.findByTicket(ticket);
		if (statusHistories.size() > 0) {
			// On trie la liste pour avoir le dernier statusHistory
			Collections.sort(statusHistories, new Comparator<StatusHistory>() {
				public int compare(StatusHistory sh1, StatusHistory sh2) {
					if (sh1.getStatusDate() == null || sh2.getStatusDate() == null)
						return 0;
					return sh1.getStatusDate().compareTo(sh2.getStatusDate());
				}
			});
			return statusHistories.get(statusHistories.size() - 1);
		} else {
			return null;
		}
	}

}
