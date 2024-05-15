package fr.eseo.tauri.service;

import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvValidationException;
import fr.eseo.tauri.exception.EmptyResourceException;
import fr.eseo.tauri.exception.ResourceNotFoundException;
import fr.eseo.tauri.model.*;
import fr.eseo.tauri.model.enumeration.Gender;
import fr.eseo.tauri.model.enumeration.GradeTypeName;
import fr.eseo.tauri.repository.BonusRepository;
import fr.eseo.tauri.repository.StudentRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.*;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@Nested
class StudentServiceTest {

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private GradeTypeService gradeTypeService;

    @Mock
    private GradeService gradeService;

    @Mock
    private TeamService teamService;

    @Mock
    private AuthService authService;

    @Mock
    private ProjectService projectService;

    @Mock
    private SprintService sprintService;

    @Mock
    private PresentationOrderService presentationOrderService;

    @Mock
    private RoleService roleService;

    @Mock
    private BonusRepository bonusRepository;

    @InjectMocks
    private StudentService studentService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getStudentByIdShouldReturnStudentWhenAuthorizedAndIdExists() {
        Student student = new Student();
        when(authService.checkAuth(anyString(), eq("readStudent"))).thenReturn(true);
        when(studentRepository.findById(anyInt())).thenReturn(Optional.of(student));

        Student result = studentService.getStudentById("token", 1);

        assertEquals(student, result);
    }

    @Test
    void getStudentByIdShouldThrowSecurityExceptionWhenUnauthorized() {
        when(authService.checkAuth(anyString(), eq("readStudent"))).thenReturn(false);

        assertThrows(SecurityException.class, () -> studentService.getStudentById("token", 1));
    }

    @Test
    void getStudentByIdShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() {
        when(authService.checkAuth(anyString(), eq("readStudent"))).thenReturn(true);
        when(studentRepository.findById(anyInt())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> studentService.getStudentById("token", 1));
    }

    @Test
    void getAllStudentsByProjectShouldReturnStudentsWhenAuthorized() {
        when(authService.checkAuth(anyString(), eq("readStudents"))).thenReturn(true);
        when(studentRepository.findAllByProject(anyInt())).thenReturn(Arrays.asList(new Student(), new Student()));

        List<Student> result = studentService.getAllStudentsByProject("token", 1);

        assertEquals(2, result.size());
    }

    @Test
    void getAllStudentsByProjectShouldThrowSecurityExceptionWhenUnauthorized() {
        when(authService.checkAuth(anyString(), eq("readStudents"))).thenReturn(false);

        assertThrows(SecurityException.class, () -> studentService.getAllStudentsByProject("token", 1));
    }

    @Test
    void getAllStudentsByProjectShouldReturnEmptyListWhenNoStudentsExist() {
        when(authService.checkAuth(anyString(), eq("readStudents"))).thenReturn(true);
        when(studentRepository.findAllByProject(anyInt())).thenReturn(Collections.emptyList());

        List<Student> result = studentService.getAllStudentsByProject("token", 1);

        assertTrue(result.isEmpty());
    }

    @Test
    void createStudentShouldSaveWhenAuthorizedAndProjectAndTeamExist() {
        when(authService.checkAuth(anyString(), eq("addStudent"))).thenReturn(true);
        when(projectService.getProjectById(anyString(), anyInt())).thenReturn(new Project());
        when(teamService.getTeamById(anyString(), anyInt())).thenReturn(new Team());
        when(sprintService.getAllSprintsByProject(anyString(), anyInt())).thenReturn(Collections.emptyList());

        Student student = new Student();
        student.projectId(1);
        student.teamId(1);
        studentService.createStudent("token", student);

        verify(studentRepository, times(1)).save(student);
        verify(roleService, times(1)).createRole(anyString(), any(Role.class));
    }


    @Test
    void deleteStudentShouldThrowSecurityExceptionWhenUnauthorized() {
        when(authService.checkAuth(anyString(), eq("deleteStudent"))).thenReturn(false);

        assertThrows(SecurityException.class, () -> studentService.deleteStudent("token", 1));
    }

    // @Test
    // void deleteAllStudentsByProjectShouldDeleteWhenAuthorizedAndProjectExists() {
    //     when(authService.checkAuth(anyString(), eq("deleteStudent"))).thenReturn(true);

    //     studentService.deleteAllStudentsByProject("token", 1);

    //     verify(studentRepository, times(1)).deleteAllByProject(1);
    //     verify(gradeTypeService, times(1)).deleteAllImportedGradeTypes("token");
    // }

