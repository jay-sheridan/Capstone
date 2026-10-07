package ca.cars365;

import ca.cars365.controllers.GoogleAuthenticationController;
import ca.cars365.repositories.UserRepository;
import ca.cars365.services.JwtService;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.server.ResponseStatusException;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GoogleAuthenticationControllerTests {
    private final UserRepository users = mock(UserRepository.class);
    private GoogleAuthenticationController controller(String clientId) {
        return new GoogleAuthenticationController(users, mock(PasswordEncoder.class), mock(JwtService.class), clientId);
    }

    @Test void unconfiguredLoginReturnsServiceUnavailable() {
        assertEquals(503, assertThrows(ResponseStatusException.class, () -> controller("").config()).getStatusCode().value());
        verifyNoInteractions(users);
    }

    @Test void missingCredentialCannotCreateAnAccount() {
        assertEquals(400, assertThrows(ResponseStatusException.class, () -> controller("test.apps.googleusercontent.com")
                .authenticate(new GoogleAuthenticationController.GoogleRequest(""))).getStatusCode().value());
        verifyNoInteractions(users);
    }

    @Test void forgedCredentialCannotCreateAnAccount() {
        assertEquals(401, assertThrows(ResponseStatusException.class, () -> controller("test.apps.googleusercontent.com")
                .authenticate(new GoogleAuthenticationController.GoogleRequest("not-a-signed-token"))).getStatusCode().value());
        verifyNoInteractions(users);
    }
}
