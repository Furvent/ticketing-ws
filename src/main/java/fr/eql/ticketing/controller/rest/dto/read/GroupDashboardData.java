package fr.eql.ticketing.controller.rest.dto.read;

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

	public GroupDashboardData(GroupData groupData, List<TicketData> ticketsData) {
		this.groupData = groupData;
		this.ticketsData = ticketsData;
	}
}