    @Test
    void deleteAllStudentsByProjectShouldThrowSecurityExceptionWhenUnauthorized() {
        when(authService.checkAuth(anyString(), eq("deleteStudent"))).thenReturn(false);

        assertThrows(SecurityException.class, () -> studentService.deleteAllStudentsByProject("token", 1));
    }

    @Test
    void extractNamesGenderBachelorAndGradesShouldReturnCorrectData() throws IOException, CsvValidationException {
        String csvContent = "1,John Doe,M,B,15,14,13\n2,Jane Doe,F,B,12,13,14";
        InputStream inputStream = new ByteArrayInputStream(csvContent.getBytes());

        Map<String, Object> result = studentService.extractNamesGenderBachelorAndGrades(inputStream);

        List<String> names = (List<String>) result.get(StudentService.MAP_KEY_NAMES);
        List<String> genders = (List<String>) result.get(StudentService.MAP_KEY_GENDERS);
        List<String> bachelors = (List<String>) result.get(StudentService.MAP_KEY_BACHELORS);
        List<List<String>> grades = (List<List<String>>) result.get(StudentService.MAP_KEY_GRADES);

        assertEquals(Arrays.asList("John Doe", "Jane Doe"), names);
        assertEquals(Arrays.asList("M", "F"), genders);
        assertEquals(Arrays.asList("B", "B"), bachelors);
        assertEquals(Arrays.asList(Arrays.asList("15", "14", "13"), Arrays.asList("12", "13", "14")), grades);
    }

    @Test
    void extractNamesGenderBachelorAndGradesShouldHandleEmptyInputStream() throws IOException, CsvValidationException {
        InputStream inputStream = new ByteArrayInputStream("".getBytes());

        Map<String, Object> result = studentService.extractNamesGenderBachelorAndGrades(inputStream);

        List<String> names = (List<String>) result.get(StudentService.MAP_KEY_NAMES);
        List<String> genders = (List<String>) result.get(StudentService.MAP_KEY_GENDERS);
        List<String> bachelors = (List<String>) result.get(StudentService.MAP_KEY_BACHELORS);
        List<List<String>> grades = (List<List<String>>) result.get(StudentService.MAP_KEY_GRADES);

        assertTrue(names.isEmpty());
        assertTrue(genders.isEmpty());
        assertTrue(bachelors.isEmpty());
        assertTrue(grades.isEmpty());
    }

    @Test
    void extractNamesGenderBachelorAndGradesShouldHandleInputStreamWithMissingData() throws IOException, CsvValidationException {
        String csvContent = "1,John Doe,M,,15,14,13\n2,Jane Doe,,B,12,13,14";
        InputStream inputStream = new ByteArrayInputStream(csvContent.getBytes());

        Map<String, Object> result = studentService.extractNamesGenderBachelorAndGrades(inputStream);

        List<String> names = (List<String>) result.get(StudentService.MAP_KEY_NAMES);
        List<String> genders = (List<String>) result.get(StudentService.MAP_KEY_GENDERS);
        List<String> bachelors = (List<String>) result.get(StudentService.MAP_KEY_BACHELORS);
        List<List<String>> grades = (List<List<String>>) result.get(StudentService.MAP_KEY_GRADES);

        assertEquals(Arrays.asList("John Doe", "Jane Doe"), names);
        assertEquals(Arrays.asList("M", ""), genders);
        assertEquals(Arrays.asList("", "B"), bachelors);
        assertEquals(Arrays.asList(Arrays.asList("15", "14", "13"), Arrays.asList("12", "13", "14")), grades);
    }

    @Test
    void hasNonEmptyValueReturnsTrueWhenIndexContainsNonEmptyValue() {
        String[] line = {"test", "example"};
        assertTrue(StudentService.hasNonEmptyValue(line, 0));
    }

    @Test
    void hasNonEmptyValueReturnsFalseWhenIndexContainsEmptyValue() {
        String[] line = {"", "test", "example"};
        assertFalse(StudentService.hasNonEmptyValue(line, 0));
    }

    @Test
    void hasNonEmptyValueReturnsFalseWhenIndexIsOutOfBounds() {
        String[] line = {"test", "example"};
        assertFalse(StudentService.hasNonEmptyValue(line, 3));
    }

    @Test
    void hasNonEmptyValueReturnsFalseWhenLineIsEmpty() {
        String[] line = {};
        assertFalse(StudentService.hasNonEmptyValue(line, 0));
    }


    @Test
    void createStudentFromDataShouldThrowExceptionWhenNameIsNull() {
        assertThrows(IllegalArgumentException.class, () -> studentService.createStudentFromData("token", null, "M", "B", 1));
    }

