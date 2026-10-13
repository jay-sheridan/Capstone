package ca.cars365.exception;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import ca.cars365.dto.response.ErrorResponse;
import ca.cars365.dto.response.ValidationErrorResponse;
import ca.cars365.enums.ErrorType;

@RestControllerAdvice
public class GlobalExceptionHandler {
	@ExceptionHandler(org.springframework.web.server.ResponseStatusException.class)
	public ResponseEntity<Map<String, String>> handleStatus(org.springframework.web.server.ResponseStatusException ex) {
		return ResponseEntity.status(ex.getStatusCode()).body(Map.of("message", ex.getReason() == null ? "Request failed." : ex.getReason()));
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
		public ResponseEntity<ValidationErrorResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {
		
		Map<String, List<String>> fieldErrors = new LinkedHashMap<>();
        List<String> missingRequiredFields = new ArrayList<>();

        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            String fieldName = error.getField();
            String message = error.getDefaultMessage();

            fieldErrors.computeIfAbsent(fieldName, k -> new ArrayList<>()).add(message);

            if ("NotBlank".equals(error.getCode()) || "NotNull".equals(error.getCode())) {
                if (!missingRequiredFields.contains(fieldName)) {
                    missingRequiredFields.add(fieldName);
                }
            }
        }

        ValidationErrorResponse response = new ValidationErrorResponse(
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                ErrorType.VALIDATION_ERROR,
                "Request validation failed",
                missingRequiredFields,
                fieldErrors
        );

        return ResponseEntity.badRequest().body(response);
	}
	
	@ExceptionHandler(InvalidVinException.class)
    public ResponseEntity<Object> handleInvalidVin(InvalidVinException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ex.getMessage());
    }
	
	@ExceptionHandler(AccountAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleAccountExistsException(AccountAlreadyExistsException ex) {
        ErrorResponse error = new ErrorResponse(
                ex.getMessage(),
                HttpStatus.CONFLICT.value(),
                ErrorType.AccountAlreadyExistsException
        );
        return new ResponseEntity<>(error, HttpStatus.CONFLICT);
    }
	
	@ExceptionHandler(InspectionAgencyNotFoundException.class)
		public ResponseEntity<ErrorResponse> handleInspectionAgencyNotFound(InspectionAgencyNotFoundException ex) {
				ErrorResponse error = new ErrorResponse(
				ex.getMessage(),
				HttpStatus.NOT_FOUND.value(),
				ErrorType.InspectionAgencyNotFoundException
		);
		return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
	}

    // 🔴 Generic fallback (VERY IMPORTANT)
//    @ExceptionHandler(Exception.class)
//    public ResponseEntity<ErrorResponse> handleGeneralException(Exception ex) {
//    	System.out.println("Handling general exception: " + ex.getMessage() + " | Trace: " + ex.getStackTrace());
//        ErrorResponse error = new ErrorResponse(
//                "Something went wrong",
//                HttpStatus.INTERNAL_SERVER_ERROR.value(),
//                null
//        );
//
//        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
//    }
    

    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<ErrorResponse> handleInvalidCredentials(InvalidCredentialsException ex) {
        ErrorResponse error = new ErrorResponse(
                ex.getMessage(),
                HttpStatus.UNAUTHORIZED.value(),
                ErrorType.InvalidCredentialsException
        );

        return new ResponseEntity<>(error, HttpStatus.UNAUTHORIZED);
    }
    
}
