package fr.eseo.tauri.controller;

import fr.eseo.tauri.model.GradeType;
import fr.eseo.tauri.repository.GradeTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/grade_types")
public class GradeTypeController {

    private final GradeTypeRepository gradeTypeRepository;

    @Autowired
    public GradeTypeController(GradeTypeRepository gradeTypeRepository) {
        this.gradeTypeRepository = gradeTypeRepository;
    }

    @GetMapping
    public ResponseEntity<Iterable<GradeType>> getGradeTypes() {
        return ResponseEntity.ok(gradeTypeRepository.findAll());
    }

    @PatchMapping("/{id}")
    public ResponseEntity<GradeType> partialUpdateGradeType(@PathVariable Integer id, @RequestBody GradeType gradeType) {
        GradeType gradeTypeToUpdate = gradeTypeRepository.findById(id).orElse(null);
        if (gradeTypeToUpdate == null) {
            return ResponseEntity.notFound().build();
        }

        if (gradeType.factor() != null) gradeTypeToUpdate.factor(gradeType.factor());

        return ResponseEntity.ok(gradeTypeRepository.save(gradeTypeToUpdate));
    }

    @PostMapping("/add")
    public GradeType addGradeType(@RequestBody GradeType gradeType) {
        return gradeTypeRepository.save(gradeType);
    }

    @GetMapping("/all")
    public Iterable<GradeType> getAllGradeTypes() {
        return gradeTypeRepository.findAll();
    }

    @GetMapping("/{id}")
    public GradeType getGradeTypeById(@PathVariable Integer id) {
        return gradeTypeRepository.findById(id).orElse(null);
    }

    @PutMapping("/update/{id}")
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

    @DeleteMapping("/delete/{id}")
    public String deleteGradeType(@PathVariable Integer id) {
        gradeTypeRepository.deleteById(id);
        return "GradeType deleted";
    }
}
