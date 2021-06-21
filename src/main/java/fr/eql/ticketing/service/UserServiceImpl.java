package fr.eql.ticketing.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import fr.eql.ticketing.entity.User;
import fr.eql.ticketing.repository.UserRepository;

@Service
public class UserServiceImpl implements UserService {
	private UserRepository repository;

	public UserServiceImpl(UserRepository repository) {
		this.repository = repository;
	}

	@Override
	public User save(User usertoAdd) {
		return repository.save(usertoAdd);
	}

	@Override
	public User getUserWithId(long idToSearch) {
		Optional<User> user = repository.findById(idToSearch);
		return user.isPresent() ? user.get() : null;
	}
	
	@Override
	public User getUserWithUsername(String username) {
		Optional<User> user = repository.findByUsername(username);
		return user.isPresent() ? user.get() : null;
	}
	
	@Override
	public User getUserWithUsernameAndPassword(String username, String password) {
		Optional<User> user = repository.findByUsernameAndPassword(username, password);
		return user.isPresent() ? user.get() : null;
	}

	@Override
	public boolean checkIfUserExistWithThisUsername(String username) {
		return repository.existsByUsername(username);
	}
	
	@Override
	public boolean checkIfUserExistWithThisId(Long id) {
		return repository.existsById(id);
	}

	@Override
	public List<User> getMultipleUsersWithIds(List<Long> ids) {
		return repository.findAllById(ids);
	}

}
