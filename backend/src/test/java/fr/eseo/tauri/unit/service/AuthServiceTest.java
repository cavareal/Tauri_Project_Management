package fr.eseo.tauri.unit.service;


import fr.eseo.tauri.service.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@Nested
class AuthServiceTest {

    @InjectMocks
    AuthService authService;

    @Mock
    AuthenticationManager authenticationManager;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Should throw SecurityException when credentials are incorrect")
    void shouldThrowSecurityExceptionWhenCredentialsAreIncorrect() {
        String email = "john.doe@example.com";
        String password = "wrongpassword";

        when(authenticationManager.authenticate(any())).thenThrow(new BadCredentialsException("Wrong credentials"));

        assertThrows(SecurityException.class, () -> authService.login(email, password));
    }

    @Test
    @DisplayName("Should return Authentication when credentials are correct")
    void shouldReturnAuthenticationWhenCredentialsAreCorrect() {
        String email = "john.doe@example.com";
        String password = "password";
        Authentication authentication = Mockito.mock(Authentication.class);

        when(authenticationManager.authenticate(any())).thenReturn(authentication);

        Authentication actualAuthentication = authService.authenticate(email, password);

        assertNotNull(actualAuthentication);
    }

}