package fr.eseo.tauri.service;

import com.opencsv.CSVReader;
import fr.eseo.tauri.repository.StudentRepository;
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

    /**
     * Constructs a new StudentService with the specified StudentRepository.
     *
     * @param studentRepository the student repository to be used
     */
    @Autowired
    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    /**
     * Handles the upload of a file.
     *
     * @param file the file to be uploaded
     * @return the uploaded file
     */
    public File handleFileUpload(@RequestParam("file") MultipartFile file){
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
    public List<String> fileReader(File fileToRead ){
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
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return resultList;
    }
}
