package fr.eql.ticketing.service;

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
	public User getUserWithUsername(String username) {
		return repository.findByUsername(username);
	}

	@Override
	public boolean checkIfUserExistWithThisUsername(String username) {
		return repository.existsByUsername(username);
	}
	
	@Override
	public boolean checkIfUserExistWithThisId(Long id) {
		return repository.existsById(id);
	}

}
