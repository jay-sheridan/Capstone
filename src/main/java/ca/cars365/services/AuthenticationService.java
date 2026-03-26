package ca.cars365.services;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import ca.cars365.beans.AuthenticationRequest;
import ca.cars365.beans.AuthenticationResponse;
import ca.cars365.dto.request.RegistrationRequest;
import ca.cars365.entity.Users;
import ca.cars365.enums.UserRole;
import ca.cars365.exception.AccountAlreadyExistsException;
import ca.cars365.exception.InvalidCredentialsException;
import ca.cars365.repositories.UserRepository;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class AuthenticationService {

	private UserRepository userRepository;
	private PasswordEncoder passwordEncoder;
	private JwtService jwtService;
	private AuthenticationManager authenticationManager;

	public AuthenticationResponse register(RegistrationRequest request) {

		if (userRepository.existsByEmail(request.getEmail()) || userRepository.existsByPhoneNumber(request.getPhone())) {

			throw new AccountAlreadyExistsException("Account already exists!");
		}

		var user = Users.builder().fullName(request.getFullName()).email(request.getEmail())
				.passwordHash(passwordEncoder.encode(request.getPassword())).role(UserRole.REGULAR_USER)
				.phoneNumber(request.getPhone()).build();

		userRepository.save(user);

		return AuthenticationResponse.builder().token(jwtService.generateToken(user)).build();
	}

	public AuthenticationResponse authenticate(AuthenticationRequest request) {

		try {
			authenticationManager
					.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
		} catch (BadCredentialsException ex) {
			throw new InvalidCredentialsException("Invalid email or password");
		}

		Users user = userRepository.findByEmail(request.getEmail()).orElseThrow();

		return AuthenticationResponse.builder().token(jwtService.generateToken(user)).build();
	}
}
