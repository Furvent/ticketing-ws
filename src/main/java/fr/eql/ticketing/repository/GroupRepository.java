package fr.eql.ticketing.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import fr.eql.ticketing.entity.Group;

public interface GroupRepository extends JpaRepository<Group, Long>{

}
