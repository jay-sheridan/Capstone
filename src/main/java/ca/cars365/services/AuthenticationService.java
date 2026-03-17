package ca.cars365.services;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import ca.cars365.beans.AuthenticationRequest;
import ca.cars365.beans.AuthenticationResponse;
import ca.cars365.entity.Role;
import ca.cars365.entity.Status;
import ca.cars365.entity.Users;
import ca.cars365.repositories.UserRepository;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class AuthenticationService {

	
	private UserRepository userRepository;
	private PasswordEncoder passwordEncoder;
	private JwtService jwtService;
	private AuthenticationManager authenticationManager;
	
	public AuthenticationResponse register(AuthenticationRequest request) {
		var user = Users.builder()
				.fullName(request.getFullName())
				.email(request.getEmail())
				.password(passwordEncoder.encode(request.getPassword()))
				.role(Role.USER)
				.phone(request.getPhone())
				.status(Status.ACTIVE)
				.build();
		
		userRepository.save(user);
		
		
		
		return AuthenticationResponse.builder()
				.token(jwtService.generateToken(user))
				.build();
	}
	
	public AuthenticationResponse authenticate (AuthenticationRequest request) {
		
		authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(
						request.getEmail(),
						request.getPassword()
				)
				);
		
		Users user = userRepository.findByEmail(request.getEmail())
				.orElseThrow();
		
		return AuthenticationResponse.builder()
				.token(jwtService.generateToken(user))
				.build();
	}
}
