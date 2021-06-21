package fr.eql.ticketing.service;

import java.util.List;
import java.util.Optional;

import fr.eql.ticketing.entity.Group;
import fr.eql.ticketing.entity.Membership;
import fr.eql.ticketing.entity.User;

public interface MembershipService {
	public Membership save(Membership membership);

	public List<Membership> getAllMemberships();

	public List<Membership> getMembershipsWithUser(User user);
	
	public Membership getMembershipWithUserAndGroup(User user, Group group);

	public Membership getMembershipById(Long membershipId);

	public void delete(Membership membership);

	public Membership update(Membership membership);

	public List<Membership> getMembershipsWithGroup(Group group);
}
