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
        Integer id = 1;
        Team expectedTeam = new Team();

        when(teamRepository.findById(id)).thenReturn(Optional.of(expectedTeam));

        Team actualTeam = teamService.getTeamById(id);

        assertEquals(expectedTeam, actualTeam);
    }


    @Test
    void getTeamByIdThrowsResourceNotFoundExceptionWhenTeamDoesNotExist() {
        Integer id = 1;

        when(teamRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> teamService.getTeamById(id));
    }

    @Test
    void createTeamsShouldThrowIllegalArgumentExceptionWhenNumberOfTeamsIsLessThanOne() {
        Integer projectId = 1;
        Integer nbTeams = 0;

        assertThrows(IllegalArgumentException.class, () -> teamService.createTeams(projectId, nbTeams));
    }

    @Test
    void deleteAllTeamsByProjectShouldDeleteTeamsWhenAuthorizedAndProjectExists() {
        Integer projectId = 1;

        teamService.deleteAllTeamsByProject(projectId);

        verify(studentRepository, times(1)).removeAllStudentsFromTeams(projectId);
        verify(teamRepository, times(1)).deleteAllByProject(projectId);
    }

    @Test
    void generateTeamsThrowsIllegalArgumentExceptionWhenNotEnoughStudents() {
        Integer projectId = 1;
        Project projectDetails = new Project();
        projectDetails.nbTeams(3);
        projectDetails.nbWomen(2);

        List<Student> women = List.of(new Student());
        List<Student> men = Arrays.asList(new Student(), new Student());

        when(studentRepository.findByGenderAndProjectId(Gender.WOMAN, projectId)).thenReturn(women);
        when(studentRepository.findByGenderOrderByBachelorAndImportedAvgDesc(Gender.MAN, projectId)).thenReturn(men);

        assertThrows(IllegalArgumentException.class, () -> teamService.generateTeams(projectId, projectDetails));
    }

    @Test
    void createTeamsShouldCreateTeamsWhenNumberOfTeamsIsGreaterThanZero() {
        Integer projectId = 1;
        Integer nbTeams = 3;
        Project project = new Project();

        when(projectService.getProjectById(anyInt())).thenReturn(project);
        when(teamRepository.findAllByProject(anyInt())).thenReturn(Collections.emptyList());

        List<Team> result = teamService.createTeams(projectId, nbTeams);

        assertEquals(nbTeams, result.size());
        verify(teamRepository, times(nbTeams)).save(any(Team.class));
    }

    @Test
    void createTeamsShouldDeleteExistingTeamsBeforeCreatingNewOnes() {
        Integer projectId = 1;
        Integer nbTeams = 3;
        Project project = new Project();
        List<Team> existingTeams = Arrays.asList(new Team(), new Team());

        when(projectService.getProjectById(anyInt())).thenReturn(project);
        when(teamRepository.findAllByProject(anyInt())).thenReturn(existingTeams);

        teamService.createTeams(projectId, nbTeams);

        verify(teamService, times(1)).deleteAllTeamsByProject(projectId);
    }

    @Test
    void getNbWomenByTeamIdShouldReturnCountWhenAuthorizedAndTeamExists() {
        Integer id = 1;
        Integer expectedCount = 2;

        when(teamRepository.findById(id)).thenReturn(Optional.of(new Team()));
        when(teamRepository.countWomenInTeam(id)).thenReturn(expectedCount);

        Integer actualCount = teamService.getNbWomenByTeamId(id);

        assertEquals(expectedCount, actualCount);
    }


    @Test
    void getNbBachelorByTeamIdShouldReturnCountWhenAuthorizedAndTeamExists() {
        Integer id = 1;
        Integer expectedCount = 2;

        when(teamRepository.findById(id)).thenReturn(Optional.of(new Team()));
        when(teamRepository.countBachelorInTeam(id)).thenReturn(expectedCount);

        Integer actualCount = teamService.getNbBachelorByTeamId(id);

        assertEquals(expectedCount, actualCount);
    }


    @Test
    void getStudentsByTeamIdShouldReturnStudentsWhenAuthorizedAndTeamExists() {
        Integer id = 1;
        List<Student> expectedStudents = Arrays.asList(new Student(), new Student());

        when(teamRepository.findById(id)).thenReturn(Optional.of(new Team()));
        when(studentRepository.findByTeam(id)).thenReturn(expectedStudents);

        List<Student> actualStudents = teamService.getStudentsByTeamId(id);

        assertEquals(expectedStudents, actualStudents);
    }


    @Test
    void getStudentsByTeamIdShouldThrowResourceNotFoundExceptionWhenTeamDoesNotExist() {
        Integer id = 1;

        when(teamRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> teamService.getStudentsByTeamId(id));
    }

    @Test
    void getTeamAvgGradeShouldReturnAverageWhenAuthorizedAndTeamExists() {
        Integer id = 1;
        Double expectedAvg = 85.0;

        when(teamRepository.findById(id)).thenReturn(Optional.of(new Team()));
        when(teamRepository.findAvgGradeByTeam(any(Team.class))).thenReturn(expectedAvg);

        Double actualAvg = teamService.getTeamAvgGrade(id);

        assertEquals(expectedAvg, actualAvg);
    }


    @Test
    void getTeamAvgGradeShouldThrowResourceNotFoundExceptionWhenTeamDoesNotExist() {
        Integer id = 1;

        when(teamRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> teamService.getTeamAvgGrade(id));
    }

    @Test
    void getCriteriaByTeamIdShouldReturnCriteriaWhenAuthorizedAndTeamExists() {
        Integer id = 1;
        Integer projectId = 1;
        Project project = new Project();
        project.nbWomen(2);

        when(teamRepository.countBachelorInTeam(id)).thenReturn(1);
        when(teamRepository.findById(id)).thenReturn(Optional.of(new Team()));
        when(projectService.getProjectById(projectId)).thenReturn(project);
        when(teamRepository.countWomenInTeam(id)).thenReturn(2);
        when(teamRepository.countBachelorInTeam(id)).thenReturn(1);

        Criteria actualCriteria = teamService.getCriteriaByTeamId(id, projectId);

        assertEquals(2, actualCriteria.nbWomens());
        assertEquals(1, actualCriteria.nbBachelors());
        assertTrue(actualCriteria.validCriteriaWoman());
        assertTrue(actualCriteria.validCriteriaBachelor());
    }


    @Test
    void getCriteriaByTeamIdShouldThrowResourceNotFoundExceptionWhenTeamDoesNotExist() {
        Integer id = 1;
        Integer projectId = 1;

        when(teamRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> teamService.getCriteriaByTeamId(id, projectId));
    }

    @Test
    void getFeedbacksByTeamAndSprintShouldReturnCommentsWhenAuthorizedAndTeamExists() {
        Integer teamId = 1;
        Integer sprintId = 1;
        List<Comment> expectedComments = Arrays.asList(new Comment(), new Comment());

        when(commentRepository.findAllByTeamIdAndSprintId(teamId, sprintId)).thenReturn(expectedComments);

        List<Comment> actualComments = teamService.getFeedbacksByTeamAndSprint(teamId, sprintId);

        assertEquals(expectedComments, actualComments);
    }


    @Test
    void getFeedbacksByTeamAndSprintShouldReturnEmptyListWhenNoCommentsExist() {
        Integer teamId = 1;
        Integer sprintId = 1;
        List<Comment> expectedComments = new ArrayList<>();

        when(commentRepository.findAllByTeamIdAndSprintId(teamId, sprintId)).thenReturn(expectedComments);

        List<Comment> actualComments = teamService.getFeedbacksByTeamAndSprint(teamId, sprintId);

        assertEquals(expectedComments, actualComments);
    }

    @Test
    void getTeamTotalGradeShouldReturnAverageWhenAuthorizedAndTeamExists() {
        Integer teamId = 1;
        Integer sprintId = 1;
        Double expectedAvg = 90.0;
        List<GradeType> gradeTypes = Arrays.asList(new GradeType(), new GradeType());

        when(gradeTypeRepository.findTeacherGradedTeamGradeTypes()).thenReturn(gradeTypes);
        when(gradeRepository.findAverageByGradeTypeForTeam(teamId, sprintId, gradeTypes.get(0).name())).thenReturn(80.0);
        when(gradeRepository.findAverageByGradeTypeForTeam(teamId, sprintId, gradeTypes.get(1).name())).thenReturn(90.0);

        Double actualAvg = teamService.getTeamTotalGrade(teamId, sprintId);

        assertEquals(expectedAvg, actualAvg);
    }


    @Test
    void getTeamByIdShouldReturnTeamWhenAuthorizedAndIdExists() {
        Team team = new Team();
        when(teamRepository.findById(anyInt())).thenReturn(Optional.of(team));

        Team result = teamService.getTeamById(1);

        assertEquals(team, result);
    }


    @Test
    void getTeamByIdShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() {
        when(teamRepository.findById(anyInt())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> teamService.getTeamById(1));
    }

    @Test
    void getAllTeamsByProjectShouldReturnTeamsWhenAuthorized() {
        List<Team> teams = new ArrayList<>();
        when(teamRepository.findAllByProject(anyInt())).thenReturn(teams);

        List<Team> result = teamService.getAllTeamsByProject(1);

        assertEquals(teams, result);
    }


    @Test
    void createTeamsShouldCreateTeamsWhenAuthorized() {
        Project project = new Project();
        when(projectService.getProjectById(anyInt())).thenReturn(project);
        when(teamRepository.save(any(Team.class))).thenAnswer(i -> i.getArguments()[0]);

        List<Team> result = teamService.createTeams(1, 3);

        assertEquals(3, result.size());
    }


    @Test
    void updateTeamShouldUpdateWhenAuthorizedAndIdExists() {
        Team team = new Team();
        Team updatedTeam = new Team();
        updatedTeam.name("Updated Team");
        when(teamRepository.findById(anyInt())).thenReturn(Optional.of(team));

        teamService.updateTeam(1, updatedTeam);

        verify(teamRepository, times(1)).save(updatedTeam);
    }


    @Test
    void updateTeamShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() {
        Team updatedTeam = new Team();
        when(teamRepository.findById(anyInt())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> teamService.updateTeam(1, updatedTeam));
    }


    @Test
    void getNbWomenByTeamIdShouldThrowResourceNotFoundExceptionWhenTeamDoesNotExist() {
        when(teamRepository.findById(anyInt())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> teamService.getNbWomenByTeamId(1));
    }

    @Test
    void getNbBachelorByTeamIdShouldThrowResourceNotFoundExceptionWhenTeamDoesNotExist() {
        when(teamRepository.findById(anyInt())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> teamService.getNbBachelorByTeamId(1));
    }


    @Test
    void getIndividualTotalGradesShouldThrowResourceNotFoundExceptionWhenTeamDoesNotExist() {
        when(teamRepository.findById(anyInt())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> teamService.getIndividualTotalGrades(1, 1));
    }

    @Test
    void getSprintGradesShouldThrowResourceNotFoundExceptionWhenTeamDoesNotExist() {
        when(teamRepository.findById(anyInt())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> teamService.getSprintGrades(1, 1));
    }

}
