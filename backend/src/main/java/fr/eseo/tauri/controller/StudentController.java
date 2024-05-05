package fr.eseo.tauri.controller;

import com.opencsv.exceptions.CsvValidationException;
import fr.eseo.tauri.model.Student;
import fr.eseo.tauri.service.StudentService;
import fr.eseo.tauri.util.CustomLogger;
import fr.eseo.tauri.util.ResponseMessage;
import fr.eseo.tauri.util.valid.Update;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import fr.eseo.tauri.util.valid.Create;
import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/students")
@Tag(name = "students")
public class StudentController {

	private final StudentService studentService;
	private final ResponseMessage responseMessage = new ResponseMessage("student");

	@GetMapping("/{id}")
	public ResponseEntity<Student> getStudentById(@RequestHeader("Authorization") String token, @PathVariable Integer id) {
		Student student = studentService.getStudentById(token, id);
		return ResponseEntity.ok(student);
	}

	@GetMapping
	public ResponseEntity<List<Student>> getAllStudentsByProject(@RequestHeader("Authorization") String token, @RequestParam Integer projectId) {
		List<Student> students = studentService.getAllStudentsByProject(token, projectId);
		return ResponseEntity.ok(students);
	}

	@PostMapping
	public ResponseEntity<String> createStudent(@RequestHeader("Authorization") String token, @Validated(Create.class) @RequestBody Student student) {
		studentService.createStudent(token, student);
		CustomLogger.info(responseMessage.create());
		return ResponseEntity.ok(responseMessage.create());
	}

	@PatchMapping("/{id}")
	public ResponseEntity<String> updateStudent(@RequestHeader("Authorization") String token, @PathVariable Integer id, @Validated(Update.class) @RequestBody Student updatedStudent) {
		studentService.updateStudent(token, id, updatedStudent);
		CustomLogger.info(responseMessage.update());
		return ResponseEntity.ok(responseMessage.update());
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<String> deleteStudent(@RequestHeader("Authorization") String token, @PathVariable Integer id) {
		studentService.deleteStudent(token, id);
		CustomLogger.info(responseMessage.delete());
		return ResponseEntity.ok(responseMessage.delete());
	}

	@DeleteMapping
	public ResponseEntity<String> deleteAllStudentsByProject(@RequestHeader("Authorization") String token, @RequestParam Integer projectId) {
		studentService.deleteAllStudentsByProject(token, projectId);
		CustomLogger.info(responseMessage.deleteAllFromCurrentProject());
		return ResponseEntity.ok(responseMessage.deleteAllFromCurrentProject());
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
	@PostMapping("/upload")
	public ResponseEntity<String> handleFileUpload(@RequestHeader("Authorization") String token, @RequestParam("file-upload") MultipartFile file, @RequestParam("projectId") Integer projectId) throws IOException, CsvValidationException {
		studentService.populateDatabaseFromCSV(token, file, projectId);
		return ResponseEntity.ok("File uploaded successfully");
	}

	@GetMapping("/download")
	public ResponseEntity<byte[]> downloadStudentsCSV(@RequestHeader("Authorization") String token, @RequestParam Integer projectId) throws IOException {
		byte[] studentsCSV = studentService.createStudentsCSV(token, projectId);
		return ResponseEntity.ok(studentsCSV);
	}

}
