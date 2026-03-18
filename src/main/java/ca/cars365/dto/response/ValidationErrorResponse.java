package ca.cars365.dto.response;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import ca.cars365.beans.ErrorType;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ValidationErrorResponse {

	
	private LocalDateTime timestamp;
    private int status;
    private ErrorType errorType;
    private String message;
    private List<String> missingRequiredFields;
    private Map<String, List<String>> fieldErrors;
    
}
