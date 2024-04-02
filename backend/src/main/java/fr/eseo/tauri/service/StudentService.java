package fr.eseo.tauri.service;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import fr.eseo.tauri.model.Student;
import fr.eseo.tauri.model.enumeration.Gender;
import fr.eseo.tauri.repository.StudentRepository;
import fr.eseo.tauri.util.CustomLogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Service class for handling student-related operations.
 */
@Service
public class StudentService {

    private final StudentRepository studentRepository;

    /**
     * Constructs a new StudentService with the specified StudentRepository.
     *
     * @param studentRepository the student repository to be used
     */
    @Autowired
    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
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
                CustomLogger.logError("An error occurred while saving the file", e);
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
            CSVReader csvReader;
            try (FileReader filereader = new FileReader(fileToRead)) {

                // Create CSVReader object passing
                // file reader as a parameter
                csvReader = new CSVReader(filereader);
            }
            String[] nextRecord;

            // Read data line by line
            while ((nextRecord = csvReader.readNext()) != null) {
                for (String cell : nextRecord) {
                    resultList.add(cell);
                    CustomLogger.logInfo(cell);
                }
            }
        } catch (Exception e) {
            CustomLogger.logError("An error occurred while reading the file in fileReader", e);
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
     * @param inputStream the path to the CSV file
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
    public static List<List<String>> extractNamesGenderAndBachelor(InputStream inputStream) {
        if (inputStream == null) {
            throw new IllegalArgumentException("Input stream cannot be null");
        }

        List<List<String>> result = new ArrayList<>();
        List<String> names = new ArrayList<>();
        List<String> genders = new ArrayList<>();
        List<String> bachelors = new ArrayList<>();

        boolean namesStarted = false;

        try (CSVReader reader = new CSVReader(new InputStreamReader(inputStream))) {
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
        } catch (IOException e) {
            CustomLogger.logError("An IOException occurred while reading the input stream in extractNamesGenderAndBachelor", e);
        } catch (CsvValidationException e) {
            CustomLogger.logError("A CsvValidationException occurred in extractNamesGenderAndBachelor", e);
        }

        result.add(names);
        result.add(genders);
        result.add(bachelors);
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
    private static boolean hasNonEmptyValue(String[] line, int index) {
        return line.length > index && !line[index].trim().isEmpty();
    }

    /**
     * Helper method
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
     * Helper method
     * Creates a student object from extracted data.
     *
     * @param name     The name of the student.
     * @param gender   The gender of the student.
     * @param bachelor The bachelor status of the student.
     * @return The created student object.
     */
    private Student createStudentFromData(String name, String gender, String bachelor) {
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
        student.project(null); // TODO: Discuss with clement how the project is assigned on the front
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
    public void populateDatabaseFromCsv(MultipartFile file) {
        // Check if the file is empty
        if (file.isEmpty()) {
            CustomLogger.logInfo("Uploaded file is empty");
            return;
        }

        // Handle the uploaded file in memory
        try {
            // Extract data from CSV
            List<List<String>> extractedData = extractNamesGenderAndBachelor(file.getInputStream());

            // Process extracted data
            for (int i = 0; i < extractedData.get(0).size(); i++) {
                // Create student
                Student student = createStudentFromData(
                        extractedData.get(0).get(i),
                        extractedData.get(1).get(i),
                        extractedData.get(2).get(i)
                );

                // Save student
                studentRepository.save(student);
            }
            CustomLogger.logInfo("Successfully populated database with " + extractedData.get(0).size() + " students.");
        } catch (IOException e) {
            CustomLogger.logError("An error occurred while handling the uploaded file", e);
        }
    }

}
