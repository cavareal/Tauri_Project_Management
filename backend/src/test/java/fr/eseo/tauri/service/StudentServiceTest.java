package fr.eseo.tauri.service;

import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvValidationException;
import fr.eseo.tauri.exception.EmptyResourceException;
import fr.eseo.tauri.exception.ResourceNotFoundException;
import fr.eseo.tauri.model.*;
import fr.eseo.tauri.model.enumeration.Gender;
import fr.eseo.tauri.repository.StudentRepository;
import org.junit.jupiter.api.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.*;

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
    void createStudentShouldThrowSecurityExceptionWhenUnauthorized() {
        when(authService.checkAuth(anyString(), eq("addStudent"))).thenReturn(false);

        assertThrows(SecurityException.class, () -> studentService.createStudent("token", new Student()));
    }

    @Test
    void createStudentShouldCreatePresentationOrderWhenSprintsExist() {
        when(authService.checkAuth(anyString(), eq("addStudent"))).thenReturn(true);
        when(projectService.getProjectById(anyString(), anyInt())).thenReturn(new Project());
        when(teamService.getTeamById(anyString(), anyInt())).thenReturn(new Team());
        when(sprintService.getAllSprintsByProject(anyString(), anyInt())).thenReturn(Arrays.asList(new Sprint(), new Sprint()));

        Student student = new Student();
        student.projectId(1);
        student.teamId(1);
        studentService.createStudent("token", student);

        verify(presentationOrderService, times(2)).createPresentationOrder(anyString(), any(PresentationOrder.class));
    }


    @Test
    void updateStudentShouldThrowSecurityExceptionWhenUnauthorized() {
        when(authService.checkAuth(anyString(), eq("updateStudent"))).thenReturn(false);

        assertThrows(SecurityException.class, () -> studentService.updateStudent("token", 1, new Student()));
    }


    @Test
    void deleteStudentShouldThrowSecurityExceptionWhenUnauthorized() {
        when(authService.checkAuth(anyString(), eq("deleteStudent"))).thenReturn(false);

        assertThrows(SecurityException.class, () -> studentService.deleteStudent("token", 1));
    }

    @Test
    void deleteAllStudentsByProjectShouldDeleteWhenAuthorizedAndProjectExists() {
        when(authService.checkAuth(anyString(), eq("deleteStudent"))).thenReturn(true);

        studentService.deleteAllStudentsByProject("token", 1);

        verify(studentRepository, times(1)).deleteAllByProject(1);
        verify(gradeTypeService, times(1)).deleteAllImportedGradeTypes("token");
    }

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
    void createStudentFromDataShouldCreateStudentWithValidData() {
        Student result = studentService.createStudentFromData("token", "John Doe", "M", "B", 1);

        assertEquals("John Doe", result.name());
        assertEquals(Gender.MAN, result.gender());
        assertTrue(result.bachelor());
        assertEquals("Not assigned", result.teamRole());
        assertEquals("password", result.password());
        assertEquals("privateKey", result.privateKey());
        assertEquals("john.doe@reseau.eseo.fr", result.email());
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
    void populateDatabaseFromCSVShouldPopulateDatabaseWithValidData() throws IOException, CsvValidationException {
        MultipartFile file = mock(MultipartFile.class);
        InputStream inputStream = new ByteArrayInputStream("1,John Doe,M,B,15,14,13\n2,Jane Doe,F,B,12,13,14".getBytes());
        when(file.getInputStream()).thenReturn(inputStream);
        when(file.isEmpty()).thenReturn(false);
        when(authService.checkAuth(anyString(), eq("addStudent"))).thenReturn(true);
        when(gradeTypeService.createGradeTypesFromCSV(anyString(), any(InputStream.class))).thenReturn(Collections.emptyList());

        studentService.populateDatabaseFromCSV("token", file, 1);

        verify(studentRepository, times(2)).save(any(Student.class));
        verify(gradeService, times(2)).createGradesFromGradeTypesAndValues(any(Student.class), anyList(), anyList(), anyString());
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
        gradeType2.name("AVERAGE");
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

    @Test
    void writeEmptyRowsShouldWriteCorrectNumberOfEmptyRows() {
        StringWriter stringWriter = new StringWriter();
        CSVWriter csvWriter = new CSVWriter(stringWriter);

        studentService.writeEmptyRows(csvWriter, 3, 2);

        String expectedCsv = "\"\",\"\"\n\"\",\"\"\n\"\",\"\"\n";
        String actualCsv = stringWriter.toString();

        assertEquals(expectedCsv, actualCsv);
    }

    @Test
    void writeEmptyRowsShouldHandleZeroRows() {
        StringWriter stringWriter = new StringWriter();
        CSVWriter csvWriter = new CSVWriter(stringWriter);

        studentService.writeEmptyRows(csvWriter, 0, 2);

        String expectedCsv = "";
        String actualCsv = stringWriter.toString();

        assertEquals(expectedCsv, actualCsv);
    }

    @Test
    void writeEmptyRowsShouldHandleZeroRowLength() {
        StringWriter stringWriter = new StringWriter();
        CSVWriter csvWriter = new CSVWriter(stringWriter);

        studentService.writeEmptyRows(csvWriter, 3, 0);

        String expectedCsv = "\n\n\n";
        String actualCsv = stringWriter.toString();

        assertEquals(expectedCsv, actualCsv);
    }

    @Test
    void writeCountRowShouldWriteCorrectDataWithValidInput() {
        StringWriter stringWriter = new StringWriter();
        CSVWriter csvWriter = new CSVWriter(stringWriter);

        studentService.writeCountRow(csvWriter, "Nombre F", 5, 3);

        String expectedCsv = "\"\",\"Nombre F\",\"5\"\n";
        String actualCsv = stringWriter.toString();

        assertEquals(expectedCsv, actualCsv);
    }

    @Test
    void writeCountRowShouldHandleZeroCount() {
        StringWriter stringWriter = new StringWriter();
        CSVWriter csvWriter = new CSVWriter(stringWriter);

        studentService.writeCountRow(csvWriter, "Nombre F", 0, 3);

        String expectedCsv = "\"\",\"Nombre F\",\"0\"\n";
        String actualCsv = stringWriter.toString();

        assertEquals(expectedCsv, actualCsv);
    }

    @Test
    void writeCountRowShouldHandleEmptyLabel() {
        StringWriter stringWriter = new StringWriter();
        CSVWriter csvWriter = new CSVWriter(stringWriter);

        studentService.writeCountRow(csvWriter, "", 5, 3);

        String expectedCsv = "\"\",\"\",\"5\"\n";
        String actualCsv = stringWriter.toString();

        assertEquals(expectedCsv, actualCsv);
    }


    /*@Test
    @DisplayName("Test if the student service is correctly instantiated")
    void testStudentServiceInstantiation() {
        assertNotNull(studentService);
    }

    @Test
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
    @DisplayName("createStudent with null name throws exception")
    void createStudent_withNullName_throwsException() {
        Student student = new Student();
        student.name(null);
        assertThrows(IllegalArgumentException.class, () -> studentService.createStudent(student));
    }

    @Test
    @DisplayName("getStudentQuantity returns correct quantity when students exist")
    void getStudentQuantity_returnsCorrectQuantity_whenStudentsExist() {
        List<Student> students = Arrays.asList(new Student(), new Student(), new Student());
        when(studentRepository.findAll()).thenReturn(students);

        Integer quantity = studentService.getStudentQuantity();

        assertEquals(3, quantity);
    }

    @Test
    @DisplayName("getStudentQuantity returns zero when no students exist")
    void getStudentQuantity_returnsZero_whenNoStudentsExist() {

        when(studentRepository.findAll()).thenReturn(new ArrayList<>());

        Integer quantity = studentService.getStudentQuantity();

        assertEquals(0, quantity);
    }

    @Test
    @DisplayName("createStudentFromData throws exception when name is null")
    void createStudentFromData_throwsExceptionWhenNameIsNull() {
        assertThrows(IllegalArgumentException.class, () -> studentService.createStudentFromData(null, "M", "B"));
    }

    @Test
    @DisplayName("createStudentFromData throws exception when name is empty")
    void createStudentFromData_throwsExceptionWhenNameIsEmpty() {
        assertThrows(IllegalArgumentException.class, () -> studentService.createStudentFromData("", "M", "B"));
    }

    @Test
    @DisplayName("createStudentFromData throws exception when gender is null")
    void createStudentFromData_throwsExceptionWhenGenderIsNull() {
        assertThrows(IllegalArgumentException.class, () -> studentService.createStudentFromData("John Doe", null, "B"));
    }

    @Test
    @DisplayName("createStudentFromData throws exception when gender is empty")
    void createStudentFromData_throwsExceptionWhenGenderIsEmpty() {
        assertThrows(IllegalArgumentException.class, () -> studentService.createStudentFromData("John Doe", "", "B"));
    }

    @Test
    @DisplayName("createStudentFromData throws exception when bachelor is null")
    void createStudentFromData_throwsExceptionWhenBachelorIsNull() {
        assertThrows(IllegalArgumentException.class, () -> studentService.createStudentFromData("John Doe", "M", null));
    }


    @Test
    @DisplayName("isEmpty returns true when line is empty at index")
    void isEmpty_returnsTrue_whenLineIsEmptyAtIndex() {
        String[] line = {"", "test", "example"};
        assertFalse(StudentService.hasNonEmptyValue(line, 0));
    }

    @Test
    @DisplayName("isEmpty returns false when line is not empty at index")
    void isEmpty_returnsFalse_whenLineIsNotEmptyAtIndex() {
        String[] line = {"test", "example"};
        assertTrue(StudentService.hasNonEmptyValue(line, 0));
    }

    @Test
    @DisplayName("isEmpty returns true when index is out of bounds")
    void isEmpty_returnsTrue_whenIndexIsOutOfBounds() {
        String[] line = {"test", "example"};
        assertFalse(StudentService.hasNonEmptyValue(line, 3));
    }

    @Test
    @DisplayName("createStudentFromData creates student with valid data")
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
    @DisplayName("createStudentFromData creates student with empty bachelor status")
    void createStudentFromData_createsStudentWithEmptyBachelorStatus() {
        Student student = studentService.createStudentFromData("John Doe", "M", "");

        assertEquals("John Doe", student.name());
        assertEquals(Gender.MAN, student.gender());
        assertFalse(student.bachelor());
        assertEquals("Not assigned", student.teamRole());
        assertNull(student.project());
        assertNull(student.team());
        assertEquals("password", student.password());
        assertEquals("privateKey", student.privateKey());
        assertEquals("john.doe@reseau.eseo.fr", student.email());
    }

    @Test
    @DisplayName("createStudentFromData creates student with female gender")
    void createStudentFromData_createsStudentWithFemaleGender() {
        Student student = studentService.createStudentFromData("Jane Doe", "F", "B");

        assertEquals("Jane Doe", student.name());
        assertEquals(Gender.WOMAN, student.gender());
        assertTrue(student.bachelor());
        assertEquals("Not assigned", student.teamRole());
        assertNull(student.project());
        assertNull(student.team());
        assertEquals("password", student.password());
        assertEquals("privateKey", student.privateKey());
        assertEquals("jane.doe@reseau.eseo.fr", student.email());
    }

    @Test
    @DisplayName("extractNamesGenderBachelorAndGrades extracts valid data from input stream")
    @SuppressWarnings("unchecked")
    void extractNamesGenderBachelorAndGrades_extractsValidDataFromInputStream() {
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
    @DisplayName("extractNamesGenderBachelorAndGrades handles empty input stream")
    @SuppressWarnings("unchecked")
    void extractNamesGenderBachelorAndGrades_handlesEmptyInputStream() {
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
    @DisplayName("extractNamesGenderBachelorAndGrades handles input stream with missing data")
    @SuppressWarnings("unchecked")
    void extractNamesGenderBachelorAndGrades_handlesInputStreamWithMissingData() {
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
    @DisplayName("populateDatabaseFromCSV populates database with students from valid CSV file")
    void populateDatabaseFromCSV_populatesDatabaseWithStudentsFromValidCSVFile() throws IOException {
        MultipartFile multipartFile = mock(MultipartFile.class);
        byte[] content = "1,JOHN Doe,M,B,,,\n2,JANE Smith,F,,,,".getBytes();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(content);
        when(multipartFile.isEmpty()).thenReturn(false);
        when(multipartFile.getInputStream()).thenReturn(inputStream);

        ArgumentCaptor<Student> studentCaptor = ArgumentCaptor.forClass(Student.class);

        studentService.populateDatabaseFromCSV(multipartFile);

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
    @DisplayName("populateDatabaseFromCSV handles empty file")
    void populateDatabaseFromCSV_handlesEmptyFile() {
        MultipartFile multipartFile = mock(MultipartFile.class);
        when(multipartFile.isEmpty()).thenReturn(true);

        studentService.populateDatabaseFromCSV(multipartFile);

        verifyNoInteractions(studentRepository);
    }

    @Test
    void testGetStudentsByTeamId() {
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
    void testGetStudentsByTeamIdReturnsEmptyList() {
        // Arrange
        Team team = new Team();
        when(teamService.getTeamById(1)).thenReturn(team);
        when(studentRepository.findStudentsByTeam(team)).thenReturn(List.of());

        // Act
        List<Student> students = studentService.getStudentsByTeamId(1);

        // Assert
        assertThat(students).isEmpty();
        verify(teamService, times(1)).getTeamById(1);
        verify(studentRepository, times(1)).findStudentsByTeam(team);
    }

    @Test
    @DisplayName("deleteAllImportedStudentsAndGradeTypes_deletesAllImportedStudentsAndGradeTypes")
    void deleteAllImportedStudentsAndGradeTypes_deletesAllImportedStudentsAndGradeTypes() {
        doNothing().when(studentRepository).deleteAll();
        doNothing().when(gradeTypeService).deleteAllImportedGradeTypes();

        studentService.deleteAllImportedStudentsAndGradeTypes();

        verify(studentRepository, times(1)).deleteAll();
        verify(gradeTypeService, times(1)).deleteAllImportedGradeTypes();
    }

    @Test
    @DisplayName("deleteAllImportedStudentsAndGradeTypes_handlesExceptionWhenDeletingStudents")
    void deleteAllImportedStudentsAndGradeTypes_handlesExceptionWhenDeletingStudents() {
        doThrow(new RuntimeException()).when(studentRepository).deleteAll();
        doNothing().when(gradeTypeService).deleteAllImportedGradeTypes();

        studentService.deleteAllImportedStudentsAndGradeTypes();

        verify(studentRepository, times(1)).deleteAll();
        verify(gradeTypeService, times(1)).deleteAllImportedGradeTypes();
    }

    @Test
    void testGetStudents() {
        Student student1 = new Student();
        Student student2 = new Student();
        when(studentRepository.findAll()).thenReturn(Arrays.asList(student1, student2));

        List<Student> students = studentService.getStudents();

        assertEquals(2, students.size());
    }

    @Test
    void testGetNumberWomen() {
        when(studentRepository.countWomen()).thenReturn(10);

        int count = studentService.getNumberWomen();

        assertEquals(10, count);
    }

    @Test
    void testGetNumberStudents() {
        when(studentRepository.countTotal()).thenReturn(20);

        int count = studentService.getNumberStudents();

        assertEquals(20, count);
    }

    @Test
    void testGetNumberBachelor() {
        when(studentRepository.countBachelor()).thenReturn(5);

        int count = studentService.getNumberBachelor();

        assertEquals(5, count);
    }

    @Test
    void testWriteSummaryData() {
        StringWriter stringWriter = new StringWriter();
        CSVWriter csvWriter = new CSVWriter(stringWriter);

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

    @Test
    void testWriteEmptyRows() {
        StringWriter stringWriter = new StringWriter();
        CSVWriter csvWriter = new CSVWriter(stringWriter);

        studentService.writeEmptyRows(csvWriter, 2, 2);

        String expectedCsv = """
                "",""
                "",""
                """;
        String actualCsv = stringWriter.toString();

        assertEquals(expectedCsv, actualCsv);
    }

    @Test
    void testWriteCountRow() {
        StringWriter stringWriter = new StringWriter();
        CSVWriter csvWriter = new CSVWriter(stringWriter);

        studentService.writeCountRow(csvWriter, "Nombre F", 2, 3);

        String expectedCsv = "\"\",\"Nombre F\",\"2\"\n";
        String actualCsv = stringWriter.toString();

        assertEquals(expectedCsv, actualCsv);
    }

    @Test
    void writeStudentData_writesCorrectDataForSingleStudent() {
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
    void writeStudentData_writesCorrectDataForMultipleStudents() {
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
    void writeStudentData_handlesNullGrade() {
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
    @DisplayName("writeHeaders correctly handles empty grade types")
    void writeHeaders_correctlyHandlesEmptyGradeTypes() {
        StringWriter stringWriter = new StringWriter();
        CSVWriter csvWriter = new CSVWriter(stringWriter);

        List<GradeType> importedGrades = new ArrayList<>();

        studentService.writeHeaders(csvWriter, importedGrades);

        String expectedCsv = """
                "","","",""
                "","","sexe M / F",""
                """;
        String actualCsv = stringWriter.toString();

        assertEquals(expectedCsv, actualCsv);
    }

    @Test
    @DisplayName("writeHeaders correctly skips AVERAGE grade type")
    void writeHeaders_correctlySkipsAverageGradeType() {
        StringWriter stringWriter = new StringWriter();
        CSVWriter csvWriter = new CSVWriter(stringWriter);

        GradeType gradeType1 = new GradeType();
        gradeType1.name("GradeType1");
        gradeType1.factor(1.0f);

        GradeType gradeType2 = new GradeType();
        gradeType2.name("AVERAGE");
        gradeType2.factor(2.0f);

        List<GradeType> importedGrades = Arrays.asList(gradeType1, gradeType2);

        studentService.writeHeaders(csvWriter, importedGrades);

        String expectedCsv = """
                "","","","","","1.0"
                "","","sexe M / F","","","GradeType1"
                """;
        String actualCsv = stringWriter.toString();

        assertEquals(expectedCsv, actualCsv);
    }

    @Test
    @DisplayName("createStudentsCSV returns valid CSV for empty student list and grade types")
    void createStudentsCSV_returnsValidCSVForEmptyStudentListAndGradeTypes() {
        when(gradeTypeService.getImportedGradeTypes()).thenReturn(Collections.emptyList());
        when(studentService.getStudents()).thenReturn(Collections.emptyList());

        byte[] csv = studentService.createStudentsCSV();

        String expectedCsv = """
                "","","",""
                "","","sexe M / F",""
                "","","",""
                "","","",""
                "","","",""
                "","","",""
                "","Nombre F","0",""
                "","Nombre M","0",""
                "","Nombre B","","0"
                """;

        assertEquals(expectedCsv, new String(csv));
    }*/

}