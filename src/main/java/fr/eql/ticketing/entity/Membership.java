package fr.eql.ticketing.entity;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class Membership {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private LocalDateTime joinDate;
	private LocalDateTime withDrawalDate;

	@ManyToOne
	@JoinColumn(nullable = false)
	private User user;

	@ManyToOne
	@JoinColumn(nullable = false)
	private Group group;

	public Membership() {
	}

	public Membership(User user, Group group, LocalDateTime joinDate) {
		this.user = user;
		this.group = group;
		this.joinDate = joinDate;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public LocalDateTime getJoinDate() {
		return joinDate;
	}

	public void setJoinDate(LocalDateTime joinDate) {
		this.joinDate = joinDate;
	}

	public LocalDateTime getWithDrawalDate() {
		return withDrawalDate;
	}

	public void setWithDrawalDate(LocalDateTime withDrawalDate) {
		this.withDrawalDate = withDrawalDate;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Group getGroup() {
		return group;
	}

	public void setGroup(Group group) {
		this.group = group;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((joinDate == null) ? 0 : joinDate.hashCode());
		result = prime * result + ((withDrawalDate == null) ? 0 : withDrawalDate.hashCode());
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
		Membership other = (Membership) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (joinDate == null) {
			if (other.joinDate != null)
				return false;
		} else if (!joinDate.equals(other.joinDate))
			return false;
		if (withDrawalDate == null) {
			if (other.withDrawalDate != null)
				return false;
		} else if (!withDrawalDate.equals(other.withDrawalDate))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Membership [id=" + id + ", joinDate=" + joinDate + ", withDrawalDate=" + withDrawalDate + "]";
	}

}
