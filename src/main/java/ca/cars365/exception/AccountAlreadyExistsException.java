package ca.cars365.exception;

public class AccountAlreadyExistsException extends RuntimeException {

	public AccountAlreadyExistsException(String message) {
		super(message);
	}

}
