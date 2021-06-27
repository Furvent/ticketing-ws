package fr.eql.ticketing.enums;
public enum EntityType {
	GROUP,
	TICKET;
	
	
	public boolean isValid(String entityType) {
		if(entityType == EntityType.GROUP.name() || entityType == EntityType.TICKET.name()) {
			return true;
		} else {
			return false;
		}
		
	}
}
