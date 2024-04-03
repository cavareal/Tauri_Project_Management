package fr.eseo.tauri.service;

import fr.eseo.tauri.model.Student;
import fr.eseo.tauri.model.enumeration.Gender;
import fr.eseo.tauri.repository.StudentRepository;
import org.junit.jupiter.api.*;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@Nested
class StudentServiceTest {

    @Mock
    private StudentRepository studentRepository;

    @InjectMocks
    private StudentService studentService;

    @InjectMocks
    private GradeService gradeService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @Order(1)
    @DisplayName("Test if the student service is correctly instantiated")
    void testStudentServiceInstantiation() {
        assertNotNull(studentService);
    }

    @Test
    @Order(2)
    @DisplayName("createStudent with valid data saves student")
    void createStudent_withValidData_savesStudent() {

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

        studentService.createStudent(student);

        verify(studentRepository, times(1)).save(student);
        verifyNoMoreInteractions(studentRepository);
    }

    @Test
    @Order(3)
    @DisplayName("createStudent with null name throws exception")
    void createStudent_withNullName_throwsException() {
        Student student = new Student();
        student.name(null);
        assertThrows(IllegalArgumentException.class, () -> studentService.createStudent(student));
    }

    @Test
    @Order(4)
    @DisplayName("getStudentQuantity returns correct quantity when students exist")
    void getStudentQuantity_returnsCorrectQuantity_whenStudentsExist() {
        List<Student> students = Arrays.asList(new Student(), new Student(), new Student());
        when(studentRepository.findAll()).thenReturn(students);

        Integer quantity = studentService.getStudentQuantity();

        assertEquals(3, quantity);
    }

    @Test
    @Order(5)
    @DisplayName("getStudentQuantity returns zero when no students exist")
    void getStudentQuantity_returnsZero_whenNoStudentsExist() {

        when(studentRepository.findAll()).thenReturn(new ArrayList<>());

        Integer quantity = studentService.getStudentQuantity();

        assertEquals(0, quantity);
    }

    @Test
    @Order(6)
    @DisplayName("populateDatabaseFromCsv populates database with students")
    void populateDatabaseFromCsv_populatesDatabaseWithStudents() throws IOException {

        MultipartFile multipartFile = mock(MultipartFile.class);
        byte[] content = "1,JOHN Doe,M,B,,,\n2,JANE Smith,F,,,,".getBytes();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(content);
        when(multipartFile.isEmpty()).thenReturn(false);
        when(multipartFile.getInputStream()).thenReturn(inputStream);

        ArgumentCaptor<Student> studentCaptor = ArgumentCaptor.forClass(Student.class);

        studentService.populateDatabaseFromCsv(multipartFile);

        verify(studentRepository, times(2)).save(studentCaptor.capture());
        verifyNoMoreInteractions(studentRepository);

        List<Student> capturedStudents = studentCaptor.getAllValues();

        assertEquals("JOHN Doe", capturedStudents.get(0).name());
        assertEquals("john.doe@reseau.eseo.fr", capturedStudents.get(0).email());
        assertEquals(Gender.MAN, capturedStudents.get(0).gender());
        assertTrue(capturedStudents.get(0).bachelor());

        assertEquals("JANE Smith", capturedStudents.get(1).name());
        assertEquals("jane.smith@reseau.eseo.fr", capturedStudents.get(1).email());
        assertEquals(Gender.WOMAN, capturedStudents.get(1).gender());
        assertFalse(capturedStudents.get(1).bachelor());
    }

    @Test
    @Order(7)
    @DisplayName("populateDatabaseFromCsv handles empty file")
    void populateDatabaseFromCsv_handlesEmptyFile() throws IOException {
        MultipartFile multipartFile = mock(MultipartFile.class);
        when(multipartFile.isEmpty()).thenReturn(true);

        studentService.populateDatabaseFromCsv(multipartFile);

        verifyNoInteractions(studentRepository);
    }

    @Test
    @DisplayName("createStudentFromData creates student with valid data")
    @Order(8)
    void createStudentFromData_createsStudentWithValidData() {
        Student student = studentService.createStudentFromData("John Doe", "M", "B");

        assertEquals("John Doe", student.name());
        assertEquals(Gender.MAN, student.gender());
        assertTrue(student.bachelor());
        assertEquals("Not assigned", student.teamRole());
        assertNull(student.project());
        assertNull(student.team());
        assertEquals("password", student.password());
        assertEquals("privateKey", student.privateKey());
        assertEquals("john.doe@reseau.eseo.fr", student.email());
    }

    @Test
    @Order(9)
    @DisplayName("createStudentFromData throws exception when name is null")
    void createStudentFromData_throwsExceptionWhenNameIsNull() {
        assertThrows(IllegalArgumentException.class, () -> studentService.createStudentFromData(null, "M", "B"));
    }

    @Test
    @Order(10)
    @DisplayName("createStudentFromData throws exception when name is empty")
    void createStudentFromData_throwsExceptionWhenNameIsEmpty() {
        assertThrows(IllegalArgumentException.class, () -> studentService.createStudentFromData("", "M", "B"));
    }

    @Test
    @Order(11)
    @DisplayName("createStudentFromData throws exception when gender is null")
    void createStudentFromData_throwsExceptionWhenGenderIsNull() {
        assertThrows(IllegalArgumentException.class, () -> studentService.createStudentFromData("John Doe", null, "B"));
    }

    @Test
    @Order(12)
    @DisplayName("createStudentFromData throws exception when gender is empty")
    void createStudentFromData_throwsExceptionWhenGenderIsEmpty() {
        assertThrows(IllegalArgumentException.class, () -> studentService.createStudentFromData("John Doe", "", "B"));
    }

    @Test
    @Order(13)
    @DisplayName("createStudentFromData throws exception when bachelor is null")
    void createStudentFromData_throwsExceptionWhenBachelorIsNull() {
        assertThrows(IllegalArgumentException.class, () -> studentService.createStudentFromData("John Doe", "M", null));
    }


    @Test
    @Order(14)
    @DisplayName("isEmpty returns true when line is empty at index")
    void isEmpty_returnsTrue_whenLineIsEmptyAtIndex() {
        String[] line = {"", "test", "example"};
        assertFalse(StudentService.hasNonEmptyValue(line, 0));
    }

    @Test
    @Order(15)
    @DisplayName("isEmpty returns false when line is not empty at index")
    void isEmpty_returnsFalse_whenLineIsNotEmptyAtIndex() {
        String[] line = {"test", "example"};
        assertTrue(StudentService.hasNonEmptyValue(line, 0));
    }

    @Test
    @Order(16)
    @DisplayName("isEmpty returns true when index is out of bounds")
    void isEmpty_returnsTrue_whenIndexIsOutOfBounds() {
        String[] line = {"test", "example"};
        assertFalse(StudentService.hasNonEmptyValue(line, 3));
    }
}