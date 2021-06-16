package fr.eql.ticketing.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import fr.eql.ticketing.entity.User;
import fr.eql.ticketing.repository.UserRepository;

@Service
public class UserServiceImpl implements UserService {

	UserRepository repository;

	public UserServiceImpl(UserRepository repository) {
		this.repository = repository;
	}

	@Override
	public User save(User usertoAdd) {
		return repository.save(usertoAdd);
	}

	@Override
	public List<User> getAllUsers() {
		return repository.findAll();
	}

	@Override
	public User getUserWithId(long idToSearch) {
		return repository.findById(idToSearch).get();
	}
	
	@Override
	public User getUserWithLogin(String login) {
		return repository.findByLogin(login);
	}

	@Override
	public User getUserForConnection(String login, String password) {
		User returnedUser = null;
		List<User> users = new ArrayList<User>();
		users = repository.findByLoginAndPassword(login, password);
		System.out.println(users.size());
		if (users.size() > 0) {
			returnedUser = users.iterator().next();
			return returnedUser;
		}
		return returnedUser;
	}

	@Override
	public boolean checkIfUserExistWithThisLogin(String login) {
		return repository.existsByLogin(login);
	}
	
	@Override
	public boolean checkIfUserExistWithThisId(Long id) {
		return repository.existsById(id);
	}

}
