package fr.eseo.tauri.unit.service;

import fr.eseo.tauri.exception.ResourceNotFoundException;
import fr.eseo.tauri.model.*;
import fr.eseo.tauri.model.enumeration.Gender;
import fr.eseo.tauri.model.enumeration.GradeTypeName;
import fr.eseo.tauri.model.enumeration.RoleType;
import fr.eseo.tauri.repository.GradeRepository;
import fr.eseo.tauri.repository.GradeTypeRepository;
import fr.eseo.tauri.repository.StudentRepository;
import fr.eseo.tauri.repository.TeamRepository;
import fr.eseo.tauri.service.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@Nested
class GradeServiceTest {

    @Mock
    private GradeRepository gradeRepository;

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private AuthService authService;

    @Mock
    private UserService userService;

    @Mock
    private SprintService sprintService;

    @Mock
    private GradeTypeService gradeTypeService;

    @Mock
    private TeamService teamService;

    @Mock
    private StudentService studentService;

    @Mock
    private TeamRepository teamRepository;

    @Mock
    private GradeTypeRepository gradeTypeRepository;

    @InjectMocks
    private GradeService gradeService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getGradeByIdShouldReturnGradeWhenAuthorized() {
        Grade grade = new Grade();
        when(gradeRepository.findById(anyInt())).thenReturn(Optional.of(grade));

        Grade result = gradeService.getGradeById(1);

        assertEquals(grade, result);
    }

    @Test
    void getGradeByIdShouldThrowResourceNotFoundExceptionWhenGradeNotFound() {
        when(gradeRepository.findById(anyInt())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> gradeService.getGradeById(1));
    }

    @Test
    void getAllUnimportedGradesByProjectShouldReturnGradesWhenAuthorized() {
        List<Grade> grades = Collections.singletonList(new Grade());

        when(gradeRepository.findAllUnimportedByProject(anyInt())).thenReturn(grades);

        List<Grade> result = gradeService.getAllUnimportedGradesByProject(1);

        assertEquals(grades, result);
    }

    @Test
    void getAllImportedGradesByProjectShouldReturnGradesWhenAuthorized() {
        List<Grade> grades = Collections.singletonList(new Grade());
        when(gradeRepository.findAllImportedByProject(anyInt())).thenReturn(grades);

        List<Grade> result = gradeService.getAllImportedGradesByProject(1);

        assertEquals(grades, result);
    }

    @Test
    void createGradeShouldSaveGradeWhenAuthorizedAndGradeTypeForGroup() {
        Grade grade = new Grade();
        grade.authorId(1);
        grade.sprintId(1);
        grade.gradeTypeId(1);
        grade.teamId(1);
        GradeType gradeType = new GradeType();
        gradeType.forGroup(true);
        grade.gradeType(gradeType);

        when(userService.getUserById(anyInt())).thenReturn(new User());
        when(sprintService.getSprintById(anyInt())).thenReturn(new Sprint());
        when(gradeTypeService.getGradeTypeById(anyInt())).thenReturn(gradeType);
        when(teamService.getTeamById(anyInt())).thenReturn(new Team());

        gradeService.createGrade(grade);

        verify(gradeRepository, times(1)).save(any(Grade.class));
    }

    @Test
    void createGradeShouldSaveGradeWhenAuthorizedAndGradeTypeNotForGroup() {
        Grade grade = new Grade();
        grade.authorId(1);
        grade.sprintId(1);
        grade.gradeTypeId(1);
        grade.studentId(1);
        GradeType gradeType = new GradeType();
        gradeType.forGroup(false);
        grade.gradeType(gradeType);

        when(userService.getUserById(anyInt())).thenReturn(new User());
        when(sprintService.getSprintById(anyInt())).thenReturn(new Sprint());
        when(gradeTypeService.getGradeTypeById(anyInt())).thenReturn(gradeType);
        when(studentService.getStudentById(anyInt())).thenReturn(new Student());

        gradeService.createGrade(grade);

        verify(gradeRepository, times(1)).save(any(Grade.class));
    }

    @Test
    void deleteGradeShouldDeleteGradeWhenAuthorized() {
        when(gradeRepository.findById(anyInt())).thenReturn(Optional.of(new Grade()));

        gradeService.deleteGrade(1);

        verify(gradeRepository, times(1)).deleteById(anyInt());
    }

