package fr.eseo.tauri.service;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import fr.eseo.tauri.exception.GlobalExceptionHandler;
import fr.eseo.tauri.model.GradeType;
import fr.eseo.tauri.model.Student;
import fr.eseo.tauri.model.enumeration.Gender;
import fr.eseo.tauri.repository.StudentRepository;
import fr.eseo.tauri.util.CustomLogger;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import fr.eseo.tauri.exception.ResourceNotFoundException;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;

@Service
@RequiredArgsConstructor
public class StudentService {

    private final AuthService authService;
    private final StudentRepository studentRepository;
    private final UserService userService;
    private final ProjectService projectService;
    private final TeamService teamService;
    private final GradeTypeService gradeTypeService;
    private final GradeService gradeService;

    static final String MAP_KEY_NAMES = "names";
    static final String MAP_KEY_GENDERS = "genders";
    static final String MAP_KEY_BACHELORS = "bachelors";
    static final String MAP_KEY_GRADES = "grades";

    public Student getStudentById(String token, Integer id) {
        if (!Boolean.TRUE.equals(authService.checkAuth(token, "readStudent"))) {
            throw new SecurityException(GlobalExceptionHandler.UNAUTHORIZED_ACTION);
        }
        return studentRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("student", id));
    }

    public List<Student> getAllStudentsByProject(String token, Integer projectId) {
        if (!Boolean.TRUE.equals(authService.checkAuth(token, "readStudents"))) {
            throw new SecurityException(GlobalExceptionHandler.UNAUTHORIZED_ACTION);
        }
        return studentRepository.findAllByProject(projectId);
    }

    public void createStudent(String token, Student student) {
        if (!Boolean.TRUE.equals(authService.checkAuth(token, "addStudent"))) {
            throw new SecurityException(GlobalExceptionHandler.UNAUTHORIZED_ACTION);
        }
        student.team(teamService.getTeamById(token, student.teamId()));
        student.project(projectService.getProjectById(token, student.projectId()));

        studentRepository.save(student);
    }

    public void updateStudent(String token, Integer id, Student updatedStudent) {
        if (!Boolean.TRUE.equals(authService.checkAuth(token, "updateStudent"))) {
            throw new SecurityException(GlobalExceptionHandler.UNAUTHORIZED_ACTION);
        }

        Student student = getStudentById(token, id);

        if (updatedStudent.name() != null) student.name(updatedStudent.name());
        if (updatedStudent.email() != null) student.email(updatedStudent.email());
        if (updatedStudent.password() != null) student.password(updatedStudent.password());
        if (updatedStudent.privateKey() != null) student.privateKey(updatedStudent.privateKey());
        if (updatedStudent.gender() != null) student.gender(updatedStudent.gender());
        if (updatedStudent.bachelor() != null) student.bachelor(updatedStudent.bachelor());
        if (updatedStudent.teamRole() != null) student.teamRole(updatedStudent.teamRole());
        if (updatedStudent.projectId() != null) student.project(projectService.getProjectById(token, updatedStudent.projectId()));
        if (updatedStudent.teamId() != null) student.team(teamService.getTeamById(token, updatedStudent.teamId()));

        studentRepository.save(student);
    }

    public void deleteStudent(String token, Integer id) {
        if (!Boolean.TRUE.equals(authService.checkAuth(token, "deleteStudent"))) {
            throw new SecurityException(GlobalExceptionHandler.UNAUTHORIZED_ACTION);
        }
        getStudentById(token, id);
        studentRepository.deleteById(id);
    }

    public void deleteAllStudentsByProject(String token, Integer projectId) {
        if (!Boolean.TRUE.equals(authService.checkAuth(token, "deleteStudent"))) {
            throw new SecurityException(GlobalExceptionHandler.UNAUTHORIZED_ACTION);
        }
        studentRepository.deleteAllByProject(projectId);
        gradeTypeService.deleteAllImportedGradeTypes();
    }

    /**
     * This method is used to create a new student and save it to the repository.
     *
     * @param student the Student object to be saved
     * @throws IllegalArgumentException if the student's name is null or empty
     */
    public void createStudent(Student student) {
        if (student.name() == null || student.name().trim().isEmpty()) {
            throw new IllegalArgumentException("Name cannot be null or empty");
        }
        studentRepository.save(student);
    }

    /**
     * Retrieves students form a team.
     * @param teamId The id of the team
     * @return a list of students
     */
    /*public List<Student> getStudentsByTeamId(Integer teamId) {
        Team team = teamService.getTeamById(teamId);
        return studentRepository.findStudentsByTeam(team);
    }*/

    /**
     * <b>HELPER METHOD</b>
     * This method is used to extract student data from a CSV file.
     * The data includes the student's name, gender, bachelor status, and grades.
     *
     * @param inputStream The input stream of the CSV file.
     * @return A map containing lists of names, genders, bachelor statuses, and grades.
     */
    public Map<String, Object> extractNamesGenderBachelorAndGrades(InputStream inputStream) {
        Map<String, Object> result = new HashMap<>();
        List<String> names = new ArrayList<>();
        List<String> genders = new ArrayList<>();
        List<String> bachelors = new ArrayList<>();
        List<List<String>> grades = new ArrayList<>();

        boolean namesStarted = false;

        try (CSVReader reader = new CSVReader(new InputStreamReader(inputStream))) {
            String[] nextLine;
            while ((nextLine = reader.readNext()) != null) {
                if (!namesStarted && hasNonEmptyValue(nextLine, 1)) {
                    namesStarted = true;
                }
                if (namesStarted && !names.isEmpty() && !hasNonEmptyValue(nextLine, 1)) {
                    break;
                }
                if (namesStarted && hasNonEmptyValue(nextLine, 1)) {
                    names.add(nextLine[1]);
                    genders.add(nextLine[2]);
                    bachelors.add(nextLine.length > 3 ? nextLine[3] : "");
                    grades.add(Arrays.asList(Arrays.copyOfRange(nextLine, 4, nextLine.length)));
                }
            }
        } catch (IOException | CsvValidationException e) {
            CustomLogger.error("An error occurred in extractNamesGenderAndBachelor", e);
        }

        CustomLogger.info("Successfully extracted student data (names, genders, bachelors and grades ) from the CSV file.");

        result.put(MAP_KEY_NAMES, names);
        result.put(MAP_KEY_GENDERS, genders);
        result.put(MAP_KEY_BACHELORS, bachelors);
        result.put(MAP_KEY_GRADES, grades);

        return result;
    }


    /**
     * <b>HELPER METHOD</b>
     * Checks if the specified index in the given line contains a non-empty value.
     *
     * @param line  the array representing a line from the CSV file
     * @param index the index to check
     * @return {@code true} if the index contains a non-empty value, {@code false} otherwise
     */
    static boolean hasNonEmptyValue(String[] line, int index) {
        return line.length > index && !line[index].trim().isEmpty();
    }


    /**
     * <b>HELPER  METHOD</b>
     * This method is used to create a Student object from the provided data.
     * The data includes the student's name, gender, and bachelor status.
     *
     * @param name the name of the student
     * @param gender the gender of the student
     * @param bachelor the bachelor status of the student
     * @return the created Student object
     * @throws IllegalArgumentException if the name or gender is null or empty, or if the bachelor status is null
     */
    Student createStudentFromData(String name, String gender, String bachelor) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Name cannot be null or empty");
        }
        if (gender == null || gender.trim().isEmpty()) {
            throw new IllegalArgumentException("Gender cannot be null or empty");
        }
        if (bachelor == null) {
            throw new IllegalArgumentException("Bachelor status cannot be null");
        }

        Student student = new Student();
        student.name(name);
        student.gender(gender.equals("M") ? Gender.MAN : Gender.WOMAN);
        student.bachelor(!bachelor.isEmpty());
        student.teamRole("Not assigned");
        student.project(null);
        student.team(null); // Team is not assigned yet
        student.password("password");
        student.privateKey("privateKey");
        student.email(name.toLowerCase().replace(" ", ".") + "@reseau.eseo.fr");
        return student;
    }

    /**
     * This method is used to populate the database with student data from a CSV file.
     * The CSV file is expected to contain the following data for each student:
     * - Name
     * - Gender
     * - Bachelor status
     * - Grades
     * - Coefficients
     * - Ratings
     * @param file The CSV file containing the student data.
     */
    @SuppressWarnings("unchecked")
    public void populateDatabaseFromCSV(MultipartFile file) {
        if (file.isEmpty()) {
            CustomLogger.info("Uploaded file is empty");
            return;
        }

        try {
            List<GradeType> gradeTypes = gradeTypeService.createGradeTypesFromCSV(file.getInputStream());
            CustomLogger.info("Successfully created GradeType objects from the CSV file.");
            Map<String, Object> extractedData = extractNamesGenderBachelorAndGrades(file.getInputStream());

            List<String> names = (List<String>) extractedData.get(MAP_KEY_NAMES);
            List<String> genders = (List<String>) extractedData.get(MAP_KEY_GENDERS);
            List<String> bachelors = (List<String>) extractedData.get(MAP_KEY_BACHELORS);
            List<List<String>> gradesList = (List<List<String>>) extractedData.get(MAP_KEY_GRADES);

            for (int i = 0; i < names.size(); i++) {
                Student student = createStudentFromData(names.get(i), genders.get(i), bachelors.get(i));
                createStudent(student);
                gradeService.createGradesFromGradeTypesAndValues(student, gradesList.get(i), gradeTypes, "Imported grades");
            }

            CustomLogger.info(String.format("Successfully populated database with %d students and their associated grades contained in the CSV file.", names.size()));
        } catch (Exception e) {
            CustomLogger.error("An error occurred while handling the uploaded file", e);
            throw new RuntimeException("An unexpected error occurred: " + e.getMessage());
        }
    }

}
