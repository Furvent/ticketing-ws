package fr.eql.ticketing.enums;

public final class TicketStatus {
	public static final String OPENED = "Opened";
	public static final String ALLOCATED = "Allocated";
	public static final String DONE = "Done";
	public static final String CLOSED = "Closed";
	public static final String CANCELED = "Canceled";

	public static boolean isValid(String status) {
		return status.equals(TicketStatus.OPENED) || status.equals(TicketStatus.ALLOCATED)
				|| status.equals(TicketStatus.DONE) || status.equals(TicketStatus.CLOSED)
				|| status.equals(TicketStatus.CANCELED);
	}
}
