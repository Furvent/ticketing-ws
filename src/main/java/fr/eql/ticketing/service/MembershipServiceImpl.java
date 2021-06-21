package fr.eql.ticketing.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import fr.eql.ticketing.entity.Group;
import fr.eql.ticketing.entity.Membership;
import fr.eql.ticketing.entity.User;
import fr.eql.ticketing.repository.MembershipRepository;

@Service
public class MembershipServiceImpl implements MembershipService {
	MembershipRepository repository;

	public MembershipServiceImpl(MembershipRepository repository) {
		this.repository = repository;
	}

	public void setRepository(MembershipRepository repository) {
		this.repository = repository;
	}

	@Override
	public Membership save(Membership membership) {
		return repository.save(membership);
	}

	@Override
	public List<Membership> getAllMemberships() {
		return repository.findAll();
	}

	@Override
	public Membership getMembershipById(Long membershipId) {
		return repository.findById(membershipId).get();
	}

	@Override
	public void delete(Membership membership) {
		repository.delete(membership);
	}

	@Override
	public Membership update(Membership membership) {
		return repository.save(membership);
	}

	@Override
	public List<Membership> getMembershipsWithUser(User user) {
		return repository.findByUser(user);
		
	}

	@Override
	public List<Membership> getMembershipsWithGroup(Group group) {
		return repository.findByGroup(group);
	}

	@Override
	public Membership getMembershipWithUserAndGroup(User user, Group group) {
		Optional<Membership> membership = repository.findByUserAndGroup(user, group);
		return membership.isPresent() ? membership.get() : null;
	}

}
