package fr.eql.ticketing.restController.dto.read;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import fr.eql.ticketing.entity.Group;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
/**
 * All Data to display group's information. TODO: Create a factory to avoid
 * dependency with entity.
 * 
 * @author Furvent
 *
 */
public class GroupData {
	private long id, creatorId;
	private String name;
	private List<PublicUser> users = new ArrayList<PublicUser>();
	private LocalDateTime creationDateGroup;

	public GroupData(Group group, List<PublicUser> users) {
		this.id = group.getId();
		this.creatorId = group.getCreatedBy().getId();
		this.name = group.getName();
		this.creationDateGroup = group.getCreationDateGroup();
		this.users = users;
	}

}
