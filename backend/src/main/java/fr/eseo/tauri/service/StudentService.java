package fr.eseo.tauri.service;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import fr.eseo.tauri.model.Student;
import fr.eseo.tauri.model.User;
import fr.eseo.tauri.model.enumeration.Gender;
import fr.eseo.tauri.repository.ProjectRepository;
import fr.eseo.tauri.repository.StudentRepository;
import fr.eseo.tauri.repository.TeamRepository;
import fr.eseo.tauri.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Service class for handling student-related operations.
 */
@Service
public class StudentService {

    private final StudentRepository studentRepository;
    private final UserRepository userRepository;

    private final TeamRepository teamRepository;

    private final ProjectRepository projectRepository;

    /**
     * Constructs a new StudentService with the specified StudentRepository.
     *
     * @param studentRepository the student repository to be used
     * @param userRepository    the user repository to be used
     * @param teamRepository    the team repository to be used
     * @param projectRepository the project repository to be used
     */
    @Autowired
    public StudentService(StudentRepository studentRepository, UserRepository userRepository, TeamRepository teamRepository, ProjectRepository projectRepository) {
        this.studentRepository = studentRepository;
        this.userRepository = userRepository;
        this.teamRepository = teamRepository;
        this.projectRepository = projectRepository;
    }

    public void createStudent(Student student) {
        studentRepository.save(student);
    }

    /**
     * Handles the upload of a file.
     *
     * @param file the file to be uploaded
     * @return the uploaded file
     */
    public File handleFileUpload(@RequestParam("file") MultipartFile file) {
        // Handle the file upload
        File savedFile = null;
        if (!file.isEmpty()) {
            try {
                // Save the file to the file system
                savedFile = new File("C:\\Users\\pallu\\OneDrive\\Documents\\Workspace\\Ingenieur\\E4 n2\\ProjetGL\\nath\\example.csv" /*+ file.getOriginalFilename()*/);

                file.transferTo(savedFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return savedFile;
    }

    /**
     * Reads data from a CSV file.
     *
     * @param fileToRead the CSV file to be read
     * @return a list of strings representing the data read from the CSV file
     */
    public List<String> fileReader(File fileToRead) {
        List<String> resultList = new ArrayList<>();

        try {

            // Create an object of FileReader
            // class with CSV file as a parameter.
            FileReader filereader = new FileReader(fileToRead);

            // Create CSVReader object passing
            // file reader as a parameter
            CSVReader csvReader = new CSVReader(filereader);
            String[] nextRecord;

            // Read data line by line
            while ((nextRecord = csvReader.readNext()) != null) {
                for (String cell : nextRecord) {
                    resultList.add(cell);
                    System.out.println(cell);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultList;
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
     * Extracts names, genders, and bachelor status from a CSV file.
     *
     * @param filePath the path to the CSV file
     * @return a list containing three lists: names, genders, and bachelor status
     * <p>
     * The returned list contains three inner lists:
     * <ul>
     * <li>names: List of names extracted from the CSV file.</li>
     * <li>genders: List of genders extracted from the CSV file.</li>
     * <li>bachelor status: List of bachelor status extracted from the CSV file.</li>
     * </ul>
     * </p>
     */
    private static List<List<String>> extractNamesGenderAndBachelor(String filePath) {
        List<List<String>> result = new ArrayList<>();
        List<String> names = new ArrayList<>();
        List<String> genders = new ArrayList<>();
        List<String> bachelors = new ArrayList<>();

        boolean namesStarted = false;

        try (CSVReader reader = new CSVReader(new FileReader(filePath))) {
            String[] nextLine;
            while ((nextLine = reader.readNext()) != null) {
                if (!namesStarted && hasNonEmptyValue(nextLine, 1)) {
                    namesStarted = true;
                }

                if (namesStarted && !names.isEmpty() && isEmpty(nextLine, 1)) {
                    break;
                }

                if (namesStarted && !isEmpty(nextLine, 1)) {
                    names.add(nextLine[1]); // Assuming complete name is in the second column
                    genders.add(nextLine[2]);
                    bachelors.add(nextLine.length > 3 ? nextLine[3] : ""); // Add bachelor status or empty string
                }
            }
        } catch (IOException | CsvValidationException e) {
            e.printStackTrace();
        }

        result.add(names);
        result.add(genders);
        result.add(bachelors);
        return result;
    }

    /**
     * Checks if the specified index in the given line contains a non-empty value.
     *
     * @param line  the array representing a line from the CSV file
     * @param index the index to check
     * @return {@code true} if the index contains a non-empty value, {@code false} otherwise
     */
    private static boolean hasNonEmptyValue(String[] line, int index) {
        return line.length > index && !line[index].trim().isEmpty();
    }

    /**
     * Checks if the specified index in the given line contains an empty value.
     *
     * @param line  the array representing a line from the CSV file
     * @param index the index to check
     * @return {@code true} if the index contains an empty value, {@code false} otherwise
     */
    private static boolean isEmpty(String[] line, int index) {
        return line.length > index && line[index].trim().isEmpty();
    }

    /**
     * Populates the database with user and student records from a CSV file.
     *
     * @param filePath The path to the CSV file containing user and student data.
     */
    public void populateDatabaseFromCsv(String filePath) {
        // TODO: What about the projectId ?
        // Extract data from CSV
        List<List<String>> extractedData = extractNamesGenderAndBachelor(filePath);
        List<String> names = extractedData.get(0);
        List<String> genders = extractedData.get(1);
        List<String> bachelors = extractedData.get(2);

        // Process extracted data
        for (int i = 0; i < names.size(); i++) {
            // Create user
            User user = createUserFromName(names.get(i));

            // Save user and retrieve user ID
            int userId = saveUserAndGetId(user);

            // Create student
            Student student = createStudentFromData(names.get(i), genders.get(i), bachelors.get(i), userId);

            // Save student
            studentRepository.save(student);
        }
    }

    /**
     * Creates a user object from a given name.
     *
     * @param name The complete name of the user.
     * @return The created user object.
     */
    private User createUserFromName(String name) {
        User user = new User();
        user.name(name);
        user.email(name.toLowerCase().replace(" ", ".") + "@reseau.eseo.fr");
        user.password("password");
        user.privateKey("privateKey");
        return user;
    }

    /**
     * Saves a user object to the database and retrieves its auto-generated ID.
     *
     * @param user The user object to be saved.
     * @return The auto-generated ID of the saved user.
     */
    private int saveUserAndGetId(User user) {
        User savedUser = userRepository.save(user);
        return savedUser.id();
    }

    /**
     * Creates a student object from extracted data.
     *
     * @param name     The name of the student.
     * @param gender   The gender of the student.
     * @param bachelor The bachelor status of the student.
     * @param userId   The ID of the corresponding user.
     * @return The created student object.
     */
    private Student createStudentFromData(String name, String gender, String bachelor, int userId) {
        Student student = new Student();
        student.name(name);
        student.gender(gender.equals("M") ? Gender.MAN : Gender.WOMAN);
        student.bachelor(!bachelor.isEmpty());
        student.teamRole("Not assigned");
        student.projectId(null);
        student.teamId(null);
        student.id(userId);
        return student;
    }


}
