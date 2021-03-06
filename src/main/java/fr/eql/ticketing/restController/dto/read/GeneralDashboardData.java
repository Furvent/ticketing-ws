package fr.eql.ticketing.restController.dto.read;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
/**
 * All data send to client to displays general dashboard data. TODO: Create a
 * factory class to separate dependency between dto and entity classes
 * 
 * @author Furvent
 *
 */
public class GeneralDashboardData {
	private List<GroupData> groupsData = new ArrayList<GroupData>();

	// TODO: Add comments list
	public GeneralDashboardData(List<GroupData> groupsData) {
		this.groupsData = groupsData;
	}

}
