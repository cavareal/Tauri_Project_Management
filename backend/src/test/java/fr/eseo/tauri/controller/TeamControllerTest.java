package fr.eseo.tauri.controller;

import fr.eseo.tauri.model.Criteria;
import fr.eseo.tauri.model.Team;
import fr.eseo.tauri.service.AuthService;
import fr.eseo.tauri.service.ProjectService;
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
class TeamControllerTest {

    @Mock
    private TeamService teamService;

    @Mock
    private AuthService authService;

    @Mock
    private ProjectService projectService;

    @InjectMocks
    private TeamController teamController;

    @Test
    void testGetAllTeamsReturnsTeams() {
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
    void testGetAllTeamsReturnsNotFound() {
        // Arrange
        when(authService.checkAuth(anyString(), anyString())).thenReturn(true);
        when(teamService.getAllTeams()).thenReturn(List.of());

        // Act
        ResponseEntity<List<Team>> response = teamController.getAllTeams("mockToken");

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        verify(authService, times(1)).checkAuth(anyString(), anyString());
        verify(teamService, times(1)).getAllTeams();
    }

    @Test
    void testGetAllTeamsReturnsInternalServerError() {
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
    void testGetAllTeamsReturnsUnauthorized() {
        // Arrange
        when(authService.checkAuth(anyString(), anyString())).thenReturn(false);

        // Act
        ResponseEntity<List<Team>> response = teamController.getAllTeams("mockToken");

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
        verify(authService, times(1)).checkAuth(anyString(), anyString());
        verify(teamService, never()).getAllTeams();
    }

    @Test
    void testGetCriteriaByTeamId() {
        // Arrange
        Criteria criteria = new Criteria(5, 3, 10, true, true);
        when(authService.checkAuth(anyString(), anyString())).thenReturn(true);
        when(teamService.getNbWomanByTeamId(1)).thenReturn(5);
        when(teamService.getNbBachelorByTeamId(1)).thenReturn(3);
        when(teamService.getNbStudentsByTeamId(1)).thenReturn(10);
        when(projectService.getRatioGender()).thenReturn(50);

        // Act
        ResponseEntity<Criteria> result = teamController.getCriteriaByTeamId("token", 1);

        // Assert
        assertThat(result.getBody()).usingRecursiveComparison().isEqualTo(criteria);
        verify(authService, times(1)).checkAuth(anyString(), anyString());
        verify(teamService, times(1)).getNbWomanByTeamId(1);
        verify(teamService, times(1)).getNbBachelorByTeamId(1);
        verify(teamService, times(1)).getNbStudentsByTeamId(1);
        verify(projectService, times(1)).getRatioGender();

    }

    @Test
    void testGetCriteriaByTeamIdTeamNotFound() {
        // Arrange
        when(authService.checkAuth(anyString(), anyString())).thenReturn(true);

        // Act
        ResponseEntity<Criteria> result = teamController.getCriteriaByTeamId("token", 1);

        // Assert
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        verify(authService, times(1)).checkAuth(anyString(), anyString());
    }

    @Test
    void testGetCriteriaByTeamIdUnauthorized() {
        // Arrange
        when(authService.checkAuth(anyString(), anyString())).thenReturn(false);

        // Act
        ResponseEntity<Criteria> result = teamController.getCriteriaByTeamId("token", 1);

        // Assert
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
        verify(authService, times(1)).checkAuth(anyString(), anyString());
    }
}