package fr.eql.ticketing.controller.rest.dto.read;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class CommentsToGet {
	
	private Long entityId;
	private String entityType;
	
	public CommentsToGet() {
		super();
		// TODO Auto-generated constructor stub
	}
	public CommentsToGet(Long entityId, String entityType) {
		super();
		this.entityId = entityId;
		this.entityType = entityType;
	}
	
	

}
