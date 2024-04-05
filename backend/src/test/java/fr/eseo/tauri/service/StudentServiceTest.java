package fr.eseo.tauri.service;

import fr.eseo.tauri.model.Student;
import fr.eseo.tauri.model.Team;
import fr.eseo.tauri.repository.StudentRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.*;

import java.util.Arrays;
import java.util.List;

import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class StudentServiceTest {

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private TeamService teamService;

    @InjectMocks
    private StudentService studentService;

    @Test
    public void testGetStudentsByTeamId() {
        // Arrange
        Team team = new Team();
        Student student1 = new Student();
        Student student2 = new Student();
        when(teamService.getTeamById(1)).thenReturn(team);
        when(studentRepository.findStudentsByTeam(team)).thenReturn(Arrays.asList(student1, student2));

        // Act
        List<Student> students = studentService.getStudentsByTeamId(1);

        // Assert
        assertThat(students).hasSize(2);
        verify(teamService, times(1)).getTeamById(1);
        verify(studentRepository, times(1)).findStudentsByTeam(team);
    }

    @Test
    public void testGetStudentsByTeamIdReturnsEmptyList() {
        // Arrange
        Team team = new Team();
        when(teamService.getTeamById(1)).thenReturn(team);
        when(studentRepository.findStudentsByTeam(team)).thenReturn(Arrays.asList());

        // Act
        List<Student> students = studentService.getStudentsByTeamId(1);

        // Assert
        assertThat(students).isEmpty();
        verify(teamService, times(1)).getTeamById(1);
        verify(studentRepository, times(1)).findStudentsByTeam(team);
    }
}