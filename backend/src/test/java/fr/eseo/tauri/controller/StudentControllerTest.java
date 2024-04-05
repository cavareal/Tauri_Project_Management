package fr.eseo.tauri.controller;

import fr.eseo.tauri.model.Student;
import fr.eseo.tauri.repository.StudentRepository;
import fr.eseo.tauri.service.AuthService;
import fr.eseo.tauri.service.StudentService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@SpringBootTest
class StudentControllerTest {

    @Mock
    private StudentService studentService;

    @Mock
    private AuthService authService;

    @InjectMocks
    private StudentController studentController;

    @Test
    void testGetStudentsByTeam() {
        // Arrange
        Student student1 = new Student();
        Student student2 = new Student();
        when(studentService.getStudentsByTeamId(1)).thenReturn(Arrays.asList(student1, student2));
        when(authService.checkAuth(anyString(), anyString())).thenReturn(true);

        // Act
        ResponseEntity<List<Student>> response = studentController.getStudentsByTeam("mockToken", 1);

        // Assert
        assertThat(response.getBody()).hasSize(2);
        verify(studentService, times(1)).getStudentsByTeamId(1);
        verify(authService, times(1)).checkAuth(anyString(), anyString());
    }

    @Test
    void testGetStudentsByTeamReturnsInternalServerError() {
        // Arrange
        when(studentService.getStudentsByTeamId(1)).thenThrow(new RuntimeException("Unexpected error"));
        when(authService.checkAuth(anyString(), anyString())).thenReturn(true);

        // Act
        ResponseEntity<List<Student>> response = studentController.getStudentsByTeam("mockToken", 1);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        verify(studentService, times(1)).getStudentsByTeamId(1);
        verify(authService, times(1)).checkAuth(anyString(), anyString());
    }

    @Test
    void testGetStudentsByTeamReturnsUnauthorized() {
        // Arrange
        when(authService.checkAuth(anyString(), anyString())).thenReturn(false);

        // Act
        ResponseEntity<List<Student>> response = studentController.getStudentsByTeam("mockToken", 1);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
        verify(authService, times(1)).checkAuth(anyString(), anyString());
        verify(studentService, never()).getStudentsByTeamId(anyInt());
    }
}