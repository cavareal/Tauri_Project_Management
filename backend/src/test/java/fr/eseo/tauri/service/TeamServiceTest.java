package fr.eseo.tauri.service;

import fr.eseo.tauri.model.Project;
import fr.eseo.tauri.model.Student;
import fr.eseo.tauri.model.Team;
import fr.eseo.tauri.model.enumeration.Gender;
import fr.eseo.tauri.repository.StudentRepository;
import fr.eseo.tauri.repository.TeamRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class TeamServiceTest {

    @Mock
    private TeamRepository teamRepository;

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private ProjectService projectService;

    @InjectMocks
    private TeamService teamService;

    @BeforeEach
    void init_mocks() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllTeams() {
        // Arrange
        Team team1 = new Team();
        Team team2 = new Team();
        Project project = new Project();
        when(teamRepository.findAllByProjectId(project.id())).thenReturn(Arrays.asList(team1, team2));
        when(projectService.getCurrentProject()).thenReturn(project);

        // Act
        List<Team> teams = teamService.getAllTeams();

        // Assert
        assertThat(teams).hasSize(2);
        verify(teamRepository, times(1)).findAllByProjectId(project.id());
        verify(projectService, times(1)).getCurrentProject();

    }

    @Test
    void testGetAllTeamsEmptyList() {
        // Arrange
        Team team1 = new Team();
        Team team2 = new Team();
        Project project = new Project();
        when(projectService.getCurrentProject()).thenReturn(project);
        when(teamRepository.findAllByProjectId(project.id())).thenReturn(List.of());

        // Act
        List<Team> teams = teamService.getAllTeams();

        // Assert
        assertThat(teams).isEmpty();
        verify(teamRepository, times(1)).findAllByProjectId(project.id());
        verify(projectService, times(1)).getCurrentProject();
    }

    @Test
    void testGetTeamById() {
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
    void testGetTeamByIdNull() {
        // Arrange
        when(teamRepository.findById(1)).thenReturn(Optional.empty());

        // Act
        Team result = teamService.getTeamById(1);

        // Assert
        assertThat(result).isNull();
    }

    @Test
    void testGetNbWomanByTeamId() {
        // Arrange
        Team team = new Team();
        Student student1 = new Student();
        student1.gender(Gender.WOMAN);
        Student student2 = new Student();
        student2.gender(Gender.MAN);
        when(teamRepository.findById(1)).thenReturn(Optional.of(team));
        when(studentRepository.findByTeam(team)).thenReturn(Arrays.asList(student1, student2));

        // Act
        Integer nbWoman = teamService.getNbWomanByTeamId(1);

        // Assert
        assertThat(nbWoman).isEqualTo(1);
    }

    @Test
    void testGetNbWomanByTeamIdZero() {
        // Arrange
        Team team = new Team();
        Student student1 = new Student();
        student1.gender(Gender.MAN);
        Student student2 = new Student();
        student2.gender(Gender.MAN);
        when(teamRepository.findById(1)).thenReturn(Optional.of(team));
        when(studentRepository.findByTeam(team)).thenReturn(Arrays.asList(student1, student2));

        // Act
        Integer nbWoman = teamService.getNbWomanByTeamId(1);

        // Assert
        assertThat(nbWoman).isZero();
    }

    @Test
    void testGetNbBachelorsByTeamId() {
        // Arrange
        Team team = new Team();
        Student student1 = new Student();
        student1.bachelor(true);
        Student student2 = new Student();
        student2.bachelor(false);
        when(teamRepository.findById(1)).thenReturn(Optional.of(team));
        when(studentRepository.findByTeam(team)).thenReturn(Arrays.asList(student1, student2));

        // Act
        Integer nbBachelors = teamService.getNbBachelorByTeamId(1);

        // Assert
        assertThat(nbBachelors).isEqualTo(1);
    }

    @Test
    void testGetNbBachelorsByTeamIdZero() {
        // Arrange
        Team team = new Team();
        Student student1 = new Student();
        student1.bachelor(false);
        Student student2 = new Student();
        student2.bachelor(false);
        when(teamRepository.findById(1)).thenReturn(Optional.of(team));
        when(studentRepository.findByTeam(team)).thenReturn(Arrays.asList(student1, student2));

        // Act
        Integer nbBachelors = teamService.getNbBachelorByTeamId(1);

        // Assert
        assertThat(nbBachelors).isZero();
    }

    @Test
    void testGetNbStudentsByTeamId() {
        // Arrange
        Team team = new Team();
        Student student1 = new Student();
        Student student2 = new Student();
        when(teamRepository.findById(1)).thenReturn(Optional.of(team));
        when(studentRepository.findByTeam(team)).thenReturn(Arrays.asList(student1, student2));

        // Act
        Integer nbStudents = teamService.getNbStudentsByTeamId(1);

        // Assert
        assertThat(nbStudents).isEqualTo(2);
    }

    @Test
    void testGetNbStudentsByTeamIdZero() {
        // Arrange
        Team team = new Team();
        when(teamRepository.findById(1)).thenReturn(Optional.of(team));
        when(studentRepository.findByTeam(team)).thenReturn(List.of());

        // Act
        Integer nbStudents = teamService.getNbStudentsByTeamId(1);

        // Assert
        assertThat(nbStudents).isZero();
    }
}