package fr.eseo.tauri.service;

import fr.eseo.tauri.model.Student;
import fr.eseo.tauri.model.enumeration.Gender;
import fr.eseo.tauri.repository.StudentRepository;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@Nested
class StudentServiceTest {

        @Mock
        private StudentRepository studentRepository;

        @InjectMocks
        private StudentService studentService;

        @BeforeEach
        void setUp() {
            MockitoAnnotations.openMocks(this);
        }

        @Test
        @DisplayName("Test if the student service is correctly instantiated")
        void testStudentServiceInstantiation() {
            assertNotNull(studentService);
        }

        @Test
        @DisplayName("createStudent with valid data saves student")
        void createStudent_withValidData_savesStudent() {

                // Given
                Student student = new Student();
                student.name("John Doe");
                student.gender(Gender.MAN);
                student.bachelor(true);
                student.teamRole("Not assigned");
                student.project(null);
                student.team(null);
                student.password("password");
                student.privateKey("privateKey");
                student.email("john.doe@gmail.com");

                when(studentRepository.save(any(Student.class))).thenReturn(student);

                // When
                studentService.createStudent(student);

                // Then
                verify(studentRepository, times(1)).save(student);
        }
}