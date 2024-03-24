package fr.eseo.tauri.controller;

import fr.eseo.tauri.model.Student;
import fr.eseo.tauri.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/students")
public class StudentController {

    private final StudentRepository studentRepository;
    @Autowired
    public StudentController(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    @PostMapping("/add")
    public Student addStudent(@RequestBody Student student) {
        return studentRepository.save(student);
    }

    @GetMapping("/all")
    public Iterable<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    @GetMapping("/{id}")
    public Student getStudentById(@PathVariable Integer id) {
        return studentRepository.findById(id).orElse(null);
    }

    @PutMapping("/update/{id}")
    public Student updateStudent(@PathVariable Integer id, @RequestBody Student studentDetails) {
        Student student = studentRepository.findById(id).orElse(null);
        if (student != null) {
            student.gender(studentDetails.gender());
            student.bachelor(studentDetails.bachelor());
            student.role(studentDetails.role());
            return studentRepository.save(student);
        }
        return null;
    }

    @DeleteMapping("/delete/{id}")
    public String deleteStudent(@PathVariable Integer id) {
        studentRepository.deleteById(id);
        return "Student deleted";
    }
}
