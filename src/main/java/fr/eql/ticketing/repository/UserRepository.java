package fr.eql.ticketing.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import fr.eql.ticketing.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {

	public List<User> findByUsernameAndPassword(String username, String password);

	public User findByUsername(String username);
	
	public boolean existsByUsername(String username);

}
