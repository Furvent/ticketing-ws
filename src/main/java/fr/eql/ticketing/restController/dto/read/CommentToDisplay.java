package fr.eql.ticketing.restController.dto.read;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class CommentToDisplay {
	private String text, author;
	private LocalDateTime creationDate;

	public CommentToDisplay(String text, String author, LocalDateTime date) {
		this.text = text;
		this.author = author;
		this.creationDate = date;
	}

	public CommentToDisplay() {
		super();
		// TODO Auto-generated constructor stub
	}
	

}
