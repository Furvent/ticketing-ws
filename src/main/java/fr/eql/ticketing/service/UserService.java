package fr.eql.ticketing.service;

import java.util.List;

import fr.eql.ticketing.entity.User;

public interface UserService {
	public User save(User usertoAdd);

	public List<User> getAllUsers();
	
	public List<User> getMultipleUsersWithIds(List<Long> ids);

	public User getUserWithId(long idToSearch);
	
	public User getUserWithUsername(String username);
	
	public boolean checkIfUserExistWithThisUsername(String username);
	
	public boolean checkIfUserExistWithThisId(Long id);
	
	public User getUserWithUsernameAndPassword(String username, String password);
}
