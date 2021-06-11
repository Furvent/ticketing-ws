package fr.eql.ticketing.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import fr.eql.ticketing.entity.Group;
import fr.eql.ticketing.entity.Membership;
import fr.eql.ticketing.entity.User;

public interface MembershipRepository extends JpaRepository<Membership, Long>{
	public List<Membership> findByUser(User user);
	public List<Membership> findByGroup(Group group);
}
