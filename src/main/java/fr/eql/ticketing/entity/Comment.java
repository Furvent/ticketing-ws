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

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Comment {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String text;
	private LocalDateTime createDate;

	@ManyToOne()
	private Comment parent;

	@OneToMany(fetch = FetchType.EAGER)
	private Set<Comment> children = new HashSet<Comment>();

	@ManyToOne()
	@JoinColumn(nullable = false)
	private User user;

	@ManyToOne()
	@JoinColumn(nullable = false)
	private Ticket ticket;

	public Comment() {
	}

	public Comment(String text, LocalDateTime createDate) {
		this.text = text;
		this.createDate = createDate;
	}

	public Comment(String text, LocalDateTime createDate, Comment parentComment) {
		this(text, createDate);
		this.parent = parentComment;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((createDate == null) ? 0 : createDate.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((text == null) ? 0 : text.hashCode());
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
		Comment other = (Comment) obj;
		if (createDate == null) {
			if (other.createDate != null)
				return false;
		} else if (!createDate.equals(other.createDate))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (text == null) {
			if (other.text != null)
				return false;
		} else if (!text.equals(other.text))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Comment [id=" + id + ", text=" + text + ", createDate=" + createDate + ", parent=" + parent
				+ ", number of children=" + children.size() + ", user=" + user.getPseudo() + ", ticket="
				+ ticket.getDetails() + "]";
	}

}
