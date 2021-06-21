package fr.eql.ticketing.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import fr.eql.ticketing.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
	public Optional<User> findByUsername(String username);
	
	public Optional<User> findByUsernameAndPassword(String username, String password);
	
	public boolean existsByUsername(String username);

}
