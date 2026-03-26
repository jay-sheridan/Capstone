package ca.cars365.exception;

public class DealerNotFoundException extends RuntimeException {

	public DealerNotFoundException(String message) {
        super(message);
    }
}
