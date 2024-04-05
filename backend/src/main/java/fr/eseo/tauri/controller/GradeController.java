package fr.eseo.tauri.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.eseo.tauri.model.Grade;
import fr.eseo.tauri.model.GradeType;
import java.util.Map;
import fr.eseo.tauri.repository.GradeRepository;
import fr.eseo.tauri.service.GradeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/grades")
public class GradeController {

    private final GradeRepository gradeRepository;
    private final GradeService gradeService;

    @Autowired
    public GradeController(GradeRepository gradeRepository, GradeService gradeService) {
        this.gradeRepository = gradeRepository;
        this.gradeService = gradeService;
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

    @PostMapping("/addOverallPerformance")
    public ResponseEntity<Map<String, String>> addGradeOverallPerformance(@RequestBody String evaluations){
        try {
            System.out.println(evaluations);

            GradeType gradeType = new GradeType("Performance Gloable");
            ObjectMapper objectMapper = new ObjectMapper();
            TypeReference<Map<String, List<Map<String, Object>>>> typeReference = new TypeReference<Map<String, List<Map<String, Object>>>>() {};
            Map<String, List<Map<String, Object>>> evaluationsMap = objectMapper.readValue(evaluations, typeReference);

            for (Map.Entry<String, List<Map<String, Object>>> entry : evaluationsMap.entrySet()) {
                String teamName = entry.getKey();
                List<Map<String, Object>> evaluationsList = entry.getValue();

                for (Map<String, Object> evaluation : evaluationsList) {
                    Integer value = (Integer) evaluation.get("note");
                    gradeService.assignGradeToTeam(teamName, value, gradeType);
                }
            }
            return ResponseEntity.ok(Map.of("message", "Grades added successfully."));
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().body(Map.of("error", "Invalid number format for grade value."));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error occurred while adding grades: " + e.getMessage()));
        }
    }
}
