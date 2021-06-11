package fr.eql.ticketing.service;

import java.util.List;

import fr.eql.ticketing.entity.Group;

public interface GroupService {
	public Group save(Group group);
	public List<Group> getAllGroups();
	public Group getGroupById(Long groupId);
	public void delete(Group group);
	public Group update(Group group);
}
