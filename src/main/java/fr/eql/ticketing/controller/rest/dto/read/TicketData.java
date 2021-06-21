package fr.eql.ticketing.controller.rest.dto.read;

import java.util.List;

import fr.eql.ticketing.entity.Ticket;
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
