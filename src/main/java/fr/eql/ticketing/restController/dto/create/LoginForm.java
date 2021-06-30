package fr.eql.ticketing.restController.dto.create;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class LoginForm {
	private String username, password;
}
