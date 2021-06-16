package fr.eql.ticketing.controller.rest.dto.create;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class LoginForm {
	private String login, password;
}
