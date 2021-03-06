package fr.eql.ticketing.restController.dto.update;

import java.util.ArrayList;
import java.util.List;

import fr.eql.ticketing.restController.dto.read.PublicUser;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class UpdatedTicket {
	private long ticketId;
	private String newTitle, newDescription, newStatus;
	private List<PublicUser> usersOnTask = new ArrayList<PublicUser>();
	
}
