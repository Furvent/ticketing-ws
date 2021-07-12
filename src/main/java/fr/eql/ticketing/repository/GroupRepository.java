package fr.eql.ticketing.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import fr.eql.ticketing.entity.Group;

public interface GroupRepository extends JpaRepository<Group, Long>{
	public Optional<Group> findByPublicId(String publicId);
}
