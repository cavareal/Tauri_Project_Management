package fr.eseo.tauri.service;

import fr.eseo.tauri.exception.ResourceNotFoundException;
import fr.eseo.tauri.model.*;
import fr.eseo.tauri.model.enumeration.Gender;
import fr.eseo.tauri.model.enumeration.RoleType;
import fr.eseo.tauri.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@Nested
class GradeServiceTest {

    private final String TEST_TOKEN = "testToken";

    @Mock
    private GradeRepository gradeRepository;

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private GradeTypeRepository gradeTypeRepository;

    @Mock
    private TeamRepository teamRepository;

    @Mock
    private UserRepository userRepository;

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

    @InjectMocks
    private GradeService gradeService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getGradeByIdShouldReturnGradeWhenAuthorized() {
        Grade grade = new Grade();
        when(authService.checkAuth(anyString(), anyString())).thenReturn(true);
        when(gradeRepository.findById(anyInt())).thenReturn(Optional.of(grade));

        Grade result = gradeService.getGradeById(TEST_TOKEN, 1);

        assertEquals(grade, result);
    }

    @Test
    void getGradeByIdShouldThrowSecurityExceptionWhenNotAuthorized() {
        when(authService.checkAuth(anyString(), anyString())).thenReturn(false);

        assertThrows(SecurityException.class, () -> gradeService.getGradeById(TEST_TOKEN, 1));
    }

    @Test
    void getGradeByIdShouldThrowResourceNotFoundExceptionWhenGradeNotFound() {
        when(authService.checkAuth(anyString(), anyString())).thenReturn(true);
        when(gradeRepository.findById(anyInt())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> gradeService.getGradeById(TEST_TOKEN, 1));
    }

    @Test
    void getAllUnimportedGradesByProjectShouldReturnGradesWhenAuthorized() {
        List<Grade> grades = Collections.singletonList(new Grade());
        when(authService.checkAuth(anyString(), anyString())).thenReturn(true);
        when(gradeRepository.findAllUnimportedByProject(anyInt())).thenReturn(grades);

        List<Grade> result = gradeService.getAllUnimportedGradesByProject(TEST_TOKEN, 1);

        assertEquals(grades, result);
    }

    @Test
    void getAllUnimportedGradesByProjectShouldThrowExceptionWhenNotAuthorized() {
        when(authService.checkAuth(anyString(), anyString())).thenReturn(false);

        assertThrows(SecurityException.class, () -> gradeService.getAllUnimportedGradesByProject(TEST_TOKEN, 1));
    }

    @Test
    void getAllImportedGradesByProjectShouldReturnGradesWhenAuthorized() {
        List<Grade> grades = Collections.singletonList(new Grade());
        when(authService.checkAuth(anyString(), anyString())).thenReturn(true);
        when(gradeRepository.findAllImportedByProject(anyInt())).thenReturn(grades);

        List<Grade> result = gradeService.getAllImportedGradesByProject(TEST_TOKEN, 1);

        assertEquals(grades, result);
    }

    @Test
    void getAllImportedGradesByProjectShouldThrowExceptionWhenNotAuthorized() {
        when(authService.checkAuth(anyString(), anyString())).thenReturn(false);

        assertThrows(SecurityException.class, () -> gradeService.getAllImportedGradesByProject(TEST_TOKEN, 1));
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

        when(authService.checkAuth(anyString(), anyString())).thenReturn(true);
        when(userService.getUserById(anyString(), anyInt())).thenReturn(new User());
        when(sprintService.getSprintById(anyString(), anyInt())).thenReturn(new Sprint());
        when(gradeTypeService.getGradeTypeById(anyString(), anyInt())).thenReturn(gradeType);
        when(teamService.getTeamById(anyString(), anyInt())).thenReturn(new Team());

        gradeService.createGrade(TEST_TOKEN, grade);

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

        when(authService.checkAuth(anyString(), anyString())).thenReturn(true);
        when(userService.getUserById(anyString(), anyInt())).thenReturn(new User());
        when(sprintService.getSprintById(anyString(), anyInt())).thenReturn(new Sprint());
        when(gradeTypeService.getGradeTypeById(anyString(), anyInt())).thenReturn(gradeType);
        when(studentService.getStudentById(anyString(), anyInt())).thenReturn(new Student());

        gradeService.createGrade(TEST_TOKEN, grade);

        verify(gradeRepository, times(1)).save(any(Grade.class));
    }

    @Test
    void createGradeShouldThrowExceptionWhenNotAuthorized() {
        Grade grade = new Grade();

        when(authService.checkAuth(anyString(), anyString())).thenReturn(false);

        assertThrows(SecurityException.class, () -> gradeService.createGrade(TEST_TOKEN, grade));
    }

