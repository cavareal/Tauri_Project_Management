package fr.eseo.tauri.controller;

import fr.eseo.tauri.model.Student;
import fr.eseo.tauri.repository.StudentRepository;
import fr.eseo.tauri.service.AuthService;
import fr.eseo.tauri.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;

@RestController
@RequestMapping("/api/students")
public class StudentController {

    private final StudentRepository studentRepository;
    private final StudentService studentService;
    private final AuthService authService;

    @Autowired
    public StudentController(StudentRepository studentRepository, StudentService studentService, AuthService authService) {
        this.studentRepository = studentRepository;
        this.studentService = studentService;
        this.authService = authService;
    }


    @GetMapping("/quantity-all")
    public ResponseEntity<String> getStudentQuantity(@RequestHeader("Authorization") String token) {
        // Check token, if user is GOOD
        String permission = "readStudentQuantity";
        if (authService.checkAuth(token, permission)) {
            try {
                Integer quantity = studentService.getStudentQuantity();
                return ResponseEntity.status(HttpStatus.OK).body(String.valueOf(quantity));
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erreur lors de la mise à jour : " + e.getMessage()); // Erreur 500
            }
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Non autorisé"); // Code 401
        }
    }


    /**
     * Handles the upload of a file containing student data.
     *
     * @param file  the file to be uploaded
     * @param token the authorization token
     * @return a ResponseEntity containing the result of the upload operation
     */
    @PostMapping("/upload")
    public ResponseEntity<String> upload(@RequestParam("file") MultipartFile file, @RequestHeader("Authorization") String token) {
        // Check token, if user is GOOD
        String permission = "addStudentFile";
        if (authService.checkAuth(token, permission)) {
            try {
                File file2 = studentService.handleFileUpload(file);
                List<String> listFile = studentService.fileReader(file2);
                if (file2 != null) {
                    return ResponseEntity.ok("L'upload à bien été enregistré"); // Retourne true avec code 200
                } else {
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erreur lors de la mise à jour"); // Erreur 500
                }
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erreur lors de la mise à jour : " + e.getMessage()); // Erreur 500
            }
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Non autorisé"); // Code 401
        }
    }

    /**
     * Test function to upload a file for the user story uplaod student list
     *
     * Handles the upload of a file containing student data.
     *
     * @param file  the file to be uploaded
     * @return a ResponseEntity containing the result of the upload operation
     *
     */
    @PostMapping("/uploadTest")
    public ResponseEntity<String> handleFileUpload(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("Uploaded file is empty");
        }

        try {
            // Pass the uploaded file to the service method for further processing
            studentService.populateDatabaseFromCsv(file);
            return ResponseEntity.ok("File uploaded successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to process the uploaded file");
        }
    }
}
