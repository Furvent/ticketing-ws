package fr.eql.ticketing.exception.restController;

public class InvalidNewDataPostException extends RuntimeException {

	public InvalidNewDataPostException() {
		super();
	}

	public InvalidNewDataPostException(String message) {
		super(message);
	}

}
