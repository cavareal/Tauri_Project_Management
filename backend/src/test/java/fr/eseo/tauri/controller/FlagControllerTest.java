package fr.eseo.tauri.controller;

import fr.eseo.tauri.model.Flag;
import fr.eseo.tauri.service.AuthService;
import fr.eseo.tauri.service.FlagService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class FlagControllerTest {

    @InjectMocks
    private FlagController flagController;

    @Mock
    private FlagService flagService;

    @Mock
    private AuthService authService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    /*@Test
    void testAddFlag() {
        Flag flag = new Flag();
        when(authService.checkAuth(anyString(), anyString())).thenReturn(true);
        when(flagService.createFlag("testToken", )).thenReturn(flag);
        when(flagService.addFlag(any(Flag.class))).thenReturn(flag);
        when(flagService.createFlag(any(Flag.class))).thenReturn(flag);

        ResponseEntity<Flag> response = flagController.addFlag(flag, "token");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(flag, response.getBody());

        verify(authService, times(1)).checkAuth(anyString(), anyString());
        verify(flagService, times(1)).addFlag(any(Flag.class));
    }

    @Test
    void testAddFlagUnauthorized() {
        Flag flag = new Flag();
        when(authService.checkAuth(anyString(), anyString())).thenReturn(false);

        ResponseEntity<Flag> response = flagController.addFlag(flag, "token");

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());

        verify(authService, times(1)).checkAuth(anyString(), anyString());
    }

    @Test
    void testGetFlagsByAuthorAndDescription() {
        Flag flag = new Flag();
        List<Flag> flags = List.of(flag);
        when(authService.checkAuth(anyString(), anyString())).thenReturn(true);
        when(flagService.getFlagsByAuthorAndDescription(anyInt(), anyString())).thenReturn(flags);

        ResponseEntity<List<Flag>> response = flagController.getFlagsByAuthorAndDescription(1, "description", "token");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(flags, response.getBody());

        verify(authService, times(1)).checkAuth(anyString(), anyString());
        verify(flagService, times(1)).getFlagsByAuthorAndDescription(anyInt(), anyString());
    }

    @Test
    void testGetFlagsByAuthorAndDescriptionUnauthorized() {
        when(authService.checkAuth(anyString(), anyString())).thenReturn(false);

        ResponseEntity<List<Flag>> response = flagController.getFlagsByAuthorAndDescription(1, "description", "token");

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());

        verify(authService, times(1)).checkAuth(anyString(), anyString());
    }*/
}
