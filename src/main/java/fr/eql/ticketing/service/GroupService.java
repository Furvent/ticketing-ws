package fr.eql.ticketing.service;

import fr.eql.ticketing.entity.Group;

public interface GroupService {
	public Group save(Group group);

	public Group getGroupWithId(Long groupId);
	
	public Group getGroupWithPublicId(String publicId);

	public boolean checkIfGroupExistsWithThisId(long id);
}
