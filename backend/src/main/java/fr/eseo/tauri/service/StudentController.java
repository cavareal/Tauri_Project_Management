package fr.eseo.tauri.service;

import fr.eseo.tauri.model.Student;
import fr.eseo.tauri.repository.StudentRepository;
import fr.eseo.tauri.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import fr.eseo.tauri.service.AuthService;

import java.io.File;
import java.util.List;

/**
 * Controller class for handling student-related HTTP requests.
 */
@RestController
@RequestMapping("/students")
public class StudentController {

    private final StudentRepository studentRepository;
    private final AuthService authService;
    private final StudentService studentService;

    /**
     * Constructs a new StudentController with the specified dependencies.
     *
     * @param studentRepository the student repository to be used
     * @param authService       the authentication service to be used
     * @param studentService    the student service to be used
     */
    @Autowired
    public StudentController(StudentRepository studentRepository, AuthService authService, StudentService studentService) {
        this.studentRepository = studentRepository;
        this.authService = authService;
        this.studentService = studentService;
    }

    /**
     * Retrieves all students.
     *
     * @return an iterable collection of all students
     */
    @GetMapping("/all")
    public Iterable<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    /**
     * Retrieves a student by ID.
     *
     * @param id the ID of the student to retrieve
     * @return the student with the specified ID, or null if not found
     */
    @GetMapping("/{id}")
    public Student getStudentById(@PathVariable Integer id) {
        return studentRepository.findById(id).orElse(null);
    }

    /**
     * Deletes a student by ID.
     *
     * @param id the ID of the student to delete
     * @return a string indicating that the student has been deleted
     */
    @DeleteMapping("/delete/{id}")
    public String deleteStudent(@PathVariable Integer id) {
        studentRepository.deleteById(id);
        return "Student deleted";
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
        if(authService.checkAuth(token, permission)) {
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
}
