package fr.eql.ticketing.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import fr.eql.ticketing.entity.Status;


public interface StatusRepository extends JpaRepository<Status, Long>{
	public Status findByLabel(String label);
}
