package fr.eql.ticketing.restController.dto.create;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class NewComment {

	private Long entityId;

	private String entityType;

	private String text, author;

}
