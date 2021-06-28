package fr.eql.ticketing.controller.rest.dto.update;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class UpdatedUser {
	/**
	 * TODO: rework field login if using spring security
	 */
	private String username;
	private String oldPassword, newPassword, newPseudo;
}
