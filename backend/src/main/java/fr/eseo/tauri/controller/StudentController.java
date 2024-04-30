package fr.eseo.tauri.controller;

import fr.eseo.tauri.model.Student;
import fr.eseo.tauri.repository.StudentRepository;
import fr.eseo.tauri.service.AuthService;
import fr.eseo.tauri.service.StudentService;
import fr.eseo.tauri.util.CustomLogger;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/students")
@Tag(name = "students")
public class StudentController {

    private final StudentRepository studentRepository;
    private final StudentService studentService;

    private final AuthService authService;

    @Autowired
    public StudentController(StudentService studentService, AuthService authService, StudentRepository studentRepository) {
        this.studentService = studentService;
        this.authService = authService;
        this.studentRepository = studentRepository;
    }

    @GetMapping
    public ResponseEntity<List<Student>> getStudents() {
        return ResponseEntity.ok(studentRepository.findAll());
    }

    @GetMapping("/quantity-all")
    public ResponseEntity<String> getStudentQuantity(@RequestHeader("Authorization") String token) {
        // Check token, if user is GOOD
        String permission = "readStudentQuantity";
        if (Boolean.TRUE.equals(authService.checkAuth(token, permission))) {
            try {
                Integer quantity = studentService.getStudentQuantity();
                return ResponseEntity.status(HttpStatus.OK).body(String.valueOf(quantity));
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erreur lors de la mise à jour : " + e.getMessage());
            }
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Non autorisé");
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

    @GetMapping("/team/{id}")
    public ResponseEntity<List<Student>> getStudentsByTeam(@RequestHeader("Authorization") String token, @PathVariable Integer id) {
        // Check token, if user is GOOD
        String permission = "readStudentByTeam";
        if (Boolean.TRUE.equals(authService.checkAuth(token, permission))) {
            try {
                List<Student> students = studentService.getStudentsByTeamId(id);
                return ResponseEntity.ok(students);
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
            }
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
    }

    @DeleteMapping()
    public ResponseEntity<String> deleteStudents() {
        studentService.deleteAllImportedStudentsAndGradeTypes();
        return ResponseEntity.ok("students have been deleted successfully");
    }

    @GetMapping("/download-students-csv")
    public ResponseEntity<byte[]> downloadStudentsCSV() {
        try{
            CustomLogger.logInfo("Downloading students CSV");
            return ResponseEntity.ok(studentService.createStudentsCSV());
        }
        catch (Exception e){
            CustomLogger.logError("Error downloading students CSV", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }

    }
}
