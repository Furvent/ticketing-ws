package fr.eql.ticketing.controller.rest.dto.read;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import fr.eql.ticketing.entity.User;
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
	private long userId;
	private String userUsername, userPseudo;
	private LocalDateTime userCreationAccountDate;
	private List<GroupData> groupsData = new ArrayList<GroupData>();

	// TODO: Add comments list
	public GeneralDashboardData(User user, List<GroupData> groupsData) {
		this.userId = user.getId();
		this.userUsername = user.getUsername();
		this.userPseudo = user.getPseudo();
		this.userCreationAccountDate = user.getCreationAccountDate();
		this.groupsData = groupsData;
	}

}
