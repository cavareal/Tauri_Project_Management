package fr.eseo.tauri.service;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import fr.eseo.tauri.model.GradeType;
import fr.eseo.tauri.model.Student;
import fr.eseo.tauri.model.enumeration.Gender;
import fr.eseo.tauri.repository.StudentRepository;
import fr.eseo.tauri.util.CustomLogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;

/**
 * Service class for handling student-related operations.
 */
@Service
public class StudentService {

    private static final String MAP_KEY_NAMES = "names";
    private static final String MAP_KEY_GENDERS = "genders";
    private static final String MAP_KEY_BACHELORS = "bachelors";
    private static final String MAP_KEY_GRADES = "grades";

    private final StudentRepository studentRepository;

    private final GradeTypeService gradeTypeService;

    private final GradeService gradeService;

    /**
     * Constructs a new StudentService with the specified StudentRepository.
     *
     * @param studentRepository the student repository to be used
     * @param gradeTypeService  the grade service to be used
     * @param gradeService      the grade service to be used
     */
    @Autowired
    public StudentService(StudentRepository studentRepository, GradeTypeService gradeTypeService, GradeService gradeService) {
        this.studentRepository = studentRepository;
        this.gradeTypeService = gradeTypeService;
        this.gradeService = gradeService;
    }

    public void createStudent(Student student) {
        if (student.name() == null || student.name().trim().isEmpty()) {
            throw new IllegalArgumentException("Name cannot be null or empty");
        }
        studentRepository.save(student);
    }


    /**
     * Retrieves the quantity of students.
     *
     * @return the quantity of students
     */
    public Integer getStudentQuantity() {
        List<Student> students = studentRepository.findAll();
        return students.size();
    }

    /**
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
            CustomLogger.logError("An error occurred in extractNamesGenderAndBachelor", e);
        }

        result.put(MAP_KEY_NAMES, names);
        result.put(MAP_KEY_GENDERS, genders);
        result.put(MAP_KEY_BACHELORS, bachelors);
        result.put(MAP_KEY_GRADES, grades);

        return result;
    }


    /**
     * Helper method
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
     * Helper method
     * Creates a student object from extracted data.
     *
     * @param name     The name of the student.
     * @param gender   The gender of the student.
     * @param bachelor The bachelor status of the student.
     * @return The created student object.
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
     * Populates the database with user and student records from a CSV file.
     *
     * @param file The path to the CSV file containing user and student data.
     */
    @SuppressWarnings("unchecked")
    public void populateDatabaseFromCsv(MultipartFile file) {
        if (file.isEmpty()) {
            CustomLogger.logInfo("Uploaded file is empty");
            return;
        }

        try {
            List<GradeType> gradeTypes = gradeTypeService.createGradeTypesFromCSV(file.getInputStream());
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

            CustomLogger.logInfo("Successfully populated database with " + names.size() + " students.");
        } catch (Exception e) {
            CustomLogger.logError("An error occurred while handling the uploaded file", e);
        }
    }

}