    @Test
    void deleteGradeShouldThrowResourceNotFoundExceptionWhenGradeNotFound() {
        when(gradeRepository.findById(anyInt())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> gradeService.deleteGrade(1));
    }

    @Test
    void deleteAllGradesByProjectShouldDeleteGradesWhenAuthorized() {
        gradeService.deleteAllGradesByProject(1);

        verify(gradeRepository, times(1)).deleteAllByProject(anyInt());
    }

    @Test
    void meanShouldReturnZeroWhenNoGrades() {
        List<Grade> grades = Collections.emptyList();

        float result = gradeService.mean(grades);

        assertEquals(0, result);
    }

    @Test
    void meanShouldReturnZeroWhenAllFactorsAreZero() {
        GradeType gradeType = new GradeType();
        gradeType.factor(0f);
        Grade grade1 = new Grade();
        grade1.value(90f);
        grade1.gradeType(gradeType);
        Grade grade2 = new Grade();
        grade2.value(80f);
        grade2.gradeType(gradeType);
        List<Grade> grades = Arrays.asList(grade1, grade2);

        float result = gradeService.mean(grades);

        assertEquals(0, result);
    }

    @Test
    void meanShouldReturnCorrectMeanWhenGradesWithFactors() {
        GradeType gradeType1 = new GradeType();
        gradeType1.factor(2f);
        Grade grade1 = new Grade();
        grade1.value(90f);
        grade1.gradeType(gradeType1);
        GradeType gradeType2 = new GradeType();
        gradeType2.factor(1f);
        Grade grade2 = new Grade();
        grade2.value(80f);
        grade2.gradeType(gradeType2);
        List<Grade> grades = Arrays.asList(grade1, grade2);

        float result = gradeService.mean(grades);

        assertEquals(86.67f, result, 0.01f);
    }

    @Test
    void createStudentIndividualGradesCSVReportShouldGenerateCorrectReportWhenAuthorized() throws IOException {
        String token = "validToken";
        int projectId = 1;
        Student student = new Student();
        student.name("John Doe");
        student.gender(Gender.MAN);
        student.bachelor(true);
        GradeType gradeType = new GradeType();
        gradeType.name("Test Grade");
        gradeType.factor(1f);
        List<Student> students = Collections.singletonList(student);
        List<GradeType> gradeTypes = Collections.singletonList(gradeType);

        when(studentRepository.findAllByProject(projectId)).thenReturn(students);
        when(gradeRepository.findAllUnimportedGradeTypesByProjectId(projectId)).thenReturn(gradeTypes);
        when(gradeService.getGradeByStudentAndGradeType(student, gradeType)).thenReturn(90f);

        byte[] result = gradeService.createStudentIndividualGradesCSVReport(projectId);

        String expectedCsv = """
                "","","","Test Grade"
                "","","","1.0"
                "John Doe","M","B","90.0"
                """;
        String actualCsv = new String(result);

        assertEquals(expectedCsv, actualCsv);
    }

    @Test
    void createStudentIndividualGradesCSVReportShouldHandleNoGrades() throws IOException {
        String token = "validToken";
        int projectId = 1;
        Student student = new Student();
        student.name("John Doe");
        student.gender(Gender.MAN);
        student.bachelor(true);
        List<Student> students = Collections.singletonList(student);
        List<GradeType> gradeTypes = Collections.emptyList();

        when(studentRepository.findAllByProject(projectId)).thenReturn(students);
        when(gradeRepository.findAllUnimportedGradeTypesByProjectId(projectId)).thenReturn(gradeTypes);

        byte[] result = gradeService.createStudentIndividualGradesCSVReport(projectId);

        String expectedCsv = """
                "","",""
                "","",""
                "John Doe","M","B"
                """;
        String actualCsv = new String(result);

        assertEquals(expectedCsv, actualCsv);
    }


    @Test
    void updateImportedMeanShouldNotUpdateMeanForBachelorStudents() {
        Student student = new Student();
        student.id(1);
        student.bachelor(true);
        List<Student> students = Collections.singletonList(student);

        when(studentRepository.findAll()).thenReturn(students);

        gradeService.updateImportedMean(1);

        verify(gradeRepository, never()).updateImportedMeanByStudentId(anyFloat(), anyInt());
    }

    @Test
    void updateImportedMeanByStudentIdShouldUpdateWhenValueIsNotNullAndStudentIdIsValid() {
        Float value = 85.0f;
        Integer studentId = 1;

        gradeService.updateImportedMeanByStudentId(value, studentId);

        verify(gradeRepository, times(1)).updateImportedMeanByStudentId(value, studentId);
    }

