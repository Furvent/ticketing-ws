package fr.eql.ticketing.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import fr.eql.ticketing.entity.Status;


public interface StatusRepository extends JpaRepository<Status, Long>{
	public Optional<Status> findByLabel(String label);
}
