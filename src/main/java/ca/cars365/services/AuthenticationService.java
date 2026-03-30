package ca.cars365.services;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import ca.cars365.dto.request.InspectionAgencyLoginRequest;
import ca.cars365.dto.request.InspectionAgencyRegistrationRequest;
import ca.cars365.dto.request.UserLoginRequest;
import ca.cars365.dto.request.UserRegistrationRequest;
import ca.cars365.dto.response.AuthenticationResponse;
import ca.cars365.entity.InspectionAgency;
import ca.cars365.entity.Users;
import ca.cars365.enums.UserRole;
import ca.cars365.exception.AccountAlreadyExistsException;
import ca.cars365.exception.InvalidCredentialsException;
import ca.cars365.repositories.InspectionAgencyRepository;
import ca.cars365.repositories.UserRepository;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class AuthenticationService {

	private UserRepository userRepository;
	private InspectionAgencyRepository inspectionAgencyRepository;
	private PasswordEncoder passwordEncoder;
	private JwtService jwtService;
	private AuthenticationManager authenticationManager;

	public AuthenticationResponse register(UserRegistrationRequest request) {

		if (userRepository.existsByEmail(request.getEmail()) || userRepository.existsByPhoneNumber(request.getPhone())) {

			throw new AccountAlreadyExistsException("Account already exists!");
		}

		Users user = Users.builder().fullName(request.getFullName()).email(request.getEmail())
				.passwordHash(passwordEncoder.encode(request.getPassword())).role(UserRole.REGULAR_USER)
				.phoneNumber(request.getPhone()).build();

		userRepository.save(user);

		return AuthenticationResponse.builder().token(jwtService.generateToken(user)).build();
	}

	public AuthenticationResponse authenticate(UserLoginRequest request) {

		Users user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new InvalidCredentialsException("Invalid email or password"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPasswordHash())) {
            throw new InvalidCredentialsException("Invalid email or password");
        }

		return AuthenticationResponse.builder().token(jwtService.generateToken(user)).build();
	}
	
	public AuthenticationResponse registerInspectionAgency(InspectionAgencyRegistrationRequest request) {
		
		if (inspectionAgencyRepository.existsByLicenseNumber(request.getLicenseNumber())) {
			throw new AccountAlreadyExistsException("Inspection agency with this license number already exists!");
		}
		
		InspectionAgency agency = InspectionAgency.builder()
				.name(request.getName())
				.address(request.getAddress())
				.city(request.getCity())
				.provinceState(request.getProvinceState())
				.postalCode(request.getPostalCode())
				.country(request.getCountry())
				.licenseNumber(request.getLicenseNumber())
				.passwordHash(passwordEncoder.encode(request.getPassword()))
				.email(request.getEmail())
				.phoneNumber(request.getPhoneNumber())
				.build();
		
		inspectionAgencyRepository.save(agency);
		
		return AuthenticationResponse.builder().token(jwtService.generateToken(agency)).build();
	}
	
	public AuthenticationResponse authenticateInspectionAgency(InspectionAgencyLoginRequest request) {
		
		InspectionAgency agency = inspectionAgencyRepository.findByLicenseNumber(request.getLicenseNumber())
                .orElseThrow(() -> new InvalidCredentialsException("Invalid license number or password"));

        if (!passwordEncoder.matches(request.getPassword(), agency.getPasswordHash())) {
            throw new InvalidCredentialsException("Invalid license number or password");
        }
		
		
		return AuthenticationResponse.builder().token(jwtService.generateToken(agency)).build();
	} 
}
