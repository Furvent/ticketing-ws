package fr.eql.ticketing.restController.dto.read;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@ToString
public class PublicUser {
	private long id;
	private String pseudo;
	
}
