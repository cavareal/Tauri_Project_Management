package fr.eseo.tauri.service;

import fr.eseo.tauri.model.*;
import fr.eseo.tauri.repository.GradeRepository;
import fr.eseo.tauri.repository.GradeTeamsRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;

class GradeServiceTest {

    @Mock
    private GradeRepository gradeRepository;

    @Mock
    private GradeTeamsRepository gradeTeamRepository;

    @InjectMocks
    private GradeService gradeService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void attributeGradeToTeam_savesGradeTeam() {
        Team team = new Team();
        Grade grade = new Grade();

        gradeService.attributeGradeToTeam(team, grade);

        verify(gradeTeamRepository, times(1)).save(any(GradeTeams.class));
    }

    @Test
    void createGrade_savesGrade() {
        User author = new User();
        GradeType gradeType = new GradeType();
        Student student = new Student();

        gradeService.createGrade(author, gradeType, student, 10.0, "Good job!");

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
}