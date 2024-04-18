package fr.eseo.tauri.controller;

import fr.eseo.tauri.model.GradeType;
import fr.eseo.tauri.repository.GradeTypeRepository;
import fr.eseo.tauri.service.GradeTypeService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/grade-types")
@Tag(name = "grade-types")
public class GradeTypeController {

    private final GradeTypeRepository gradeTypeRepository;
    private final GradeTypeService gradeTypeService;

    @Autowired
    public GradeTypeController(GradeTypeRepository gradeTypeRepository, GradeTypeService gradeTypeService) {
        this.gradeTypeRepository = gradeTypeRepository;
        this.gradeTypeService = gradeTypeService;
    }

    @GetMapping
    public ResponseEntity<Iterable<GradeType>> getGradeTypes() {
        return ResponseEntity.ok(gradeTypeRepository.findAll());
    }

    @PatchMapping("/{id}")
    public ResponseEntity<GradeType> partialUpdateGradeType(@PathVariable Integer id, @RequestBody GradeType gradeType) {
        if (gradeType.factor() == null) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(gradeTypeService.updateFactor(id, gradeType.factor()));
    }

    @PostMapping("/")
    public GradeType addGradeType(@RequestBody GradeType gradeType) {
        return gradeTypeRepository.save(gradeType);
    }

    @GetMapping("/")
    public Iterable<GradeType> getAllGradeTypes() {
        return gradeTypeRepository.findAll();
    }

    @GetMapping("/{id}")
    public GradeType getGradeTypeById(@PathVariable Integer id) {
        return gradeTypeRepository.findById(id).orElse(null);
    }

    @PutMapping("/{id}")
    public GradeType updateGradeType(@PathVariable Integer id, @RequestBody GradeType gradeTypeDetails) {
        GradeType gradeType = gradeTypeRepository.findById(id).orElse(null);
        if (gradeType != null) {
            gradeType.name(gradeTypeDetails.name());
            gradeType.factor(gradeTypeDetails.factor());
            gradeType.forGroup(gradeTypeDetails.forGroup());
            gradeType.imported(gradeTypeDetails.imported());
            return gradeTypeRepository.save(gradeType);
        }
        return null;
    }

    @DeleteMapping("/{id}")
    public String deleteGradeType(@PathVariable Integer id) {
        gradeTypeRepository.deleteById(id);
        return "GradeType deleted";
    }
}
