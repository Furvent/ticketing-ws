package fr.eql.ticketing.restController.dto.read;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class GroupDashboardData {
	private GroupData groupData;
	private List<TicketData> ticketsData;
	private List<CommentToDisplay> commentsToDisplay;

	public GroupDashboardData(GroupData groupData, List<TicketData> ticketsData, List<CommentToDisplay> commentsToDisplay) {
		this.groupData = groupData;
		this.ticketsData = ticketsData;
		this.commentsToDisplay = commentsToDisplay;
		
	}
}
