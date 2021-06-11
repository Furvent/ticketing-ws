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

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPseudo() {
		return pseudo;
	}

	public void setPseudo(String pseudo) {
		this.pseudo = pseudo;
	}

	public LocalDateTime getCreationAccountDate() {
		return creationAccountDate;
	}

	public void setCreationAccountDate(LocalDateTime creationAccountDate) {
		this.creationAccountDate = creationAccountDate;
	}

	public Set<Membership> getMemberships() {
		return memberships;
	}

	public void setMemberships(Set<Membership> memberships) {
		this.memberships = memberships;
	}

	public Set<Task> getTasks() {
		return tasks;
	}

	public void setTasks(Set<Task> tasks) {
		this.tasks = tasks;
	}

	public Set<Comment> getComment() {
		return comment;
	}

	public void setComment(Set<Comment> comment) {
		this.comment = comment;
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
