package fr.eseo.tauri.service;

import fr.eseo.tauri.model.*;
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

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@Nested
class GradeServiceTest {

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

    @InjectMocks
    private GradeService gradeService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createGrade_savesGrade() {
        User author = new User();
        GradeType gradeType = new GradeType();
        Student student = new Student();

        gradeService.createGrade(author, gradeType, student, 10, "Good job!");

        verify(gradeRepository, times(1)).save(any(Grade.class));
    }

    @Test
    void createGradesFromGradeTypesAndValues_createsGrades() {
        Student student = new Student();
        List<String> valuesString = Arrays.asList("10.0", "9.0", "8.0");
        GradeType gradeType = new GradeType();
        List<GradeType> gradeTypes = Arrays.asList(gradeType, gradeType, gradeType);

        gradeService.createGradesFromGradeTypesAndValues(student, valuesString, gradeTypes, "Good job!");

        verify(gradeRepository, times(3)).save(any(Grade.class));
    }

    @Test
    void createGradesFromGradeTypesAndValues_ignoresEmptyValues() {
        Student student = new Student();
        List<String> valuesString = Arrays.asList("10.0", "", "");
        GradeType gradeType = new GradeType();
        List<GradeType> gradeTypes = Arrays.asList(gradeType, gradeType, gradeType);

        gradeService.createGradesFromGradeTypesAndValues(student, valuesString, gradeTypes, "Good job!");

        verify(gradeRepository, times(1)).save(any(Grade.class));
    }

    @Test
    void createGradesFromGradeTypesAndValues_ignoresInvalidValues() {
        Student student = new Student();
        List<String> valuesString = Arrays.asList("10.0", "invalid", "8.0");
        GradeType gradeType = new GradeType();
        List<GradeType> gradeTypes = Arrays.asList(gradeType, gradeType, gradeType);

        gradeService.createGradesFromGradeTypesAndValues(student, valuesString, gradeTypes, "Good job!");

        verify(gradeRepository, times(2)).save(any(Grade.class));
    }

    @Test
    void updateImportedMean_handlesNoStudents() {
        List<Student> students = Collections.emptyList();
        List<Grade> grades = Collections.emptyList();

        when(studentRepository.findAll()).thenReturn(students);
        when(gradeRepository.findAll()).thenReturn(grades);

        gradeService.updateImportedMean();

        verify(gradeRepository, times(0)).updateImportedMeanByStudentId(anyFloat(), anyInt());
    }

    @Test
    void createGrade_handlesRepositoryException() {
        User author = new User();
        GradeType gradeType = new GradeType();
        Student student = new Student();

        doThrow(new RuntimeException("Database Error")).when(gradeRepository).save(any(Grade.class));

        assertThrows(RuntimeException.class, () -> gradeService.createGrade(author, gradeType, student, 10, "Good job!"),
                "Expected createGrade to throw, but it did not");

        verify(gradeRepository, times(1)).save(any(Grade.class));
    }

    @Test
    void createGrade_savesGradeWithCorrectAttributes() {
        ArgumentCaptor<Grade> gradeCaptor = ArgumentCaptor.forClass(Grade.class);
        User author = new User();
        GradeType gradeType = new GradeType();
        Student student = new Student();

        gradeService.createGrade(author, gradeType, student, 10, "Good job!");

        verify(gradeRepository).save(gradeCaptor.capture());
        Grade capturedGrade = gradeCaptor.getValue();

        assertEquals(10, capturedGrade.value());
        assertEquals("Good job!", capturedGrade.comment());
        assertEquals(author, capturedGrade.author());
        assertEquals(gradeType, capturedGrade.gradeType());
        assertEquals(student, capturedGrade.student());
    }

    @Test
    void mean_calculatesCorrectMeanForNonEmptyGrades() {
        GradeType gradeType = new GradeType();
        gradeType.factor(0.5f);
        Grade grade1 = new Grade();
        grade1.value(10.0f);
        grade1.gradeType(gradeType);
        Grade grade2 = new Grade();
        grade2.value(20.0f);
        grade2.gradeType(gradeType);
        List<Grade> grades = List.of(grade1, grade2);

        float mean = gradeService.mean(grades);

        assertEquals(15.0f, mean);
    }

    @Test
    void mean_returnsZeroForEmptyGrades() {
        List<Grade> grades = Collections.emptyList();

        float mean = gradeService.mean(grades);

        assertEquals(0.0f, mean);
    }

    @Test
    void mean_returnsZeroWhenAllGradeFactorsAreZero() {
        GradeType gradeType = new GradeType();
        gradeType.factor(0.0f);
        Grade grade1 = new Grade();
        grade1.value(10.0f);
        grade1.gradeType(gradeType);
        Grade grade2 = new Grade();
        grade2.value(20.0f);
        grade2.gradeType(gradeType);
        List<Grade> grades = List.of(grade1, grade2);

        float mean = gradeService.mean(grades);

        assertEquals(0.0f, mean);
    }

