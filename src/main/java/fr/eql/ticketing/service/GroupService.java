package fr.eql.ticketing.service;

import fr.eql.ticketing.entity.Group;

public interface GroupService {
	public Group save(Group group);

	public Group getGroupById(Long groupId);

	public boolean checkIfGroupExistWithThisId(long id);
}
