package ca.cars365.controllers;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import ca.cars365.entity.Users;
import ca.cars365.enums.UserRole;
import ca.cars365.repositories.UserRepository;
import ca.cars365.services.JwtService;

@RestController
@RequestMapping("/api/v1/auth/facebook")
public class FacebookAuthenticationController {
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(FacebookAuthenticationController.class);
    private final UserRepository users;
    private final PasswordEncoder passwords;
    private final JwtService jwt;
    private final RestClient graph;
    private final tools.jackson.databind.json.JsonMapper json = tools.jackson.databind.json.JsonMapper.builder().build();
    private final String appId;
    private final String appSecret;
    private final String version;

    public FacebookAuthenticationController(UserRepository users, PasswordEncoder passwords, JwtService jwt,
            @Value("${FACEBOOK_APP_ID:}") String appId,
            @Value("${FACEBOOK_APP_SECRET:}") String appSecret,
            @Value("${FACEBOOK_GRAPH_VERSION:v23.0}") String version) {
        this.users = users;
        this.passwords = passwords;
        this.jwt = jwt;
        this.appId = appId;
        this.appSecret = appSecret;
        this.version = version;
        this.graph = RestClient.builder().baseUrl("https://graph.facebook.com/" + version).build();
    }

    @GetMapping("/config")
    public Map<String, String> config() {
        requireConfiguration();
        return Map.of("appId", appId, "version", version);
    }

    public record FacebookRequest(String accessToken) {}

    @ExceptionHandler(ResponseStatusException.class)
    public org.springframework.http.ResponseEntity<Map<String, String>> handleError(ResponseStatusException exception) {
        return org.springframework.http.ResponseEntity.status(exception.getStatusCode())
                .body(Map.of("message", exception.getReason() == null ? "Facebook sign-in failed." : exception.getReason()));
    }

    @PostMapping
    @SuppressWarnings("unchecked")
    public Map<String, Object> authenticate(@RequestBody FacebookRequest request) {
        requireConfiguration();
        if (request.accessToken() == null || request.accessToken().isBlank() || request.accessToken().length() > 4096) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Missing Facebook access token.");
        }
        Map<String, Object> profile;
        try {
            Map<String, Object> result = readGraphJson(graph.get().uri(builder -> builder.path("/debug_token")
                    .queryParam("input_token", request.accessToken()).build())
                    .header("Authorization", "Bearer " + appId + "|" + appSecret)
                    .retrieve().body(String.class));
            Map<String, Object> data = result == null ? null : (Map<String, Object>) result.get("data");
            if (data == null || !Boolean.TRUE.equals(data.get("is_valid")) || !appId.equals(data.get("app_id"))
                    || !"USER".equals(data.get("type"))
                    || !(data.get("expires_at") instanceof Number expiry)
                    || expiry.longValue() <= Instant.now().getEpochSecond()) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,
                        "Facebook returned an expired token or a token for a different app. Sign in again.");
            }
            profile = readGraphJson(graph.get().uri("/me?fields=id,name,email")
                    .header("Authorization", "Bearer " + request.accessToken()).retrieve().body(String.class));
            if (profile == null || profile.get("id") == null || !profile.get("id").equals(data.get("user_id"))) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Facebook identity verification failed. Sign in again.");
            }
        } catch (ResponseStatusException exception) {
            throw exception;
        } catch (RestClientResponseException exception) {
            // Never log the request URL, headers, response body, or access tokens.
            var codeMatcher = java.util.regex.Pattern.compile("\"code\"\\s*:\\s*(\\d+)")
                    .matcher(exception.getResponseBodyAsString());
            String code = codeMatcher.find() ? codeMatcher.group(1) : "unknown";
            log.warn("Facebook Graph verification rejected: HTTP {}, code {}", exception.getStatusCode().value(), code);
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,
                    "Facebook rejected verification (code " + code + "). Sign in again; if it persists, check the Meta app configuration.");
        } catch (ResourceAccessException exception) {
            Throwable cause = exception.getMostSpecificCause();
            log.warn("Facebook Graph connection failed: {}", cause.getClass().getSimpleName());
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE,
                    "The backend cannot connect securely to Facebook. Check the server connection and Java certificates.");
        } catch (Exception exception) {
            log.warn("Facebook verification failed: {}", exception.getClass().getSimpleName());
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Facebook verification failed. Please try again.");
        }
        String facebookId = profile.get("id").toString();
        Users user = users.findByFacebookId(facebookId).orElse(null);
        if (user == null) {
            String email = profile.get("email") instanceof String value ? value : "";
            if (email.isBlank()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Allow Facebook to share your email, or sign up with email.");
            }
            if (users.existsByEmail(email)) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "An account already uses this email. Please sign in with your password.");
            }
            String name = profile.get("name") instanceof String value ? value : "Facebook User";
            user = users.save(Users.builder().facebookId(facebookId).email(email)
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
        if (appId.isBlank() || appSecret.isBlank()) {
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE,
                    "Facebook login needs a Meta App ID and App Secret configured on the server.");
        }
    }

    @SuppressWarnings("unchecked")
    private Map<String, Object> readGraphJson(String body) {
        // Meta may return JSON with text/javascript rather than application/json.
        return body == null ? null : json.readValue(body, Map.class);
    }
}
