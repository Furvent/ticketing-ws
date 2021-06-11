package fr.eql.ticketing.entity;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Ticket {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String title;

	@Column(length = 3000)
	private String details;

	@OneToMany(mappedBy = "ticket")
	private Set<StatusHistory> statusHistory = new HashSet<StatusHistory>();

	@OneToMany(mappedBy = "ticket")
	private Set<Task> tasks = new HashSet<Task>();

	@OneToMany(mappedBy = "ticket")
	private Set<Comment> comment = new HashSet<Comment>();

	@ManyToOne
	@JoinColumn(nullable = false)
	private Group group;

	public Ticket() {
	}

	public Ticket(String details, String title) {
		this.details = details;
		this.title = title;
	}

	public Ticket(String details, String title, Group group) {
		this(details, title);
		this.group = group;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((details == null) ? 0 : details.hashCode());
		result = prime * result + ((group == null) ? 0 : group.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((title == null) ? 0 : title.hashCode());
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
		Ticket other = (Ticket) obj;
		if (details == null) {
			if (other.details != null)
				return false;
		} else if (!details.equals(other.details))
			return false;
		if (group == null) {
			if (other.group != null)
				return false;
		} else if (!group.equals(other.group))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (title == null) {
			if (other.title != null)
				return false;
		} else if (!title.equals(other.title))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Ticket [id=" + id + ", details=" + details + ", title=" + title + ", groupiD=" + group.getId() + "]";
	}

}
