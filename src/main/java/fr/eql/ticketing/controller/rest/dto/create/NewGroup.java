package fr.eql.ticketing.controller.rest.dto.create;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString()
public class NewGroup {
	private String name;
	private long creatorId;
}