    @Test
    void updateImportedMeanByStudentIdShouldNotUpdateWhenValueIsNull() {
        Float value = null;
        Integer studentId = 1;

        gradeService.updateImportedMeanByStudentId(value, studentId);

        verify(gradeRepository, never()).updateImportedMeanByStudentId(anyFloat(), anyInt());
    }

    @Test
    void updateImportedMeanByStudentIdShouldNotUpdateWhenStudentIdIsNull() {
        Float value = 85.0f;
        Integer studentId = null;

        gradeService.updateImportedMeanByStudentId(value, studentId);

        verify(gradeRepository, never()).updateImportedMeanByStudentId(anyFloat(), anyInt());
    }

    @Test
    void getAverageGradesByGradeTypeByRoleTypeShouldReturnAverageWhenTeamExists() {
        int userId = 1;
        RoleType roleType = RoleType.TEAM_MEMBER;
        String gradeTypeName = "Test Grade";
        Team team = new Team();
        Double expectedAverage = 85.0;

        when(teamRepository.findTeamByStudentId(userId)).thenReturn(team);
        when(gradeRepository.findAverageGradesByGradeType(team, gradeTypeName, roleType)).thenReturn(expectedAverage);

        Double actualAverage = gradeService.getAverageGradesByGradeTypeByRoleType(userId, roleType, gradeTypeName);

        assertEquals(expectedAverage, actualAverage);
    }

    @Test
    void getAverageGradesByGradeTypeByRoleTypeShouldReturnZeroWhenNoTeamExists() {
        int userId = 1;
        RoleType roleType = RoleType.TEAM_MEMBER;
        String gradeTypeName = "Test Grade";

        when(teamRepository.findTeamByStudentId(userId)).thenReturn(null);

        Double actualAverage = gradeService.getAverageGradesByGradeTypeByRoleType(userId, roleType, gradeTypeName);

        assertEquals(0.0, actualAverage);
    }

    @Test
    void getAverageGradesByGradeTypeByRoleTypeShouldReturnNullWhenNoAverageGradeExists() {
        int userId = 1;
        RoleType roleType = RoleType.TEAM_MEMBER;
        String gradeTypeName = "Test Grade";
        Team team = new Team();

        when(teamRepository.findTeamByStudentId(userId)).thenReturn(team);
        when(gradeRepository.findAverageGradesByGradeType(team, gradeTypeName, roleType)).thenReturn(null);

        Double actualAverage = gradeService.getAverageGradesByGradeTypeByRoleType(userId, roleType, gradeTypeName);

        assertNull(actualAverage);
    }

    @Test
    void getAverageByGradeTypeByStudentIdOrTeamIdShouldReturnTeamAverageWhenGradeTypeForGroup() {
        Integer id = 1;
        Integer sprintId = 1;
        String gradeTypeName = "Test Grade";
        GradeType gradeType = new GradeType();
        gradeType.forGroup(true);
        Double expectedAverage = 85.0;

        when(gradeTypeRepository.findByName(gradeTypeName)).thenReturn(gradeType);
        when(gradeRepository.findAverageByGradeTypeForTeam(id, sprintId, gradeTypeName)).thenReturn(expectedAverage);

        Double actualAverage = gradeService.getAverageByGradeTypeByStudentIdOrTeamId(id, sprintId, gradeTypeName);

        assertEquals(expectedAverage, actualAverage);
    }

    @Test
    void getAverageByGradeTypeByStudentIdOrTeamIdShouldReturnStudentAverageWhenGradeTypeNotForGroup() {
        Integer id = 1;
        Integer sprintId = 1;
        String gradeTypeName = "Test Grade";
        GradeType gradeType = new GradeType();
        gradeType.forGroup(false);
        Double expectedAverage = 90.0;

        when(gradeTypeRepository.findByName(gradeTypeName)).thenReturn(gradeType);
        when(gradeRepository.findAverageByGradeTypeForStudent(id, sprintId, gradeTypeName)).thenReturn(expectedAverage);

        Double actualAverage = gradeService.getAverageByGradeTypeByStudentIdOrTeamId(id, sprintId, gradeTypeName);

        assertEquals(expectedAverage, actualAverage);
    }

