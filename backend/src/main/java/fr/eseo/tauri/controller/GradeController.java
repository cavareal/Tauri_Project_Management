package fr.eseo.tauri.controller;

import fr.eseo.tauri.model.Grade;
import fr.eseo.tauri.repository.GradeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/grades")
public class GradeController {

    private final GradeRepository gradeRepository;

    @Autowired
    public GradeController(GradeRepository gradeRepository) {
        this.gradeRepository = gradeRepository;
    }

    @GetMapping
    public ResponseEntity<Iterable<Grade>> getAllGrades() {
        return ResponseEntity.ok(gradeRepository.findAll());
    }

    @PostMapping("/add")
    public Grade addGrade(@RequestBody Grade grade) {
        return gradeRepository.save(grade);
    }

    @GetMapping("/{id}")
    public Grade getGradeById(@PathVariable Integer id) {
        return gradeRepository.findById(id).orElse(null);
    }

    @PutMapping("/update/{id}")
    public Grade updateGrade(@PathVariable Integer id, @RequestBody Grade gradeDetails) {
        Grade grade = gradeRepository.findById(id).orElse(null);
        if (grade != null) {
            grade.value(gradeDetails.value());
            grade.comment(gradeDetails.comment());
            return gradeRepository.save(grade);
        }
        return null;
    }

    @DeleteMapping("/delete/{id}")
    public String deleteGrade(@PathVariable Integer id) {
        gradeRepository.deleteById(id);
        return "Grade deleted";
    }
}
