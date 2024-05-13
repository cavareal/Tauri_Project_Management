package fr.eseo.tauri.service;

import fr.eseo.tauri.exception.ResourceNotFoundException;
import fr.eseo.tauri.model.Project;
import fr.eseo.tauri.model.Role;
import fr.eseo.tauri.model.Student;
import fr.eseo.tauri.model.Team;
import fr.eseo.tauri.model.enumeration.Gender;
import fr.eseo.tauri.model.enumeration.RoleType;
import fr.eseo.tauri.repository.RoleRepository;
import fr.eseo.tauri.repository.StudentRepository;
import fr.eseo.tauri.repository.TeamRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@Nested
class TeamServiceTest {

    @Mock
    private TeamRepository teamRepository;

    @Mock
    private AuthService authService;

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private ProjectService projectService;



    @InjectMocks
    private TeamService teamService;

    @BeforeEach
    void init_mocks() {
        MockitoAnnotations.openMocks(this);
        teamService = spy(teamService);
    }

    @Test
    void getTeamByIdReturnsTeamWhenAuthorizedAndTeamExists() {
        String token = "validToken";
        Integer id = 1;
        Team expectedTeam = new Team();

        when(authService.checkAuth(token, "readTeam")).thenReturn(true);
        when(teamRepository.findById(id)).thenReturn(Optional.of(expectedTeam));

        Team actualTeam = teamService.getTeamById(token, id);

        assertEquals(expectedTeam, actualTeam);
    }

    @Test
    void getTeamByIdThrowsSecurityExceptionWhenUnauthorized() {
        String token = "validToken";
        Integer id = 1;

        when(authService.checkAuth(token, "readTeam")).thenReturn(false);

        assertThrows(SecurityException.class, () -> teamService.getTeamById(token, id));
    }

    @Test
    void getTeamByIdThrowsResourceNotFoundExceptionWhenTeamDoesNotExist() {
        String token = "validToken";
        Integer id = 1;

        when(authService.checkAuth(token, "readTeam")).thenReturn(true);
        when(teamRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> teamService.getTeamById(token, id));
    }

    @Test
    void getAllTeamsByProjectReturnsTeamsWhenAuthorizedAndProjectExists() {
        String token = "validToken";
        Integer projectId = 1;
        List<Team> expectedTeams = Arrays.asList(new Team(), new Team());

        when(authService.checkAuth(token, "readTeams")).thenReturn(true);
        when(teamRepository.findAllByProject(projectId)).thenReturn(expectedTeams);

        List<Team> actualTeams = teamService.getAllTeamsByProject(token, projectId);

        assertEquals(expectedTeams, actualTeams);
    }

    @Test
    void getAllTeamsByProjectThrowsSecurityExceptionWhenUnauthorized() {
        String token = "validToken";
        Integer projectId = 1;

        when(authService.checkAuth(token, "readTeams")).thenReturn(false);

        assertThrows(SecurityException.class, () -> teamService.getAllTeamsByProject(token, projectId));
    }

    @Test
    void getAllTeamsByProjectReturnsEmptyListWhenNoTeamsExist() {
        String token = "validToken";
        Integer projectId = 1;

        when(authService.checkAuth(token, "readTeams")).thenReturn(true);
        when(teamRepository.findAllByProject(projectId)).thenReturn(Collections.emptyList());

        List<Team> actualTeams = teamService.getAllTeamsByProject(token, projectId);

        assertTrue(actualTeams.isEmpty());
    }

    @Test
    void createTeamsShouldCreateTeamsWhenAuthorizedAndProjectExists() {
        String token = "validToken";
        Integer projectId = 1;
        Integer nbTeams = 3;

        when(authService.checkAuth(token, "createTeam")).thenReturn(true);
        when(authService.checkAuth(token, "createTeam")).thenReturn(true);
        when(authService.checkAuth(token, "readTeams")).thenReturn(true);
        when(authService.checkAuth(token, "deleteTeam")).thenReturn(true);
        when(authService.checkAuth(token, "readProject")).thenReturn(true);
        when(projectService.getProjectById(token, projectId)).thenReturn(new Project());
        when(teamService.getAllTeamsByProject(token, projectId)).thenReturn(Collections.emptyList());

        List<Team> teams = teamService.createTeams(token, projectId, nbTeams);

        assertEquals(nbTeams, teams.size());
        verify(teamRepository, times(nbTeams)).save(any(Team.class));
    }

