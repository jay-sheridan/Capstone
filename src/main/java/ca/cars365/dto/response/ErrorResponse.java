package ca.cars365.dto.response;

import java.time.LocalDateTime;

import ca.cars365.beans.ErrorType;
import lombok.Data;

@Data
public class ErrorResponse {
	
	private String message;
	private int status;
	private LocalDateTime timestamp;
	private ErrorType errorType;

	public ErrorResponse(String message, int status, ErrorType errorType) {
		this.message = message;
		this.status = status;
		this.timestamp = LocalDateTime.now();
		this.errorType = errorType;
	}

}
