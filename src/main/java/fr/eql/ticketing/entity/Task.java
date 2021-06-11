package fr.eql.ticketing.entity;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class Task {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private LocalDateTime userAddedDate;
	private LocalDateTime userWithdrawalDate;

	@ManyToOne
	@JoinColumn(nullable = false)
	private User user;

	@ManyToOne
	@JoinColumn(nullable = false)
	private Ticket ticket;

	public Task() {
	}

	public Task(User user, Ticket ticket, LocalDateTime userAddedDate) {
		this.user = user;
		this.ticket = ticket;
		this.userAddedDate = userAddedDate;
	}
	
	public Task(LocalDateTime userAddedDate, User user) {
		super();
		this.userAddedDate = userAddedDate;
		this.user = user;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public LocalDateTime getUserAddedDate() {
		return userAddedDate;
	}

	public void setUserAddedDate(LocalDateTime userAddedDate) {
		this.userAddedDate = userAddedDate;
	}

	public LocalDateTime getUserWithdrawalDate() {
		return userWithdrawalDate;
	}

	public void setUserWithdrawalDate(LocalDateTime userWithdrawalDate) {
		this.userWithdrawalDate = userWithdrawalDate;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Ticket getTicket() {
		return ticket;
	}

	public void setTicket(Ticket ticket) {
		this.ticket = ticket;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((userAddedDate == null) ? 0 : userAddedDate.hashCode());
		result = prime * result + ((userWithdrawalDate == null) ? 0 : userWithdrawalDate.hashCode());
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
		Task other = (Task) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (userAddedDate == null) {
			if (other.userAddedDate != null)
				return false;
		} else if (!userAddedDate.equals(other.userAddedDate))
			return false;
		if (userWithdrawalDate == null) {
			if (other.userWithdrawalDate != null)
				return false;
		} else if (!userWithdrawalDate.equals(other.userWithdrawalDate))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Task [id=" + id + ", userAddedDate=" + userAddedDate + ", userWithdrawalDate=" + userWithdrawalDate
				+ "]";
	}

}
