package fr.eseo.tauri.unit.service;

import fr.eseo.tauri.exception.ResourceNotFoundException;
import fr.eseo.tauri.model.*;
import fr.eseo.tauri.model.enumeration.Gender;
import fr.eseo.tauri.repository.*;
import fr.eseo.tauri.service.AuthService;
import fr.eseo.tauri.service.ProjectService;
import fr.eseo.tauri.service.StudentService;
import fr.eseo.tauri.service.TeamService;
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

    private static final String READ_PERMISSION = "readTeam";
    private static final String DELETE_PERMISSION = "deleteTeam";

    @Mock
    private TeamRepository teamRepository;

    @Mock
    private AuthService authService;

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private ProjectService projectService;

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private GradeTypeRepository gradeTypeRepository;

    @Mock
    private GradeRepository gradeRepository;

    @Mock
    private StudentService studentService;

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
    void getAllTeamsByProjectThrowsSecurityExceptionWhenUnauthorized() {
        String token = "validToken";
        Integer projectId = 1;

        when(authService.checkAuth(token, "readTeams")).thenReturn(false);

        assertThrows(SecurityException.class, () -> teamService.getAllTeamsByProject(token, projectId));
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

    @Test
    void createTeamsShouldCreateTeamsWhenNumberOfTeamsIsGreaterThanZero() {
        String token = "validToken";
        Integer projectId = 1;
        Integer nbTeams = 3;
        Project project = new Project();

        when(authService.checkAuth(anyString(), anyString())).thenReturn(true);
        when(projectService.getProjectById(anyString(), anyInt())).thenReturn(project);
        when(teamRepository.findAllByProject(anyInt())).thenReturn(Collections.emptyList());

        List<Team> result = teamService.createTeams(token, projectId, nbTeams);

        assertEquals(nbTeams, result.size());
        verify(teamRepository, times(nbTeams)).save(any(Team.class));
    }

    @Test
    void createTeamsShouldDeleteExistingTeamsBeforeCreatingNewOnes() {
        String token = "validToken";
        Integer projectId = 1;
        Integer nbTeams = 3;
        Project project = new Project();
        List<Team> existingTeams = Arrays.asList(new Team(), new Team());

        when(authService.checkAuth(anyString(), anyString())).thenReturn(true);
        when(projectService.getProjectById(anyString(), anyInt())).thenReturn(project);
        when(teamRepository.findAllByProject(anyInt())).thenReturn(existingTeams);

        teamService.createTeams(token, projectId, nbTeams);

        verify(teamService, times(1)).deleteAllTeamsByProject(token, projectId);
    }

    @Test
    void getNbWomenByTeamIdShouldReturnCountWhenAuthorizedAndTeamExists() {
        String token = "validToken";
        Integer id = 1;
        Integer expectedCount = 2;

        when(authService.checkAuth(token, DELETE_PERMISSION)).thenReturn(true);
        when(authService.checkAuth(token, READ_PERMISSION)).thenReturn(true);
        when(teamRepository.findById(id)).thenReturn(Optional.of(new Team()));
        when(teamRepository.countWomenInTeam(id)).thenReturn(expectedCount);

        Integer actualCount = teamService.getNbWomenByTeamId(token, id);

        assertEquals(expectedCount, actualCount);
    }

    @Test
    void getNbWomenByTeamIdShouldThrowSecurityExceptionWhenUnauthorized() {
        String token = "validToken";
        Integer id = 1;

        when(authService.checkAuth(token, DELETE_PERMISSION)).thenReturn(false);

        assertThrows(SecurityException.class, () -> teamService.getNbWomenByTeamId(token, id));
    }

    @Test
    void getNbBachelorByTeamIdShouldReturnCountWhenAuthorizedAndTeamExists() {
        String token = "validToken";
        Integer id = 1;
        Integer expectedCount = 2;

        when(authService.checkAuth(token, DELETE_PERMISSION)).thenReturn(true);
        when(authService.checkAuth(token, READ_PERMISSION)).thenReturn(true);
        when(teamRepository.findById(id)).thenReturn(Optional.of(new Team()));
        when(teamRepository.countBachelorInTeam(id)).thenReturn(expectedCount);

        Integer actualCount = teamService.getNbBachelorByTeamId(token, id);

        assertEquals(expectedCount, actualCount);
    }

    @Test
    void getNbBachelorByTeamIdShouldThrowSecurityExceptionWhenUnauthorized() {
        String token = "validToken";
        Integer id = 1;

        when(authService.checkAuth(token, DELETE_PERMISSION)).thenReturn(false);

        assertThrows(SecurityException.class, () -> teamService.getNbBachelorByTeamId(token, id));
    }

    @Test
    void getStudentsByTeamIdShouldReturnStudentsWhenAuthorizedAndTeamExists() {
        String token = "validToken";
        Integer id = 1;
        List<Student> expectedStudents = Arrays.asList(new Student(), new Student());

        when(authService.checkAuth(token, READ_PERMISSION)).thenReturn(true);
        when(teamRepository.findById(id)).thenReturn(Optional.of(new Team()));
        when(studentRepository.findByTeam(id)).thenReturn(expectedStudents);

        List<Student> actualStudents = teamService.getStudentsByTeamId(token, id);

        assertEquals(expectedStudents, actualStudents);
    }

    @Test
    void getStudentsByTeamIdShouldThrowSecurityExceptionWhenUnauthorized() {
        String token = "validToken";
        Integer id = 1;

        when(authService.checkAuth(token, READ_PERMISSION)).thenReturn(false);

        assertThrows(SecurityException.class, () -> teamService.getStudentsByTeamId(token, id));
    }

    @Test
    void getStudentsByTeamIdShouldThrowResourceNotFoundExceptionWhenTeamDoesNotExist() {
        String token = "validToken";
        Integer id = 1;

        when(authService.checkAuth(token, READ_PERMISSION)).thenReturn(true);
        when(teamRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> teamService.getStudentsByTeamId(token, id));
    }

    @Test
    void getTeamAvgGradeShouldReturnAverageWhenAuthorizedAndTeamExists() {
        String token = "validToken";
        Integer id = 1;
        Double expectedAvg = 85.0;

        when(authService.checkAuth(token, READ_PERMISSION)).thenReturn(true);
        when(teamRepository.findById(id)).thenReturn(Optional.of(new Team()));
        when(teamRepository.findAvgGradeByTeam(any(Team.class))).thenReturn(expectedAvg);

        Double actualAvg = teamService.getTeamAvgGrade(token, id);

        assertEquals(expectedAvg, actualAvg);
    }

    @Test
    void getTeamAvgGradeShouldThrowSecurityExceptionWhenUnauthorized() {
        String token = "validToken";
        Integer id = 1;

        when(authService.checkAuth(token, READ_PERMISSION)).thenReturn(false);

        assertThrows(SecurityException.class, () -> teamService.getTeamAvgGrade(token, id));
    }

    @Test
    void getTeamAvgGradeShouldThrowResourceNotFoundExceptionWhenTeamDoesNotExist() {
        String token = "validToken";
        Integer id = 1;

        when(authService.checkAuth(token, READ_PERMISSION)).thenReturn(true);
        when(teamRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> teamService.getTeamAvgGrade(token, id));
    }

    @Test
    void getCriteriaByTeamIdShouldReturnCriteriaWhenAuthorizedAndTeamExists() {
        String token = "validToken";
        Integer id = 1;
        Integer projectId = 1;
        Project project = new Project();
        project.nbWomen(2);

        when(authService.checkAuth(token, READ_PERMISSION)).thenReturn(true);
        when(authService.checkAuth(token, DELETE_PERMISSION)).thenReturn(true);
        when(teamRepository.countBachelorInTeam(id)).thenReturn(1);
        when(teamRepository.findById(id)).thenReturn(Optional.of(new Team()));
        when(projectService.getProjectById(token, projectId)).thenReturn(project);
        when(teamRepository.countWomenInTeam(id)).thenReturn(2);
        when(teamRepository.countBachelorInTeam(id)).thenReturn(1);

        Criteria actualCriteria = teamService.getCriteriaByTeamId(token, id, projectId);

        assertEquals(2, actualCriteria.nbWomens());
        assertEquals(1, actualCriteria.nbBachelors());
        assertTrue(actualCriteria.validCriteriaWoman());
        assertTrue(actualCriteria.validCriteriaBachelor());
    }

    @Test
    void getCriteriaByTeamIdShouldThrowSecurityExceptionWhenUnauthorized() {
        String token = "validToken";
        Integer id = 1;
        Integer projectId = 1;

        when(authService.checkAuth(token, READ_PERMISSION)).thenReturn(false);

        assertThrows(SecurityException.class, () -> teamService.getCriteriaByTeamId(token, id, projectId));
    }

    @Test
    void getCriteriaByTeamIdShouldThrowResourceNotFoundExceptionWhenTeamDoesNotExist() {
        String token = "validToken";
        Integer id = 1;
        Integer projectId = 1;

        when(authService.checkAuth(token, READ_PERMISSION)).thenReturn(true);
        when(teamRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> teamService.getCriteriaByTeamId(token, id, projectId));
    }

    @Test
    void getFeedbacksByTeamAndSprintShouldReturnCommentsWhenAuthorizedAndTeamExists() {
        String token = "validToken";
        Integer teamId = 1;
        Integer sprintId = 1;
        List<Comment> expectedComments = Arrays.asList(new Comment(), new Comment());

        when(authService.checkAuth(token, READ_PERMISSION)).thenReturn(true);
        when(commentRepository.findAllByTeamIdAndSprintId(teamId, sprintId)).thenReturn(expectedComments);

        List<Comment> actualComments = teamService.getFeedbacksByTeamAndSprint(token, teamId, sprintId);

        assertEquals(expectedComments, actualComments);
    }

    @Test
    void getFeedbacksByTeamAndSprintShouldThrowSecurityExceptionWhenUnauthorized() {
        String token = "validToken";
        Integer teamId = 1;
        Integer sprintId = 1;

        when(authService.checkAuth(token, READ_PERMISSION)).thenReturn(false);

        assertThrows(SecurityException.class, () -> teamService.getFeedbacksByTeamAndSprint(token, teamId, sprintId));
    }

    @Test
    void getFeedbacksByTeamAndSprintShouldReturnEmptyListWhenNoCommentsExist() {
        String token = "validToken";
        Integer teamId = 1;
        Integer sprintId = 1;
        List<Comment> expectedComments = new ArrayList<>();

        when(authService.checkAuth(token, READ_PERMISSION)).thenReturn(true);
        when(commentRepository.findAllByTeamIdAndSprintId(teamId, sprintId)).thenReturn(expectedComments);

        List<Comment> actualComments = teamService.getFeedbacksByTeamAndSprint(token, teamId, sprintId);

        assertEquals(expectedComments, actualComments);
    }

    @Test
    void getTeamTotalGradeShouldReturnAverageWhenAuthorizedAndTeamExists() {
        String token = "validToken";
        Integer teamId = 1;
        Integer sprintId = 1;
        Double expectedAvg = 90.0;
        List<GradeType> gradeTypes = Arrays.asList(new GradeType(), new GradeType());

        when(authService.checkAuth(token, READ_PERMISSION)).thenReturn(true);
        when(gradeTypeRepository.findTeacherGradedTeamGradeTypes()).thenReturn(gradeTypes);
        when(gradeRepository.findAverageByGradeTypeForTeam(teamId, sprintId, gradeTypes.get(0).name())).thenReturn(80.0);
        when(gradeRepository.findAverageByGradeTypeForTeam(teamId, sprintId, gradeTypes.get(1).name())).thenReturn(90.0);

        Double actualAvg = teamService.getTeamTotalGrade(token, teamId, sprintId);

        assertEquals(expectedAvg, actualAvg);
    }

    @Test
    void getTeamTotalGradeShouldThrowSecurityExceptionWhenUnauthorized() {
        String token = "validToken";
        Integer teamId = 1;
        Integer sprintId = 1;

        when(authService.checkAuth(token, READ_PERMISSION)).thenReturn(false);

        assertThrows(SecurityException.class, () -> teamService.getTeamTotalGrade(token, teamId, sprintId));
    }

    @Test
    void getIndividualTotalGradesShouldThrowSecurityExceptionWhenUnauthorized() {
        String token = "validToken";
        Integer id = 1;
        Integer sprintId = 1;

        when(authService.checkAuth(token, "readGrades")).thenReturn(false);

        assertThrows(SecurityException.class, () -> teamService.getIndividualTotalGrades(token, id, sprintId));
    }

    @Test
    void getSprintGradesShouldThrowSecurityExceptionWhenUnauthorized() {
        String token = "validToken";
        Integer id = 1;
        Integer sprintId = 1;

        when(authService.checkAuth(token, "readGrade")).thenReturn(false);

        assertThrows(SecurityException.class, () -> teamService.getSprintGrades(token, id, sprintId));
    }



}
