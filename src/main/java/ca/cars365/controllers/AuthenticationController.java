package ca.cars365.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ca.cars365.dto.request.InspectionAgencyLoginRequest;
import ca.cars365.dto.request.InspectionAgencyRegistrationRequest;
import ca.cars365.dto.request.UserLoginRequest;
import ca.cars365.dto.request.UserRegistrationRequest;
import ca.cars365.dto.response.AuthenticationResponse;
import ca.cars365.services.AuthenticationService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/v1/auth")
@AllArgsConstructor
public class AuthenticationController {

	private AuthenticationService authenticationService;
	
	@PostMapping(value = "/register", consumes = "application/json")
	public ResponseEntity<AuthenticationResponse> register(@Valid @RequestBody UserRegistrationRequest request) {
		return ResponseEntity.ok(authenticationService.register(request));
	}
	
	@PostMapping(value = "/authenticate", consumes = "application/json")
	public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody UserLoginRequest request) {
		return ResponseEntity.ok(authenticationService.authenticate(request));
	}
	
	@PostMapping(value = "/agency/register", consumes = "application/json")
	public ResponseEntity<AuthenticationResponse> registerAgency(@Valid @RequestBody InspectionAgencyRegistrationRequest request) {
		return ResponseEntity.ok(authenticationService.registerInspectionAgency(request));
	}
	
	@PostMapping(value = "/agency/authenticate", consumes = "application/json")
	public ResponseEntity<AuthenticationResponse> authenticateAgency(@RequestBody InspectionAgencyLoginRequest request) {
		return ResponseEntity.ok(authenticationService.authenticateInspectionAgency(request));
	}
	
	
}
