package fr.eseo.tauri.service;

import fr.eseo.tauri.util.CustomLogger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

public class AuthServiceTest {

    @InjectMocks
    AuthService authService;

    @Mock
    CustomLogger customLogger;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void checkAuthShouldReturnTrueWhenPermissionExists() {
        String token = "validToken";
        String permission = "validPermission";

        Boolean result = authService.checkAuth(token, permission);

        assertTrue(result);
    }
}