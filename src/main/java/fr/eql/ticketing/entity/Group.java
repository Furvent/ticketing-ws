package fr.eql.ticketing.entity;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "UsersGroup")
public class Group {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String name;
	private LocalDateTime creationDateGroup;

	@ManyToOne
	@JoinColumn(nullable = false)
	private User createdBy;

	@OneToMany(mappedBy = "group", fetch = FetchType.EAGER)
	private Set<Membership> memberships = new HashSet<Membership>();

	@OneToMany(mappedBy = "group", fetch = FetchType.EAGER)
	private Set<Ticket> tickets = new HashSet<Ticket>();

	public Group() {
	}

	public Group(String name, User createdBy, LocalDateTime creationDateGroup) {
		this.name = name;
		this.createdBy = createdBy;
		this.creationDateGroup = creationDateGroup;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public User getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(User createdBy) {
		this.createdBy = createdBy;
	}

	public LocalDateTime getCreationDateGroup() {
		return creationDateGroup;
	}

	public void setCreationDateGroup(LocalDateTime creationDateGroup) {
		this.creationDateGroup = creationDateGroup;
	}

	public Set<Membership> getMemberships() {
		return memberships;
	}

	public void setMemberships(Set<Membership> memberships) {
		this.memberships = memberships;
	}

	public Set<Ticket> getTickets() {
		return tickets;
	}

	public void setTickets(Set<Ticket> tickets) {
		this.tickets = tickets;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((createdBy == null) ? 0 : createdBy.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Group other = (Group) obj;
		if (createdBy == null) {
			if (other.createdBy != null)
				return false;
		} else if (!createdBy.equals(other.createdBy))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Group [id=" + id + ", name=" + name + ", createdBy=" + createdBy + ", creationDateGroup="
				+ creationDateGroup + ", memberships=" + memberships + "]";
	}

}
