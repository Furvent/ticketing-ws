package fr.eql.ticketing.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import fr.eql.ticketing.entity.Group;
import fr.eql.ticketing.repository.GroupRepository;

@Service
public class GroupServiceImpl implements GroupService {
	private GroupRepository repository;

	public GroupServiceImpl(GroupRepository repository) {
		this.repository = repository;
	}

	@Override
	public Group save(Group groupToAdd) {
		return repository.save(groupToAdd);
	}

	@Override
	public Group getGroupById(Long groupId) {
		Optional<Group> group = repository.findById(groupId);
		return group.isPresent() ? group.get() : null;
	}

	@Override
	public boolean checkIfGroupExistWithThisId(long id) {
		return repository.existsById(id);
	}
}
