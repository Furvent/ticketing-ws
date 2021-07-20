package fr.eql.ticketing.service;

import java.util.List;

import fr.eql.ticketing.entity.User;

public interface UserService {
	public User save(User usertoAdd);

	public User getUserWithId(long idToSearch);

	public User getUserWithUsername(String username);

	public User getUserWithUsernameAndPassword(String username, String password);

	public boolean checkIfUserExistWithThisUsername(String username);

	public boolean checkIfUserExistWithThisId(Long id);

	public List<User> getMultipleUsersWithIds(List<Long> ids);
	
	public List<User> getAllUsers();

}