    @Test
    void createStudentFromDataShouldThrowExceptionWhenNameIsEmpty() {
        assertThrows(IllegalArgumentException.class, () -> studentService.createStudentFromData("token", "", "M", "B", 1));
    }

    @Test
    void createStudentFromDataShouldThrowExceptionWhenGenderIsNull() {
        assertThrows(IllegalArgumentException.class, () -> studentService.createStudentFromData("token", "John Doe", null, "B", 1));
    }

    @Test
    void createStudentFromDataShouldThrowExceptionWhenGenderIsEmpty() {
        assertThrows(IllegalArgumentException.class, () -> studentService.createStudentFromData("token", "John Doe", "", "B", 1));
    }

    @Test
    void createStudentFromDataShouldThrowExceptionWhenBachelorIsNull() {
        assertThrows(IllegalArgumentException.class, () -> studentService.createStudentFromData("token", "John Doe", "M", null, 1));
    }

    @Test
    void populateDatabaseFromCSVShouldThrowSecurityExceptionWhenUnauthorized() {
        MultipartFile file = mock(MultipartFile.class);
        when(authService.checkAuth(anyString(), eq("addStudent"))).thenReturn(false);

        assertThrows(SecurityException.class, () -> studentService.populateDatabaseFromCSV("token", file, 1));
    }

    @Test
    void populateDatabaseFromCSVShouldThrowEmptyResourceExceptionWhenFileIsEmpty() {
        MultipartFile file = mock(MultipartFile.class);
        when(file.isEmpty()).thenReturn(true);
        when(authService.checkAuth(anyString(), eq("addStudent"))).thenReturn(true);

        assertThrows(EmptyResourceException.class, () -> studentService.populateDatabaseFromCSV("token", file, 1));
    }

    @Test
    void createStudentsCSVShouldThrowSecurityExceptionWhenUnauthorized() throws IOException {
        when(authService.checkAuth(anyString(), eq("exportStudents"))).thenReturn(false);

        assertThrows(SecurityException.class, () -> studentService.createStudentsCSV("token", 1));
    }

    @Test
    void writeHeadersShouldSkipAverageGradeType() {
        StringWriter stringWriter = new StringWriter();
        CSVWriter csvWriter = new CSVWriter(stringWriter);

        GradeType gradeType1 = new GradeType();
        gradeType1.name("GradeType1");
        gradeType1.factor(1.0f);

        GradeType gradeType2 = new GradeType();
        gradeType2.name(GradeTypeName.AVERAGE.displayName());
        gradeType2.factor(2.0f);

        studentService.writeHeaders(csvWriter, Arrays.asList(gradeType1, gradeType2));

        String expectedCsv = """
                "","","","","","1.0"
                "","","sexe M / F","","","GradeType1"
                """;
        String actualCsv = stringWriter.toString();

        assertEquals(expectedCsv, actualCsv);
    }

    @Test
    void writeHeadersShouldHandleEmptyGradeTypes() {
        StringWriter stringWriter = new StringWriter();
        CSVWriter csvWriter = new CSVWriter(stringWriter);

        studentService.writeHeaders(csvWriter, Collections.emptyList());

        String expectedCsv = """
                "","","",""
                "","","sexe M / F",""
                """;
        String actualCsv = stringWriter.toString();

        assertEquals(expectedCsv, actualCsv);
    }

    @Test
    void writeStudentDataShouldWriteCorrectDataForSingleStudent() {
        StringWriter stringWriter = new StringWriter();
        CSVWriter csvWriter = new CSVWriter(stringWriter);

        Student student = new Student();
        student.name("John Doe");
        student.gender(Gender.MAN);
        student.bachelor(true);

        GradeType gradeType = new GradeType();
        gradeType.name("GradeType1");

        when(gradeService.getGradeByStudentAndGradeType(student, gradeType)).thenReturn(15.0f);

        studentService.writeStudentData(csvWriter, List.of(student), List.of(gradeType));

        String expectedCsv = "\"1\",\"John Doe\",\"M\",\"B\",\"15.0\"\n";
        String actualCsv = stringWriter.toString();

        assertEquals(expectedCsv, actualCsv);
    }

