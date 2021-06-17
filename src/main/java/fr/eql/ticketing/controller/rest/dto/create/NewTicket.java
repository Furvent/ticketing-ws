package fr.eql.ticketing.controller.rest.dto.create;

import java.util.ArrayList;
import java.util.List;

import fr.eql.ticketing.controller.rest.dto.read.PublicUser;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class NewTicket {
	private long groupId;
	private String title, description;
	private List<PublicUser> usersOnTas = new ArrayList<PublicUser>();
	
}
