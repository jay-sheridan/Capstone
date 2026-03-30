package ca.cars365.exception;

public class InspectionAgencyNotFoundException extends RuntimeException {

	public InspectionAgencyNotFoundException(String message) {
        super(message);
    }
}
