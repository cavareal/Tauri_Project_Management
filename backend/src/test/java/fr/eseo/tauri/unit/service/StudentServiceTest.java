package fr.eseo.tauri.unit.service;

import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvValidationException;
import fr.eseo.tauri.exception.EmptyResourceException;
import fr.eseo.tauri.exception.ResourceNotFoundException;
import fr.eseo.tauri.model.*;
import fr.eseo.tauri.model.enumeration.Gender;
import fr.eseo.tauri.model.enumeration.GradeTypeName;
import fr.eseo.tauri.repository.BonusRepository;
import fr.eseo.tauri.repository.StudentRepository;
import fr.eseo.tauri.service.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
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
    private GradeService gradeService;

    @Mock
    private TeamService teamService;

    @Mock
    private ProjectService projectService;

    @Mock
    private SprintService sprintService;

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
        when(studentRepository.findById(anyInt())).thenReturn(Optional.of(student));

        Student result = studentService.getStudentById(1);

        assertEquals(student, result);
    }

    @Test
    void getStudentByIdShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() {
        when(studentRepository.findById(anyInt())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> studentService.getStudentById(1));
    }

    @Test
    void getAllStudentsByProjectShouldReturnStudentsWhenAuthorized() {
        when(studentRepository.findAllByProject(anyInt())).thenReturn(Arrays.asList(new Student(), new Student()));

        List<Student> result = studentService.getAllStudentsByProject(1);

        assertEquals(2, result.size());
    }

    @Test
    void getAllStudentsByProjectShouldReturnEmptyListWhenNoStudentsExist() {
        when(studentRepository.findAllByProject(anyInt())).thenReturn(Collections.emptyList());

        List<Student> result = studentService.getAllStudentsByProject(1);

        assertTrue(result.isEmpty());
    }

    @Test
    void createStudentShouldSaveWhenAuthorizedAndProjectAndTeamExist() {
        when(projectService.getProjectById(anyInt())).thenReturn(new Project());
        when(teamService.getTeamById(anyInt())).thenReturn(new Team());
        when(sprintService.getAllSprintsByProject(anyInt())).thenReturn(Collections.emptyList());

        Student student = new Student();
        student.projectId(1);
        student.teamId(1);
        studentService.createStudent(student);

        verify(studentRepository, times(1)).save(student);
        verify(roleService, times(1)).createRole(any(Role.class));
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
        assertThrows(IllegalArgumentException.class, () -> studentService.createStudentFromData(null, "M", "B", 1));
    }

    @Test
    void createStudentFromDataShouldThrowExceptionWhenNameIsEmpty() {
        assertThrows(IllegalArgumentException.class, () -> studentService.createStudentFromData("", "M", "B", 1));
    }

    @Test
    void createStudentFromDataShouldThrowExceptionWhenGenderIsNull() {
        assertThrows(IllegalArgumentException.class, () -> studentService.createStudentFromData("John Doe", null, "B", 1));
    }

    @Test
    void createStudentFromDataShouldThrowExceptionWhenGenderIsEmpty() {
        assertThrows(IllegalArgumentException.class, () -> studentService.createStudentFromData("John Doe", "", "B", 1));
    }

    @Test
    void createStudentFromDataShouldThrowExceptionWhenBachelorIsNull() {
        assertThrows(IllegalArgumentException.class, () -> studentService.createStudentFromData("John Doe", "M", null, 1));
    }

    @Test
    void populateDatabaseFromCSVShouldThrowEmptyResourceExceptionWhenFileIsEmpty() {
        MultipartFile file = mock(MultipartFile.class);
        when(file.isEmpty()).thenReturn(true);

        assertThrows(EmptyResourceException.class, () -> studentService.populateDatabaseFromCSV(file, 1));
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

    @Test
    void getStudentBonusShouldReturnBonusWhenAuthorizedAndBonusExists() {
        Integer idStudent = 1;
        Boolean limited = true;
        Bonus bonus = new Bonus();
        Integer idSprint = 1;

        when(bonusRepository.findStudentBonus(idStudent, limited, idSprint)).thenReturn(bonus);

        Bonus result = studentService.getStudentBonus(idStudent, limited, idSprint);

        assertEquals(bonus, result);
    }

    @Test
    void getStudentBonusShouldReturnNullWhenNoBonusFound() {
        Integer idStudent = 1;
        Boolean limited = true;
        Integer idSprint = 1;

        when(bonusRepository.findStudentBonus(idStudent, limited, idSprint)).thenReturn(null);

        Bonus result = studentService.getStudentBonus(idStudent, limited, idSprint);

        assertNull(result);
    }

    @Test
    void getStudentBonusesShouldReturnBonusesWhenAuthorizedAndBonusesExist() {
        Integer idStudent = 1;
        List<Bonus> bonuses = Arrays.asList(new Bonus(), new Bonus());
        Integer idSprint = 1;

        when(bonusRepository.findAllStudentBonuses(idStudent, idSprint)).thenReturn(bonuses);

        List<Bonus> result = studentService.getStudentBonuses(idStudent, idSprint);

        assertEquals(bonuses, result);
    }

    @Test
    void getStudentBonusesShouldReturnEmptyListWhenNoBonusesFound() {
        Integer idStudent = 1;
        Integer idSprint = 1;

        when(bonusRepository.findAllStudentBonuses(idStudent, idSprint)).thenReturn(Collections.emptyList());

        List<Bonus> result = studentService.getStudentBonuses(idStudent, idSprint);

        assertTrue(result.isEmpty());
    }

    @Test
    void updateStudentShouldUpdateGenderWhenProvided() {
        Integer id = 1;
        Student existingStudent = new Student();
        Student updatedStudent = new Student();
        updatedStudent.gender(Gender.MAN);

        when(studentRepository.findById(id)).thenReturn(Optional.of(existingStudent));

        studentService.updateStudent(id, updatedStudent);

        assertEquals(updatedStudent.gender(), existingStudent.gender());
        verify(studentRepository, times(1)).save(existingStudent);
    }

    @Test
    void updateStudentShouldUpdateBachelorStatusWhenProvided() {
        Integer id = 1;
        Student existingStudent = new Student();
        Student updatedStudent = new Student();
        updatedStudent.bachelor(true);

        when(studentRepository.findById(id)).thenReturn(Optional.of(existingStudent));

        studentService.updateStudent(id, updatedStudent);

        assertEquals(updatedStudent.bachelor(), existingStudent.bachelor());
        verify(studentRepository, times(1)).save(existingStudent);
    }

    @Test
    void updateStudentShouldUpdateTeamRoleWhenProvided() {
        Integer id = 1;
        Student existingStudent = new Student();
        Student updatedStudent = new Student();
        updatedStudent.teamRole("NewRole");

        when(studentRepository.findById(id)).thenReturn(Optional.of(existingStudent));

        studentService.updateStudent(id, updatedStudent);

        assertEquals(updatedStudent.teamRole(), existingStudent.teamRole());
        verify(studentRepository, times(1)).save(existingStudent);
    }

    @Test
    void updateStudentShouldUpdateProjectWhenProjectIdIsProvided() {
        Integer id = 1;
        Student existingStudent = new Student();
        Student updatedStudent = new Student();
        updatedStudent.projectId(2);
        Project project = new Project();
        project.id(2);

        when(studentRepository.findById(id)).thenReturn(Optional.of(existingStudent));
        when(projectService.getProjectById(updatedStudent.projectId())).thenReturn(project);

        studentService.updateStudent(id, updatedStudent);

        assertEquals(project, existingStudent.project());
        verify(studentRepository, times(1)).save(existingStudent);
    }

    @Test
    void updateStudentShouldUpdateTeamWhenTeamIdIsProvided() {
        Integer id = 1;
        Student existingStudent = new Student();
        Student updatedStudent = new Student();
        updatedStudent.teamId(2);
        Team team = new Team();
        team.id(2);

        when(studentRepository.findById(id)).thenReturn(Optional.of(existingStudent));
        when(teamService.getTeamById(updatedStudent.teamId())).thenReturn(team);

        studentService.updateStudent(id, updatedStudent);

        assertEquals(team, existingStudent.team());
        verify(studentRepository, times(1)).save(existingStudent);
    }

    @Test
    void updateStudentShouldThrowResourceNotFoundExceptionWhenStudentDoesNotExist() {
        Integer id = 1;
        Student updatedStudent = new Student();

        when(studentRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> studentService.updateStudent(id, updatedStudent));
    }

}