    @Test
    void getAverageByGradeTypeByStudentIdOrTeamIdShouldReturnNullWhenNoAverageExists() {
        Integer id = 1;
        Integer sprintId = 1;
        String gradeTypeName = "Test Grade";
        GradeType gradeType = new GradeType();
        gradeType.forGroup(true);

        when(gradeTypeRepository.findByName(gradeTypeName)).thenReturn(gradeType);
        when(gradeRepository.findAverageByGradeTypeForTeam(id, sprintId, gradeTypeName)).thenReturn(null);

        Double actualAverage = gradeService.getAverageByGradeTypeByStudentIdOrTeamId(id, sprintId, gradeTypeName);

        assertNull(actualAverage);
    }

    @Test
    void getTeamGradesShouldReturnCorrectGradesWhenGradeTypesExist() {
        Integer teamId = 1;
        Integer sprintId = 1;
        GradeType gradeType = new GradeType();
        gradeType.name("Test Grade");
        List<GradeType> gradeTypes = Collections.singletonList(gradeType);
        Double expectedAverage = 85.0;

        when(gradeTypeRepository.findAllUnimportedAndForGroup()).thenReturn(gradeTypes);
        when(gradeRepository.findAverageByGradeTypeForTeam(teamId, sprintId, gradeType.name())).thenReturn(expectedAverage);

        Map<String, Double> actualGrades = gradeService.getTeamGrades(teamId, sprintId);

        assertEquals(expectedAverage, actualGrades.get(gradeType.name()));
    }

    @Test
    void getTeamGradesShouldReturnEmptyMapWhenNoGradeTypesExist() {
        Integer teamId = 1;
        Integer sprintId = 1;

        when(gradeTypeRepository.findAllUnimportedAndForGroup()).thenReturn(Collections.emptyList());

        Map<String, Double> actualGrades = gradeService.getTeamGrades(teamId, sprintId);

        assertTrue(actualGrades.isEmpty());
    }

    @Test
    void getTeamGradesShouldReturnNullValueInMapWhenNoAverageGradeExists() {
        Integer teamId = 1;
        Integer sprintId = 1;
        GradeType gradeType = new GradeType();
        gradeType.name("Test Grade");
        List<GradeType> gradeTypes = Collections.singletonList(gradeType);

        when(gradeTypeRepository.findAllUnimportedAndForGroup()).thenReturn(gradeTypes);
        when(gradeRepository.findAverageByGradeTypeForTeam(teamId, sprintId, gradeType.name())).thenReturn(null);

        Map<String, Double> actualGrades = gradeService.getTeamGrades(teamId, sprintId);

        assertNull(actualGrades.get(gradeType.name()));
    }

    @Test
    void getTeamStudentGradesShouldReturnCorrectGradesWhenStudentsExist() {
        Integer teamId = 1;
        Integer sprintId = 1;
        Student student = new Student();
        student.id(1);
        List<Student> teamStudents = Collections.singletonList(student);
        Double expectedAverage = 85.0;

        when(studentRepository.findByTeam(teamId)).thenReturn(teamStudents);
        when(gradeRepository.findAverageByGradeTypeForStudent(student.id(), sprintId, "Performance individuelle")).thenReturn(expectedAverage);

        Map<String, Double> actualGrades = gradeService.getTeamStudentGrades(teamId, sprintId);

        assertEquals(expectedAverage, actualGrades.get(String.valueOf(student.id())));
    }

    @Test
    void getTeamStudentGradesShouldReturnEmptyMapWhenNoStudentsExist() {
        Integer teamId = 1;
        Integer sprintId = 1;

        when(studentRepository.findByTeam(teamId)).thenReturn(Collections.emptyList());

        Map<String, Double> actualGrades = gradeService.getTeamStudentGrades(teamId, sprintId);

        assertTrue(actualGrades.isEmpty());
    }

    @Test
    void getTeamStudentGradesShouldReturnNullValueInMapWhenNoAverageGradeExists() {
        Integer teamId = 1;
        Integer sprintId = 1;
        Student student = new Student();
        student.id(1);
        List<Student> teamStudents = Collections.singletonList(student);

        when(studentRepository.findByTeam(teamId)).thenReturn(teamStudents);
        when(gradeRepository.findAverageByGradeTypeForStudent(student.id(), sprintId, "Performance individuelle")).thenReturn(null);

        Map<String, Double> actualGrades = gradeService.getTeamStudentGrades(teamId, sprintId);

        assertNull(actualGrades.get(String.valueOf(student.id())));
    }