    @Test
    void getAverageGradesByGradeTypeByRoleType_returnsAverageForValidInputs() {
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
    void getAverageGradesByGradeTypeByRoleType_returnsNullForNoGrades() {
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
    void assignGradeToStudent_createsGradeForValidStudent() {
        String studentName = "John Doe";
        Integer value = 85;
        String gradeName = "Test Grade";
        Student student = new Student();
        GradeType gradeType = new GradeType();

        when(studentRepository.findByName(studentName)).thenReturn(student);
        when(gradeTypeRepository.findByName(gradeName)).thenReturn(gradeType);

        gradeService.assignGradeToStudent(studentName, value, gradeName);

        verify(gradeRepository, times(1)).save(any(Grade.class));
    }

    @Test
    void assignGradeToStudent_doesNotCreateGradeForInvalidStudent() {
        String studentName = "Invalid Student";
        Integer value = 85;
        String gradeName = "Test Grade";

        when(studentRepository.findByName(studentName)).thenReturn(null);

        gradeService.assignGradeToStudent(studentName, value, gradeName);

        verify(gradeRepository, times(0)).save(any(Grade.class));
    }

    @Test
    void assignGradeToTeam_createsGradeForValidTeam() {
        String teamName = "Team A";
        Integer value = 85;
        String gradeName = "Test Grade";
        int userId = 1;
        Team team = new Team();
        GradeType gradeType = new GradeType();
        User author = new User();

        when(teamRepository.findByName(teamName)).thenReturn(team);
        when(gradeTypeRepository.findByName(gradeName)).thenReturn(gradeType);
        when(userRepository.findById(userId)).thenReturn(Optional.of(author));

        gradeService.assignGradeToTeam(teamName, value, gradeName, userId);

        verify(gradeRepository, times(1)).save(any(Grade.class));
    }

    @Test
    void assignGradeToTeam_doesNotCreateGradeForInvalidTeam() {
        String teamName = "Invalid Team";
        Integer value = 85;
        String gradeName = "Test Grade";
        int userId = 1;

        when(teamRepository.findByName(teamName)).thenReturn(null);

        assertThrows(IllegalArgumentException.class, () -> gradeService.assignGradeToTeam(teamName, value, gradeName, userId));
        verify(gradeRepository, times(0)).save(any(Grade.class));
    }


    @Test
    void updateImportedMean_doesNotUpdateMeanForBachelorStudents() {
        Student student = new Student();
        student.id(1);
        student.bachelor(true);
        List<Student> students = List.of(student);

        when(studentRepository.findAll()).thenReturn(students);

        gradeService.updateImportedMean();

        verify(gradeRepository, times(0)).updateImportedMeanByStudentId(anyFloat(), anyInt());
    }

    @Test
    @DisplayName("getGradeByStudentAndGradeType returns grade for valid student and grade type")
    void getGradeByStudentAndGradeType_returnsGradeForValidStudentAndGradeType() {
        Student student = new Student();
        student.name("John Doe");
        GradeType gradeType = new GradeType();
        gradeType.name("Test Grade");

        when(gradeRepository.findValueByStudentAndGradeType(student, gradeType)).thenReturn(85.0f);

        Float actualGrade = gradeService.getGradeByStudentAndGradeType(student, gradeType);

        assertEquals(85.0f, actualGrade);
    }

    @Test
    @DisplayName("getGradeByStudentAndGradeType returns null for invalid student and grade type")
    void getGradeByStudentAndGradeType_returnsNullForInvalidStudentAndGradeType() {
        Student student = new Student();
        student.name("Invalid Student");
        GradeType gradeType = new GradeType();
        gradeType.name("Invalid Grade");

        when(gradeRepository.findValueByStudentAndGradeType(student, gradeType)).thenReturn(null);

        Float actualGrade = gradeService.getGradeByStudentAndGradeType(student, gradeType);

        assertNull(actualGrade);
    }

    @Test
    @DisplayName("getGradeByStudentAndGradeType handles NullPointerException")
    void getGradeByStudentAndGradeType_handlesNullPointerException() {
        Student student = new Student();
        student.name("John Doe");
        GradeType gradeType = new GradeType();
        gradeType.name("Test Grade");

        when(gradeRepository.findValueByStudentAndGradeType(student, gradeType)).thenThrow(new NullPointerException());

        Float actualGrade = gradeService.getGradeByStudentAndGradeType(student, gradeType);

        assertNull(actualGrade);
    }

}