    @Test
    void writeStudentDataShouldWriteCorrectDataForMultipleStudents() {
        StringWriter stringWriter = new StringWriter();
        CSVWriter csvWriter = new CSVWriter(stringWriter);

        Student student1 = new Student();
        student1.name("John Doe");
        student1.gender(Gender.MAN);
        student1.bachelor(true);

        Student student2 = new Student();
        student2.name("Jane Doe");
        student2.gender(Gender.WOMAN);
        student2.bachelor(false);

        GradeType gradeType = new GradeType();
        gradeType.name("GradeType1");

        when(gradeService.getGradeByStudentAndGradeType(student1, gradeType)).thenReturn(15.0f);
        when(gradeService.getGradeByStudentAndGradeType(student2, gradeType)).thenReturn(14.0f);

        studentService.writeStudentData(csvWriter, Arrays.asList(student1, student2), List.of(gradeType));

        String expectedCsv = """
                "1","John Doe","M","B","15.0"
                "2","Jane Doe","F","","14.0"
                """;
        String actualCsv = stringWriter.toString();

        assertEquals(expectedCsv, actualCsv);
    }

    @Test
    void writeStudentDataShouldHandleNullGrade() {
        StringWriter stringWriter = new StringWriter();
        CSVWriter csvWriter = new CSVWriter(stringWriter);

        Student student = new Student();
        student.name("John Doe");
        student.gender(Gender.MAN);
        student.bachelor(true);

        GradeType gradeType = new GradeType();
        gradeType.name("GradeType1");

        when(gradeService.getGradeByStudentAndGradeType(student, gradeType)).thenReturn(null);

        studentService.writeStudentData(csvWriter, List.of(student), List.of(gradeType));

        String expectedCsv = "\"1\",\"John Doe\",\"M\",\"B\",\"\"\n";
        String actualCsv = stringWriter.toString();

        assertEquals(expectedCsv, actualCsv);
    }

    @Test
    void writeSummaryDataShouldWriteCorrectDataWithValidInput() {
        StringWriter stringWriter = new StringWriter();
        CSVWriter csvWriter = new CSVWriter(stringWriter);

        when(studentRepository.countWomen()).thenReturn(5);
        when(studentRepository.countTotal()).thenReturn(10);
        when(studentRepository.countBachelor()).thenReturn(3);

        studentService.writeSummaryData(csvWriter, 2);

        String expectedCsv = """
                "","","","","",""
                "","","","","",""
                "","","","","",""
                "","","","","",""
                "","Nombre F","5","","",""
                "","Nombre M","5","","",""
                "","Nombre B","","3","",""
                """;
        String actualCsv = stringWriter.toString();

        assertEquals(expectedCsv, actualCsv);
    }

    @Test
    void writeSummaryDataShouldHandleZeroGrades() {
        StringWriter stringWriter = new StringWriter();
        CSVWriter csvWriter = new CSVWriter(stringWriter);

        when(studentRepository.countWomen()).thenReturn(0);
        when(studentRepository.countTotal()).thenReturn(0);
        when(studentRepository.countBachelor()).thenReturn(0);

        studentService.writeSummaryData(csvWriter, 0);

        String expectedCsv = """
                "","","",""
                "","","",""
                "","","",""
                "","","",""
                "","Nombre F","0",""
                "","Nombre M","0",""
                "","Nombre B","","0"
                """;
        String actualCsv = stringWriter.toString();

        assertEquals(expectedCsv, actualCsv);
    }

    @ParameterizedTest
    @MethodSource("provideParametersForWriteEmptyRowsTest")
    void writeEmptyRowsTest(int rows, int rowLength, String expectedCsv) {
        StringWriter stringWriter = new StringWriter();
        CSVWriter csvWriter = new CSVWriter(stringWriter);

        studentService.writeEmptyRows(csvWriter, rows, rowLength);

        String actualCsv = stringWriter.toString();

        assertEquals(expectedCsv, actualCsv);
    }

    private static Stream<Arguments> provideParametersForWriteEmptyRowsTest() {
        return Stream.of(
                Arguments.of(3, 2, "\"\",\"\"\n\"\",\"\"\n\"\",\"\"\n"),
                Arguments.of(0, 2, ""),
                Arguments.of(3, 0, "\n\n\n")
        );
    }


    @ParameterizedTest
    @MethodSource("provideParametersForWriteCountRowTest")
    void writeCountRowTest(String label, int count, String expectedCsv) {
        StringWriter stringWriter = new StringWriter();
        CSVWriter csvWriter = new CSVWriter(stringWriter);

        studentService.writeCountRow(csvWriter, label, count, 3);

        String actualCsv = stringWriter.toString();

        assertEquals(expectedCsv, actualCsv);
    }

    private static Stream<Arguments> provideParametersForWriteCountRowTest() {
        return Stream.of(
                Arguments.of("Nombre F", 5, "\"\",\"Nombre F\",\"5\"\n"),
                Arguments.of("Nombre F", 0, "\"\",\"Nombre F\",\"0\"\n"),
                Arguments.of("", 5, "\"\",\"\",\"5\"\n")
        );
    }
}