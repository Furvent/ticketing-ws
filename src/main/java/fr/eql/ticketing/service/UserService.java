package fr.eql.ticketing.service;

import java.util.List;
import java.util.Set;

import fr.eql.ticketing.entity.Group;
import fr.eql.ticketing.entity.User;

public interface UserService {
	public User save(User usertoAdd);

	public List<User> getAllUsers();

	public User getUserWithId(long idToSearch);
	public User getUserForConnection(String login, String password);
}
