package fr.eseo.tauri.controller;

import fr.eseo.tauri.model.Grade;
import fr.eseo.tauri.repository.GradeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/grades")
public class GradeController {

    @Autowired
    private GradeRepository gradeRepository;

    @PostMapping("/add")
    public Grade addGrade(@RequestBody Grade grade) {
        return gradeRepository.save(grade);
    }

    @GetMapping("/all")
    public Iterable<Grade> getAllGrades() {
        return gradeRepository.findAll();
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
