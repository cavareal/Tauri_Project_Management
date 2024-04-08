package fr.eseo.tauri.controller;

import fr.eseo.tauri.service.AuthService;
import fr.eseo.tauri.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/students")
public class StudentController {

    private final StudentService studentService;

    private final AuthService authService;

    @Autowired
    public StudentController(StudentService studentService, AuthService authService) {
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
     * This method is responsible for handling file uploads.
     * It is mapped to the "/uploadCSV" endpoint and only responds to HTTP POST requests.
     *
     * @param file This is the file that is uploaded by the client. It is expected to be a CSV file.
     * @return ResponseEntity<String> This returns a response entity with a message indicating the result of the operation.
     * If the file is empty, it returns a bad request response with a message "Uploaded file is empty".
     * If the file is processed successfully, it returns an OK response with a message "File uploaded successfully".
     * If an error occurs during the processing of the file, it returns an internal server error response with a message indicating the error.
     */
    @PostMapping("/uploadCSV")
    public ResponseEntity<String> handleFileUpload(@RequestParam("file-upload") MultipartFile file) {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("Uploaded file is empty");
        }

        try {
            // Pass the uploaded file to the service method for further processing
            studentService.populateDatabaseFromCSV(file);
            return ResponseEntity.ok("File uploaded successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred: " + e.getMessage());
        }
    }
}
