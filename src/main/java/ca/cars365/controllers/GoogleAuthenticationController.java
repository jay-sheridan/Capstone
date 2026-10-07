package ca.cars365.controllers;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.*;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ca.cars365.entity.Users;
import ca.cars365.enums.UserRole;
import ca.cars365.repositories.UserRepository;
import ca.cars365.services.JwtService;

@RestController
@RequestMapping("/api/v1/auth/google")
public class GoogleAuthenticationController {
    private final UserRepository users;
    private final PasswordEncoder passwords;
    private final JwtService jwt;
    private final String clientId;
    private final NimbusJwtDecoder decoder;

    public GoogleAuthenticationController(UserRepository users, PasswordEncoder passwords, JwtService jwt,
            @Value("${GOOGLE_CLIENT_ID:}") String clientId) {
        this.users = users;
        this.passwords = passwords;
        this.jwt = jwt;
        this.clientId = clientId.trim();
        decoder = NimbusJwtDecoder.withJwkSetUri("https://www.googleapis.com/oauth2/v3/certs").build();
        decoder.setJwtValidator(new DelegatingOAuth2TokenValidator<>(new JwtTimestampValidator(), token -> {
            boolean valid = token.getAudience().contains(this.clientId)
                    && ("https://accounts.google.com".equals(token.getClaimAsString("iss"))
                        || "accounts.google.com".equals(token.getClaimAsString("iss")))
                    && token.getExpiresAt() != null && token.getSubject() != null && !token.getSubject().isBlank();
            return valid ? OAuth2TokenValidatorResult.success() : OAuth2TokenValidatorResult.failure(
                    new OAuth2Error("invalid_token", "Invalid Google token claims", null));
        }));
    }

    @GetMapping("/config")
    public Map<String, String> config() {
        requireConfiguration();
        return Map.of("clientId", clientId);
    }

    public record GoogleRequest(String credential) {}

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<Map<String, String>> handleError(ResponseStatusException exception) {
        return ResponseEntity.status(exception.getStatusCode()).body(Map.of("message", exception.getReason()));
    }

    @PostMapping
    public Map<String, Object> authenticate(@RequestBody GoogleRequest request) {
        requireConfiguration();
        if (request.credential() == null || request.credential().isBlank() || request.credential().length() > 16384) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Missing Google ID token.");
        }
        Jwt identity;
        try {
            identity = decoder.decode(request.credential());
        } catch (JwtException exception) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Google identity verification failed. Please sign in again.");
        }
        Users user = users.findByGoogleId(identity.getSubject()).orElse(null);
        if (user == null) {
            String email = identity.getClaimAsString("email");
            if (!Boolean.TRUE.equals(identity.getClaimAsBoolean("email_verified")) || email == null || email.isBlank() || email.length() > 255) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Google must provide a verified email address.");
            }
            if (users.existsByEmail(email)) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "An account already uses this email. Sign in using your existing method.");
            }
            String name = identity.getClaimAsString("name");
            if (name == null || name.isBlank()) name = "Google User";
            user = users.save(Users.builder().googleId(identity.getSubject()).email(email)
                    .fullName(name.substring(0, Math.min(name.length(), 100)))
                    .passwordHash(passwords.encode(UUID.randomUUID().toString())).role(UserRole.REGULAR_USER).build());
        }
        if (!Boolean.TRUE.equals(user.getActive())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "This account is inactive.");
        }
        String now = Instant.now().toString();
        return Map.of("token", jwt.generateToken(user), "user", Map.of(
                "userId", user.getId(), "fullName", user.getFullName(), "email", user.getEmail(),
                "role", "BUYER", "status", "ACTIVE", "createdAt", now, "updatedAt", now));
    }

    private void requireConfiguration() {
        if (clientId.isBlank()) throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE,
                "Google sign-in needs GOOGLE_CLIENT_ID configured on the backend.");
    }
}
