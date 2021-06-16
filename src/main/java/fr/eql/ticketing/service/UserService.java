package fr.eql.ticketing.service;

import java.util.List;

import fr.eql.ticketing.entity.User;

public interface UserService {
	public User save(User usertoAdd);

	public List<User> getAllUsers();

	public User getUserWithId(long idToSearch);
	
	public User getUserWithLogin(String login);
	
	public boolean checkIfUserExistWithThisLogin(String login);
	
	public boolean checkIfUserExistWithThisId(Long id);

	public User getUserForConnection(String login, String password);
}
