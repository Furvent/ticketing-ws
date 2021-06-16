package fr.eql.ticketing.controller.rest.dto.create;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class NewUser {
	private String login, password, pseudo;
}
