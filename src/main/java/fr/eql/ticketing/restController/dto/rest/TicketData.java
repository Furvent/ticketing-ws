package fr.eql.ticketing.restController.dto.rest;

import java.util.List;

import fr.eql.ticketing.entity.Ticket;
import fr.eql.ticketing.restController.dto.read.PublicUser;
import fr.eql.ticketing.restController.dto.read.StatusData;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class TicketData {
	private long id;
	private String title, description;
	private List<StatusData> history;
	private List<PublicUser> usersOnTask;

	public TicketData(Ticket ticket, List<StatusData> history, List<PublicUser> usersOnTask) {
		this.id = ticket.getId();
		this.title = ticket.getTitle();
		this.description = ticket.getDescription();
		this.history = history;
		this.usersOnTask = usersOnTask;
	}

}