    @Test
    void getGradesConfirmationShouldReturnFalseWhenNoStudentsInTeam() {
        Integer teamId = 1;
        Integer sprintId = 1;

        when(studentRepository.findByTeam(teamId)).thenReturn(Collections.emptyList());

        Boolean result = gradeService.getGradesConfirmation(teamId, sprintId);

        assertFalse(result);
    }

    @Test
    void getGradesConfirmationShouldReturnFalseWhenAllGradesConfirmed() {
        Integer teamId = 1;
        Integer sprintId = 1;
        Student student = new Student();
        student.id(1);
        List<Student> students = Collections.singletonList(student);
        GradeType gradeType = new GradeType();
        gradeType.name(GradeTypeName.INDIVIDUAL_PERFORMANCE.displayName());
        Grade grade = new Grade();
        grade.confirmed(true);

        when(studentRepository.findByTeam(teamId)).thenReturn(students);
        when(gradeTypeService.findByName(GradeTypeName.INDIVIDUAL_PERFORMANCE.displayName())).thenReturn(gradeType);
        when(gradeRepository.findIsConfirmedBySprindAndStudent(sprintId, student.id(), gradeType.id())).thenReturn(grade);

        Boolean result = gradeService.getGradesConfirmation(teamId, sprintId);

        assertFalse(result);
    }

    @Test
    void getGradesConfirmationShouldReturnFalseWhenNoGradesFound() {
        Integer teamId = 1;
        Integer sprintId = 1;
        Student student = new Student();
        student.id(1);
        List<Student> students = Collections.singletonList(student);
        GradeType gradeType = new GradeType();
        gradeType.name(GradeTypeName.INDIVIDUAL_PERFORMANCE.displayName());

        when(studentRepository.findByTeam(teamId)).thenReturn(students);
        when(gradeTypeService.findByName(GradeTypeName.INDIVIDUAL_PERFORMANCE.displayName())).thenReturn(gradeType);
        when(gradeRepository.findIsConfirmedBySprindAndStudent(sprintId, student.id(), gradeType.id())).thenReturn(null);

        Boolean result = gradeService.getGradesConfirmation(teamId, sprintId);

        assertFalse(result);
    }

    @Test
    void setGradesConfirmationShouldReturnFalseWhenNoStudentsInTeam() {
        Integer teamId = 1;
        Integer sprintId = 1;

        when(studentRepository.findByTeam(teamId)).thenReturn(Collections.emptyList());

        Boolean result = gradeService.setGradesConfirmation(teamId, sprintId);

        assertFalse(result);
    }

    @Test
    void setGradesConfirmationShouldReturnTrueWhenGradesNotConfirmed() {
        Integer teamId = 1;
        Integer sprintId = 1;
        Student student = new Student();
        student.id(1);
        List<Student> students = Collections.singletonList(student);
        GradeType gradeType = new GradeType();
        gradeType.name(GradeTypeName.INDIVIDUAL_PERFORMANCE.displayName());
        Grade grade = new Grade();
        grade.confirmed(false);

        when(studentRepository.findByTeam(teamId)).thenReturn(students);
        when(gradeTypeService.findByName(GradeTypeName.INDIVIDUAL_PERFORMANCE.displayName())).thenReturn(gradeType);
        when(gradeRepository.findIsConfirmedBySprindAndStudent(sprintId, student.id(), gradeType.id())).thenReturn(grade);

        Boolean result = gradeService.setGradesConfirmation(teamId, sprintId);

        assertTrue(result);
        verify(gradeRepository, times(1)).save(grade);
    }

    @Test
    void setGradesConfirmationShouldReturnFalseWhenNoGradesFound() {
        Integer teamId = 1;
        Integer sprintId = 1;
        Student student = new Student();
        student.id(1);
        List<Student> students = Collections.singletonList(student);
        GradeType gradeType = new GradeType();
        gradeType.name(GradeTypeName.INDIVIDUAL_PERFORMANCE.displayName());

        when(studentRepository.findByTeam(teamId)).thenReturn(students);
        when(gradeTypeService.findByName(GradeTypeName.INDIVIDUAL_PERFORMANCE.displayName())).thenReturn(gradeType);
        when(gradeRepository.findIsConfirmedBySprindAndStudent(sprintId, student.id(), gradeType.id())).thenReturn(null);

        Boolean result = gradeService.setGradesConfirmation(teamId, sprintId);

        assertFalse(result);
    }
}