    @Test
    void createTeamsShouldThrowIllegalArgumentExceptionWhenNumberOfTeamsIsLessThanOne() {
        String token = "validToken";
        Integer projectId = 1;
        Integer nbTeams = 0;

        assertThrows(IllegalArgumentException.class, () -> teamService.createTeams(token, projectId, nbTeams));
    }

    @Test
    void updateTeamShouldThrowSecurityExceptionWhenUnauthorized() {
        String token = "validToken";
        Integer id = 1;
        Team updatedTeam = new Team();

        when(authService.checkAuth(token, "updateTeam")).thenReturn(false);

        assertThrows(SecurityException.class, () -> teamService.updateTeam(token, id, updatedTeam));
    }

    @Test
    void deleteAllTeamsByProjectShouldDeleteTeamsWhenAuthorizedAndProjectExists() {
        String token = "validToken";
        Integer projectId = 1;

        when(authService.checkAuth(token, "deleteTeam")).thenReturn(true);

        teamService.deleteAllTeamsByProject(token, projectId);

        verify(studentRepository, times(1)).removeAllStudentsFromTeams(projectId);
        verify(teamRepository, times(1)).deleteAllByProject(projectId);
    }

    @Test
    void deleteAllTeamsByProjectShouldThrowSecurityExceptionWhenUnauthorized() {
        String token = "validToken";
        Integer projectId = 1;

        when(authService.checkAuth(token, "deleteTeam")).thenReturn(false);

        assertThrows(SecurityException.class, () -> teamService.deleteAllTeamsByProject(token, projectId));
    }

    @Test
    void getNbWomenByTeamIdThrowsSecurityExceptionWhenUnauthorized() {
        String token = "validToken";
        Integer id = 1;

        when(authService.checkAuth(token, "deleteTeam")).thenReturn(false);

        assertThrows(SecurityException.class, () -> teamService.getNbWomenByTeamId(token, id));
    }

    @Test
    void getNbBachelorByTeamIdThrowsSecurityExceptionWhenUnauthorized() {
        String token = "validToken";
        Integer id = 1;

        when(authService.checkAuth(token, "deleteTeam")).thenReturn(false);

        assertThrows(SecurityException.class, () -> teamService.getNbBachelorByTeamId(token, id));
    }

    @Test
    void getStudentsByTeamIdThrowsSecurityExceptionWhenUnauthorized() {
        String token = "validToken";
        Integer id = 1;

        when(authService.checkAuth(token, "readTeam")).thenReturn(false);

        assertThrows(SecurityException.class, () -> teamService.getStudentsByTeamId(token, id));
    }

    @Test
    void getTeamAvgGradeThrowsSecurityExceptionWhenUnauthorized() {
        String token = "validToken";
        Integer id = 1;

        when(authService.checkAuth(token, "readTeam")).thenReturn(false);

        assertThrows(SecurityException.class, () -> teamService.getTeamAvgGrade(token, id));
    }

    @Test
    void getCriteriaByTeamIdThrowsSecurityExceptionWhenUnauthorized() {
        String token = "validToken";
        Integer id = 1;
        Integer projectId = 1;

        when(authService.checkAuth(token, "readTeam")).thenReturn(false);

        assertThrows(SecurityException.class, () -> teamService.getCriteriaByTeamId(token, id, projectId));
    }

    @Test
    void generateTeamsThrowsSecurityExceptionWhenUnauthorized() {
        String token = "validToken";
        Integer projectId = 1;
        Project projectDetails = new Project();

        when(authService.checkAuth(token, "createTeam")).thenReturn(false);

        assertThrows(SecurityException.class, () -> teamService.generateTeams(token, projectId, projectDetails));
    }

    @Test
    void generateTeamsThrowsIllegalArgumentExceptionWhenNotEnoughStudents() {
        String token = "validToken";
        Integer projectId = 1;
        Project projectDetails = new Project();
        projectDetails.nbTeams(3);
        projectDetails.nbWomen(2);

        List<Student> women = List.of(new Student());
        List<Student> men = Arrays.asList(new Student(), new Student());

        when(authService.checkAuth(token, "createTeam")).thenReturn(true);
        when(studentRepository.findByGender(Gender.WOMAN)).thenReturn(women);
        when(studentRepository.findByGenderOrderByBachelorAndImportedAvgDesc(Gender.MAN)).thenReturn(men);

        assertThrows(IllegalArgumentException.class, () -> teamService.generateTeams(token, projectId, projectDetails));
    }

}




