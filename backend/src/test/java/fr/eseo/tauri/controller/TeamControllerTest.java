package fr.eseo.tauri.controller;

import fr.eseo.tauri.model.Team;
import fr.eseo.tauri.service.AuthService;
import fr.eseo.tauri.service.TeamService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@SpringBootTest
public class TeamControllerTest {

    @Mock
    private TeamService teamService;

    @Mock
    private AuthService authService;

    @InjectMocks
    private TeamController teamController;

    @Test
    public void testGetAllTeamsReturnsTeams() {
        // Arrange
        Team team1 = new Team();
        Team team2 = new Team();
        when(authService.checkAuth(anyString(), anyString())).thenReturn(true);
        when(teamService.getAllTeams()).thenReturn(Arrays.asList(team1, team2));

        // Act
        ResponseEntity<List<Team>> response = teamController.getAllTeams("mockToken");

        // Assert
        assertThat(response.getBody()).hasSize(2);
        verify(authService, times(1)).checkAuth(anyString(), anyString());
        verify(teamService, times(1)).getAllTeams();
    }

    @Test
    public void testGetAllTeamsReturnsNotFound() {
        // Arrange
        when(authService.checkAuth(anyString(), anyString())).thenReturn(true);
        when(teamService.getAllTeams()).thenReturn(Arrays.asList());

        // Act
        ResponseEntity<List<Team>> response = teamController.getAllTeams("mockToken");

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        verify(authService, times(1)).checkAuth(anyString(), anyString());
        verify(teamService, times(1)).getAllTeams();
    }

    @Test
    public void testGetAllTeamsReturnsInternalServerError() {
        // Arrange
        when(authService.checkAuth(anyString(), anyString())).thenReturn(true);
        when(teamService.getAllTeams()).thenThrow(new RuntimeException("Unexpected error"));

        // Act
        ResponseEntity<List<Team>> response = teamController.getAllTeams("mockToken");

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        verify(authService, times(1)).checkAuth(anyString(), anyString());
        verify(teamService, times(1)).getAllTeams();
    }

    @Test
    public void testGetAllTeamsReturnsUnauthorized() {
        // Arrange
        when(authService.checkAuth(anyString(), anyString())).thenReturn(false);

        // Act
        ResponseEntity<List<Team>> response = teamController.getAllTeams("mockToken");

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
        verify(authService, times(1)).checkAuth(anyString(), anyString());
        verify(teamService, never()).getAllTeams();
    }
}