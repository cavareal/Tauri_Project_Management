package fr.eseo.tauri.service;

import fr.eseo.tauri.model.Team;
import fr.eseo.tauri.repository.TeamRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class TeamServiceTest {

    @Mock
    private TeamRepository teamRepository;

    @InjectMocks
    private TeamService teamService;

    @Test
    public void testGetAllTeams() {
        // Arrange
        Team team1 = new Team();
        Team team2 = new Team();
        when(teamRepository.findAll()).thenReturn(Arrays.asList(team1, team2));

        // Act
        List<Team> teams = teamService.getAllTeams();

        // Assert
        assertThat(teams).hasSize(2);
        verify(teamRepository, times(1)).findAll();
    }

    @Test
    public void testGetAllTeamsEmptyList() {
        // Arrange
        Team team1 = new Team();
        Team team2 = new Team();
        when(teamRepository.findAll()).thenReturn(Arrays.asList());

        // Act
        List<Team> teams = teamService.getAllTeams();

        // Assert
        assertThat(teams).hasSize(0);
        verify(teamRepository, times(1)).findAll();
    }

    @Test
    public void testGetTeamById() {
        // Arrange
        Team team = new Team();
        when(teamRepository.findById(1)).thenReturn(Optional.of(team));

        // Act
        Team result = teamService.getTeamById(1);

        // Assert
        assertThat(result).isEqualTo(team);
        verify(teamRepository, times(1)).findById(1);
    }

    @Test
    public void testGetTeamByIdNull() {
        // Arrange
        when(teamRepository.findById(1)).thenReturn(Optional.empty());

        // Act
        Team result = teamService.getTeamById(1);

        // Assert
        assertThat(result).isNull();
        verify(teamRepository, times(1)).findById(1);
    }
}