package ca.cars365.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ca.cars365.beans.AuthenticationRequest;
import ca.cars365.beans.AuthenticationResponse;
import ca.cars365.dto.request.RegistrationRequest;
import ca.cars365.services.AuthenticationService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/v1/auth")
@AllArgsConstructor
public class AuthenticationController {

	private AuthenticationService authenticationService;
	
	@PostMapping(value = "/register", consumes = "application/json")
	public ResponseEntity<AuthenticationResponse> register(@Valid @RequestBody RegistrationRequest request) {
		return ResponseEntity.ok(authenticationService.register(request));
	}
	
	@PostMapping(value = "/authenticate", consumes = "application/json")
	public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request) {
		return ResponseEntity.ok(authenticationService.authenticate(request));
	}
}
