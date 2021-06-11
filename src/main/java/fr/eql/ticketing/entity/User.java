package fr.eql.ticketing.entity;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "UserApp")
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	private String login, password, pseudo;
	private LocalDateTime creationAccountDate;

	@OneToMany(mappedBy = "user")
	private Set<Task> tasks = new HashSet<Task>();

	@OneToMany(mappedBy = "user")
	private Set<Membership> memberships = new HashSet<Membership>();

	@OneToMany(mappedBy = "user")
	private Set<Comment> comment = new HashSet<Comment>();

	public User() {
	}

	public User(String login, String password, String pseudo, LocalDateTime creationAccountDate) {
		this.login = login;
		this.password = password;
		this.pseudo = pseudo;
		this.creationAccountDate = creationAccountDate;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((creationAccountDate == null) ? 0 : creationAccountDate.hashCode());
		result = prime * result + (int) (id ^ (id >>> 32));
		result = prime * result + ((login == null) ? 0 : login.hashCode());
		result = prime * result + ((password == null) ? 0 : password.hashCode());
		result = prime * result + ((pseudo == null) ? 0 : pseudo.hashCode());
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
		User other = (User) obj;
		if (creationAccountDate == null) {
			if (other.creationAccountDate != null)
				return false;
		} else if (!creationAccountDate.equals(other.creationAccountDate))
			return false;
		if (id != other.id)
			return false;
		if (login == null) {
			if (other.login != null)
				return false;
		} else if (!login.equals(other.login))
			return false;
		if (password == null) {
			if (other.password != null)
				return false;
		} else if (!password.equals(other.password))
			return false;
		if (pseudo == null) {
			if (other.pseudo != null)
				return false;
		} else if (!pseudo.equals(other.pseudo))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", login=" + login + ", password=" + password + ", pseudo=" + pseudo
				+ ", creationAccountDate=" + creationAccountDate + "]";
	}

}
