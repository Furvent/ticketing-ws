package fr.eql.ticketing.controller.rest.dto.read;

import java.time.LocalDateTime;

import fr.eql.ticketing.entity.User;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class PrivateUser {

	private long id;
	private String username, pseudo;
	private LocalDateTime creationAccountDate;

	public PrivateUser(User user) {
		this.id = user.getId();
		this.username = user.getUsername();
		this.pseudo = user.getPseudo();
		this.creationAccountDate = user.getCreationAccountDate();
	}

}
