package fr.eql.ticketing.restController.dto.create;

import java.util.ArrayList;
import java.util.List;

import fr.eql.ticketing.restController.dto.read.PublicUser;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class NewTicket {
	private long groupId;
	private String title, description;
	private List<PublicUser> usersOnTask = new ArrayList<PublicUser>();
	
}
