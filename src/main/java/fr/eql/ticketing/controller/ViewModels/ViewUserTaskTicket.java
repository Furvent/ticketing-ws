package fr.eql.ticketing.controller.ViewModels;

import java.util.List;

import fr.eql.ticketing.entity.Ticket;

public class ViewUserTaskTicket {

	private String details, title, lastStatus;
	private long id;
	private List<String> usersTaskOn;

	public ViewUserTaskTicket(Ticket ticket, String lastStatus, List<String> usersTaskOn) {
		this.details = ticket.getDetails();
		this.title = ticket.getTitle();
		this.id = ticket.getId();
		this.lastStatus = lastStatus;
		this.usersTaskOn = usersTaskOn;
	}

	public String getDetails() {
		return details;
	}

	public void setDetails(String details) {
		this.details = details;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getLastStatus() {
		return lastStatus;
	}

	public void setLastStatus(String lastStatus) {
		this.lastStatus = lastStatus;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public List<String> getUsersTaskOn() {
		return usersTaskOn;
	}

	public void setUsersTaskOn(List<String> usersTaskOn) {
		this.usersTaskOn = usersTaskOn;
	}

	@Override
	public String toString() {
		return "ViewUserTaskTicket [details=" + details + ", title=" + title + ", lastStatus=" + lastStatus + ", id="
				+ id + ", usersTaskOn=" + usersTaskOn + "]";
	}

}
