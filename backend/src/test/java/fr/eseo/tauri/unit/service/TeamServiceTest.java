package fr.eseo.tauri.unit.service;

import fr.eseo.tauri.exception.ResourceNotFoundException;
import fr.eseo.tauri.model.*;
import fr.eseo.tauri.model.enumeration.Gender;
import fr.eseo.tauri.model.enumeration.GradeTypeName;
import fr.eseo.tauri.repository.*;
import fr.eseo.tauri.service.*;
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
    private SprintService sprintService;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private PresentationOrderService presentationOrderService;

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

        List<Student> actualStudents = teamService.getStudentsByTeamId(id, false);

        assertEquals(expectedStudents, actualStudents);
    }


    @Test
    void getStudentsByTeamIdShouldThrowResourceNotFoundExceptionWhenTeamDoesNotExist() {
        Integer id = 1;

        when(teamRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> teamService.getStudentsByTeamId(id, false));
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

//    @Test
//    void getStudentsByTeamIdOrderedShouldReturnOrderedStudentsWhenCurrentSprintExistsAndPresentationOrderMatchesStudentSize() {
//        Integer id = 1;
//        Team team = new Team();
//        Sprint currentSprint = new Sprint();
//        Project project = new Project();
//        project.id(1);
//        team.project(project);
//        List<Student> students = Arrays.asList(new Student(), new Student());
//
//        when(teamRepository.findById(id)).thenReturn(Optional.of(team));
//        when(teamService.getTeamById(id)).thenReturn(team);
//        when(sprintService.getCurrentSprint(team.project().id())).thenReturn(currentSprint);
//        when(studentRepository.findByTeam(id)).thenReturn(students);
//
//        List<Student> result = teamService.getStudentsByTeamIdOrdered(id);
//
//        assertEquals(students.get(1), result.get(0));
//        assertEquals(students.get(0), result.get(1));
//    }

//    @Test
//    void getStudentsByTeamIdOrderedShouldReturnUnorderedStudentsWhenCurrentSprintExistsAndPresentationOrderDoesNotMatchStudentSize() {
//        Integer id = 1;
//        Team team = new Team();
//        Project project = new Project();
//        project.id(1);
//        team.project(project);
//        Sprint currentSprint = new Sprint();
//        List<Student> students = Arrays.asList(new Student(), new Student());
//
//        when(teamRepository.findById(id)).thenReturn(Optional.of(team));
//        when(teamService.getTeamById(id)).thenReturn(team);
//        when(sprintService.getCurrentSprint(team.project().id())).thenReturn(currentSprint);
//        when(studentRepository.findByTeam(id)).thenReturn(students);
//
//        List<Student> result = teamService.getStudentsByTeamIdOrdered(id);
//
//        assertEquals(students, result);
//    }
//
//    @Test
//    void getStudentsByTeamIdOrderedShouldReturnUnorderedStudentsWhenCurrentSprintDoesNotExist() {
//        Integer id = 1;
//        Team team = new Team();
//        Project project = new Project();
//        project.id(1);
//        team.project(project);
//        List<Student> students = Arrays.asList(new Student(), new Student());
//
//        when(teamRepository.findById(id)).thenReturn(Optional.of(team));
//        when(teamService.getTeamById(id)).thenReturn(team);
//        when(sprintService.getCurrentSprint(team.project().id())).thenReturn(null);
//        when(studentRepository.findByTeam(id)).thenReturn(students);
//
//        List<Student> result = teamService.getStudentsByTeamIdOrdered(id);
//
//        assertEquals(students, result);
//    }

    @Test
    void getAverageSprintGradesShouldReturnAverageGradesWhenTeamsExist() {
        Integer sprintId = 1;
        Team team = new Team();
        team.id(1);
        Project project = new Project();
        project.id(1);
        team.project(project);
        Team team2 = new Team();
        team2.project(project);
        team2.id(2);
        List<Team> teams = Arrays.asList(team, team2);
        List<Double> sprintGradesTeam1 = Arrays.asList(15.0, 16.0, 17.0);
        List<Double> sprintGradesTeam2 = Arrays.asList(18.0, 19.0, 20.0);

        when(teamRepository.findById(1)).thenReturn(Optional.of(team));
        when(teamRepository.findById(2)).thenReturn(Optional.of(team2));
        when(teamRepository.findAll()).thenReturn(teams);
        when(teamService.getSprintGrades(teams.get(0).id(), sprintId)).thenReturn(sprintGradesTeam1);
        when(teamService.getSprintGrades(teams.get(1).id(), sprintId)).thenReturn(sprintGradesTeam2);

        List<Double> result = teamService.getAverageSprintGrades(sprintId);

        assertEquals(2, result.size());
        assertEquals(16.0, result.get(0));
        assertEquals(19.0, result.get(1));
    }

    @Test
    void getAverageSprintGradesShouldReturnEmptyListWhenNoTeamsExist() {
        Integer sprintId = 1;
        List<Team> teams = Collections.emptyList();

        when(teamRepository.findAll()).thenReturn(teams);

        List<Double> result = teamService.getAverageSprintGrades(sprintId);

        assertTrue(result.isEmpty());
    }

    @Test
    void getAverageSprintGradesShouldReturnListWithNegativeOneWhenNoSprintGradesExist() {
        Integer sprintId = 1;
        Team team = new Team();
        team.id(1);
        Project project = new Project();
        project.id(1);
        team.project(project);
        List<Team> teams = Collections.singletonList(team);
        List<Double> sprintGradesTeam1 = Collections.singletonList(-1.0);

        when(teamRepository.findById(1)).thenReturn(Optional.of(team));
        when(teamRepository.findAll()).thenReturn(teams);
        when(teamService.getSprintGrades(teams.get(0).id(), sprintId)).thenReturn(sprintGradesTeam1);

        List<Double> result = teamService.getAverageSprintGrades(sprintId);

        assertEquals(1, result.size());
        assertEquals(-1.0, result.get(0));
    }

    @Test
    void getIndividualTotalGradesShouldReturnCorrectGradesWhenBothTeamAndIndividualGradesExist() {
        Integer id = 1;
        Integer sprintId = 1;
        Student student1 = new Student();
        student1.id(1);
        Student student2 = new Student();
        student2.id(2);
        Team team = new Team();
        team.id(1);
        List<Student> students = Arrays.asList(student1, student2);
        Double teamGrade = 15.0;
        Double individualGrade1 = 16.0;
        Double individualGrade2 = 17.0;

        when(teamRepository.findById(1)).thenReturn(Optional.of(team));
        when(studentRepository.findByTeam(id)).thenReturn(students);
        when(gradeRepository.findAverageByGradeTypeForTeam(id, sprintId, GradeTypeName.GLOBAL_TEAM_PERFORMANCE.displayName())).thenReturn(teamGrade);
        when(gradeRepository.findAverageByGradeTypeForStudent(students.get(0).id(), sprintId, GradeTypeName.INDIVIDUAL_PERFORMANCE.displayName())).thenReturn(individualGrade1);
        when(gradeRepository.findAverageByGradeTypeForStudent(students.get(1).id(), sprintId, GradeTypeName.INDIVIDUAL_PERFORMANCE.displayName())).thenReturn(individualGrade2);

        List<Double> result = teamService.getIndividualTotalGrades(id, sprintId);

        assertEquals(2, result.size());
        assertEquals(15.67, result.get(0));
        assertEquals(16.33, result.get(1));
    }

    @Test
    void getIndividualTotalGradesShouldReturnTeamGradesWhenIndividualGradesDoNotExist() {
        Integer id = 1;
        Integer sprintId = 1;
        Student student1 = new Student();
        student1.id(1);
        Student student2 = new Student();
        student2.id(2);
        Team team = new Team();
        team.id(1);
        List<Student> students = Arrays.asList(student1, student2);
        Double teamGrade = 15.0;

        when(teamRepository.findById(1)).thenReturn(Optional.of(team));
        when(studentRepository.findByTeam(id)).thenReturn(students);
        when(gradeRepository.findAverageByGradeTypeForTeam(id, sprintId, GradeTypeName.GLOBAL_TEAM_PERFORMANCE.displayName())).thenReturn(teamGrade);
        when(gradeRepository.findAverageByGradeTypeForStudent(students.get(0).id(), sprintId, GradeTypeName.INDIVIDUAL_PERFORMANCE.displayName())).thenReturn(null);
        when(gradeRepository.findAverageByGradeTypeForStudent(students.get(1).id(), sprintId, GradeTypeName.INDIVIDUAL_PERFORMANCE.displayName())).thenReturn(null);

        List<Double> result = teamService.getIndividualTotalGrades(id, sprintId);

        assertEquals(2, result.size());
        assertEquals(teamGrade, result.get(0));
        assertEquals(teamGrade, result.get(1));
    }

    @Test
    void getIndividualTotalGradesShouldReturnIndividualGradesWhenTeamGradesDoNotExist() {
        Integer id = 1;
        Integer sprintId = 1;
        Student student1 = new Student();
        student1.id(1);
        Student student2 = new Student();
        student2.id(2);
        Team team = new Team();
        team.id(1);
        List<Student> students = Arrays.asList(student1, student2);
        Double individualGrade1 = 16.0;
        Double individualGrade2 = 17.0;

        when(teamRepository.findById(1)).thenReturn(Optional.of(team));
        when(studentRepository.findByTeam(id)).thenReturn(students);
        when(gradeRepository.findAverageByGradeTypeForTeam(id, sprintId, GradeTypeName.GLOBAL_TEAM_PERFORMANCE.displayName())).thenReturn(null);
        when(gradeRepository.findAverageByGradeTypeForStudent(students.get(0).id(), sprintId, GradeTypeName.INDIVIDUAL_PERFORMANCE.displayName())).thenReturn(individualGrade1);
        when(gradeRepository.findAverageByGradeTypeForStudent(students.get(1).id(), sprintId, GradeTypeName.INDIVIDUAL_PERFORMANCE.displayName())).thenReturn(individualGrade2);

        List<Double> result = teamService.getIndividualTotalGrades(id, sprintId);

        assertEquals(2, result.size());
        assertEquals(individualGrade1, result.get(0));
        assertEquals(individualGrade2, result.get(1));
    }

    @Test
    void assignWomenPerTeamShouldAssignWomenAndMenToTeams() {
        List<Team> teams = Arrays.asList(new Team(), new Team());
        List<Student> women = Arrays.asList(new Student(), new Student());
        List<Student> men = Arrays.asList(new Student(), new Student());
        Integer womenPerTeam = 1;

        teamService.assignWomenPerTeam(teams, women, men, womenPerTeam);

        verify(roleRepository, times(2)).save(any(Role.class));
        verify(studentRepository, times(2)).save(any(Student.class));
    }

    @Test
    void assignWomenPerTeamShouldAssignOnlyWomenToTeamsWhenNoMen() {
        List<Team> teams = Arrays.asList(new Team(), new Team());
        List<Student> women = Arrays.asList(new Student(), new Student());
        List<Student> men = Collections.emptyList();
        Integer womenPerTeam = 1;

        teamService.assignWomenPerTeam(teams, women, men, womenPerTeam);

        verify(roleRepository, times(2)).save(any(Role.class));
        verify(studentRepository, times(2)).save(any(Student.class));
    }

    @Test
    void assignWomenPerTeamShouldNotAssignAnyStudentWhenNoTeams() {
        List<Team> teams = Collections.emptyList();
        List<Student> women = Arrays.asList(new Student(), new Student());
        List<Student> men = Arrays.asList(new Student(), new Student());
        Integer womenPerTeam = 1;

        teamService.assignWomenPerTeam(teams, women, men, womenPerTeam);

        verify(roleRepository, times(0)).save(any(Role.class));
        verify(studentRepository, times(0)).save(any(Student.class));
    }

    @Test
    void assignWomenPerTeamShouldNotAssignAnyStudentWhenNoStudents() {
        List<Team> teams = Arrays.asList(new Team(), new Team());
        List<Student> women = Collections.emptyList();
        List<Student> men = Collections.emptyList();
        Integer womenPerTeam = 1;

        teamService.assignWomenPerTeam(teams, women, men, womenPerTeam);

        verify(roleRepository, times(0)).save(any(Role.class));
        verify(studentRepository, times(0)).save(any(Student.class));
    }

    @Test
    void assignStudentsEvenlyShouldAssignStudentsToTeams() {
        List<Team> teams = Arrays.asList(new Team(), new Team());
        List<Student> women = Arrays.asList(new Student(), new Student());
        List<Student> men = Arrays.asList(new Student(), new Student());
        Integer projectId = 1;
        int index = 0;

        when(teamRepository.findAllOrderByAvgGradeOrderByAsc(projectId)).thenReturn(teams);

        teamService.assignStudentsEvenly(women, men, projectId, index, teams);

        verify(roleRepository, times(4)).save(any(Role.class));
        verify(studentRepository, times(4)).save(any(Student.class));
    }

    @Test
    void assignStudentsEvenlyShouldAssignOnlyWomenToTeamsWhenNoMen() {
        List<Team> teams = Arrays.asList(new Team(), new Team());
        List<Student> women = Arrays.asList(new Student(), new Student());
        List<Student> men = Collections.emptyList();
        Integer projectId = 1;
        int index = 0;

        when(teamRepository.findAllOrderByAvgGradeOrderByAsc(projectId)).thenReturn(teams);

        teamService.assignStudentsEvenly(women, men, projectId, index, teams);

        verify(roleRepository, times(2)).save(any(Role.class));
        verify(studentRepository, times(2)).save(any(Student.class));
    }

    @Test
    void assignStudentsEvenlyShouldNotAssignAnyStudentWhenNoStudents() {
        List<Team> teams = Arrays.asList(new Team(), new Team());
        List<Student> women = Collections.emptyList();
        List<Student> men = Collections.emptyList();
        Integer projectId = 1;
        int index = 0;

        teamService.assignStudentsEvenly(women, men, projectId, index, teams);

        verify(roleRepository, times(0)).save(any(Role.class));
        verify(studentRepository, times(0)).save(any(Student.class));
    }

    @Test
    void fillTeamsShouldAssignStudentsToTeams() {
        List<Team> teams = Arrays.asList(new Team(), new Team());
        List<Student> women = Arrays.asList(new Student(), new Student());
        List<Student> men = Arrays.asList(new Student(), new Student());
        Integer womenPerTeam = 1;
        Integer projectId = 1;

        when(teamRepository.findAllOrderByAvgGradeOrderByAsc(projectId)).thenReturn(teams);

        teamService.fillTeams(teams, women, men, womenPerTeam, false, projectId);

        verify(roleRepository, times(4)).save(any(Role.class));
        verify(studentRepository, times(4)).save(any(Student.class));
    }

    @Test
    void fillTeamsShouldAssignOnlyWomenToTeamsWhenNoMen() {
        List<Team> teams = Arrays.asList(new Team(), new Team());
        List<Student> women = Arrays.asList(new Student(), new Student());
        List<Student> men = Collections.emptyList();
        Integer womenPerTeam = 1;
        Integer projectId = 1;

        when(teamRepository.findAllOrderByAvgGradeOrderByAsc(projectId)).thenReturn(teams);

        teamService.fillTeams(teams, women, men, womenPerTeam, false, projectId);

        verify(roleRepository, times(2)).save(any(Role.class));
        verify(studentRepository, times(2)).save(any(Student.class));
    }

    @Test
    void fillTeamsShouldNotAssignAnyStudentWhenNoStudents() {
        List<Team> teams = Arrays.asList(new Team(), new Team());
        List<Student> women = Collections.emptyList();
        List<Student> men = Collections.emptyList();
        Integer womenPerTeam = 1;
        Integer projectId = 1;

        teamService.fillTeams(teams, women, men, womenPerTeam, false, projectId);

        verify(roleRepository, times(0)).save(any(Role.class));
        verify(studentRepository, times(0)).save(any(Student.class));
    }

    @Test
    void fillTeamsShouldAssignStudentsEvenlyWhenAutoWomenRatioIsTrue() {
        List<Team> teams = Arrays.asList(new Team(), new Team());
        List<Student> women = Arrays.asList(new Student(), new Student());
        List<Student> men = Arrays.asList(new Student(), new Student());
        Integer womenPerTeam = 1;
        Integer projectId = 1;

        when(teamRepository.findAllOrderByAvgGradeOrderByAsc(projectId)).thenReturn(teams);

        teamService.fillTeams(teams, women, men, womenPerTeam, true, projectId);

        verify(roleRepository, times(4)).save(any(Role.class));
        verify(studentRepository, times(4)).save(any(Student.class));
    }

    @Test
    void generateTeamsShouldThrowExceptionWhenNotEnoughStudents() {
        Integer projectId = 1;
        Project projectDetails = new Project();
        projectDetails.nbTeams(3);
        projectDetails.nbWomen(2);
        boolean autoWomenRatio = false;

        List<Student> women = Arrays.asList(new Student(), new Student());
        List<Student> men = Arrays.asList(new Student(), new Student());

        when(studentRepository.findByGenderAndProjectId(Gender.WOMAN, projectId)).thenReturn(women);
        when(studentRepository.findByGenderOrderByBachelorAndImportedAvgDesc(Gender.MAN, projectId)).thenReturn(men);

        assertThrows(IllegalArgumentException.class, () -> teamService.generateTeams(projectId, projectDetails, autoWomenRatio));
    }

}
