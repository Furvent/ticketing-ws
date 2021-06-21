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
	private MembershipRepository repository;

	public MembershipServiceImpl(MembershipRepository repository) {
		this.repository = repository;
	}

	@Override
	public Membership save(Membership membership) {
		return repository.save(membership);
	}

	@Override
	public Membership getMembershipById(Long membershipId) {
		Optional<Membership> membership = repository.findById(membershipId);
		return membership.isPresent() ? membership.get() : null;
	}

	@Override
	public List<Membership> getMembershipsWithUser(User user) {
		return repository.findByUser(user);
		
	}

	@Override
	public Membership getMembershipWithUserAndGroup(User user, Group group) {
		Optional<Membership> membership = repository.findByUserAndGroup(user, group);
		return membership.isPresent() ? membership.get() : null;
	}

}