    @Test
    void updateGradeShouldThrowExceptionWhenNotAuthorized() {
        Grade updatedGrade = new Grade();

        when(authService.checkAuth(anyString(), anyString())).thenReturn(false);

        assertThrows(SecurityException.class, () -> gradeService.updateGrade(TEST_TOKEN, 1, updatedGrade));
    }

    @Test
    void deleteGradeShouldDeleteGradeWhenAuthorized() {
        when(authService.checkAuth(anyString(), anyString())).thenReturn(true);
        when(gradeRepository.findById(anyInt())).thenReturn(Optional.of(new Grade()));

        gradeService.deleteGrade(TEST_TOKEN, 1);

        verify(gradeRepository, times(1)).deleteById(anyInt());
    }

    @Test
    void deleteGradeShouldThrowSecurityExceptionWhenNotAuthorized() {
        when(authService.checkAuth(anyString(), anyString())).thenReturn(false);

        assertThrows(SecurityException.class, () -> gradeService.deleteGrade(TEST_TOKEN, 1));
    }

    @Test
    void deleteGradeShouldThrowResourceNotFoundExceptionWhenGradeNotFound() {
        when(authService.checkAuth(anyString(), anyString())).thenReturn(true);
        when(gradeRepository.findById(anyInt())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> gradeService.deleteGrade(TEST_TOKEN, 1));
    }

    @Test
    void deleteAllGradesByProjectShouldDeleteGradesWhenAuthorized() {
        when(authService.checkAuth(anyString(), anyString())).thenReturn(true);

        gradeService.deleteAllGradesByProject(TEST_TOKEN, 1);

        verify(gradeRepository, times(1)).deleteAllByProject(anyInt());
    }

    @Test
    void deleteAllGradesByProjectShouldThrowSecurityExceptionWhenNotAuthorized() {
        when(authService.checkAuth(anyString(), anyString())).thenReturn(false);

        assertThrows(SecurityException.class, () -> gradeService.deleteAllGradesByProject(TEST_TOKEN, 1));
    }

    @Test
    void updateImportedMeanShouldNotUpdateMeanForBachelorStudents() {
        Student student = new Student();
        student.id(1);
        student.bachelor(true);
        List<Student> students = Collections.singletonList(student);

        when(studentRepository.findAll()).thenReturn(students);

        gradeService.updateImportedMean();

        verify(gradeRepository, never()).updateImportedMeanByStudentId(anyFloat(), anyInt());
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
    void createStudentIndividualGradesCSVReportShouldGenerateCorrectReportWithValidInput() throws IOException {
        String token = "testToken";
        int userId = 1;
        Student student = new Student();
        student.name("John Doe");
        student.gender(Gender.MAN);
        student.bachelor(true);
        GradeType gradeType = new GradeType();
        gradeType.name("Test Grade");
        Grade grade = new Grade();
        grade.value(90f);
        grade.gradeType(gradeType);
        List<Grade> grades = Collections.singletonList(grade);

        when(authService.checkAuth(token, "exportGrades")).thenReturn(true);
        when(studentService.getStudentById(token, userId)).thenReturn(student);
        when(gradeRepository.findAllunimportedByStudentId(userId)).thenReturn(grades);

        byte[] result = gradeService.createStudentIndividualGradesCSVReport(token, userId);

        String expectedCsv = "\"\",\"\",\"\",\"Test Grade\"\n\"John Doe\",\"M\",\"B\",\"90.0\"\n";
        String actualCsv = new String(result);

        assertEquals(expectedCsv, actualCsv);
    }

    @Test
    void createStudentIndividualGradesCSVReportShouldThrowSecurityExceptionWhenNotAuthorized() {
        String token = "testToken";
        int userId = 1;

        when(authService.checkAuth(token, "exportGrades")).thenReturn(false);

        assertThrows(SecurityException.class, () -> gradeService.createStudentIndividualGradesCSVReport(token, userId));
    }

    @Test
    void createStudentIndividualGradesCSVReportShouldHandleNoGrades() throws IOException {
        String token = "testToken";
        int userId = 1;
        Student student = new Student();
        student.name("John Doe");
        student.gender(Gender.MAN);
        student.bachelor(true);
        List<Grade> grades = Collections.emptyList();

        when(authService.checkAuth(token, "exportGrades")).thenReturn(true);
        when(studentService.getStudentById(token, userId)).thenReturn(student);
        when(gradeRepository.findAllunimportedByStudentId(userId)).thenReturn(grades);

        byte[] result = gradeService.createStudentIndividualGradesCSVReport(token, userId);

        String expectedCsv = "\"\",\"\",\"\"\n\"John Doe\",\"M\",\"B\"\n";
        String actualCsv = new String(result);

        assertEquals(expectedCsv, actualCsv);
    }
}