package fr.eseo.tauri.unit.service;

import fr.eseo.tauri.model.User;
import fr.eseo.tauri.security.AuthResponse;
import fr.eseo.tauri.security.JwtTokenUtil;
import fr.eseo.tauri.service.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class AuthServiceTest {

    @InjectMocks
    AuthService authService;

    @Mock
    UserDetailsService userDetailsService;

    @Mock
    PasswordEncoder passwordEncoder;

    @Mock
    JwtTokenUtil jwtTokenUtil;

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

//    @Test
//    void loginShouldReturnAuthResponseWhenCredentialsMatch() {
//        String login = "validLogin";
//        String password = "validPassword";
//        User user = new User();
//        user.password(passwordEncoder.encode(password));
//
//        when(userDetailsService.loadUserByUsername(login)).thenReturn(user);
//        when(passwordEncoder.matches(password, user.getPassword())).thenReturn(true);
//        when(jwtTokenUtil.generateAccessToken(user)).thenReturn("validToken");
//
//        AuthResponse actualResponse = authService.login(login, password);
//
//        assertEquals(user.id(), actualResponse.id());
//        assertEquals("validToken", actualResponse.accessToken());
//    }

//    @Test
//    void loginShouldThrowSecurityExceptionWhenCredentialsDoNotMatch() {
//        String login = "validLogin";
//        String password = "validPassword";
//        User user = new User();
//        user.password(passwordEncoder.encode("wrongPassword"));
//
//        when(userDetailsService.loadUserByUsername(login)).thenReturn(user);
//        when(passwordEncoder.matches(password, user.getPassword())).thenReturn(false);
//
//        assertThrows(SecurityException.class, () -> authService.login(login, password));
//    }
}