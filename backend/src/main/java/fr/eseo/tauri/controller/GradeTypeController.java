package fr.eseo.tauri.controller;

import fr.eseo.tauri.model.GradeType;
import fr.eseo.tauri.repository.GradeTypeRepository;
import fr.eseo.tauri.service.GradeTypeService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/grade-types")
@Tag(name = "grade-types")
public class GradeTypeController {

    private final GradeTypeService gradeTypeService;
    private final GradeTypeRepository gradeTypeRepository;

    @GetMapping("/{id}")
    public ResponseEntity<GradeType> getGradeTypeById(@RequestHeader("Authorization") String token, @PathVariable Integer id) {
        GradeType gradeType = gradeTypeService.getGradeTypeById(token, id);
        return ResponseEntity.ok(gradeType);
    }

    @GetMapping
    public ResponseEntity<List<GradeType>> getAllGradeTypes(@RequestHeader("Authorization") String token) {
        List<GradeType> gradeTypes = gradeTypeService.getAllGradeTypes(token);
        return ResponseEntity.ok(gradeTypes);
    }

    @PostMapping
    public ResponseEntity<String> createGradeType(@RequestHeader("Authorization") String token, @RequestBody GradeType gradeType) {
        gradeTypeService.createGradeType(token, gradeType);
        return ResponseEntity.ok("GradeType created successfully.");
    }

    @PatchMapping("/{id}")
    public ResponseEntity<String> updateGradeType(@RequestHeader("Authorization") String token, @PathVariable Integer id, @RequestBody GradeType updatedGradeType) {
        gradeTypeService.updateGradeType(token, id, updatedGradeType);
        return ResponseEntity.ok("GradeType updated successfully.");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteGradeTypeById(@RequestHeader("Authorization") String token, @PathVariable Integer id) {
        gradeTypeService.deleteGradeTypeById(token, id);
        return ResponseEntity.ok("GradeType deleted successfully.");
    }

    @DeleteMapping
    public ResponseEntity<String> deleteAllGradeTypes(@RequestHeader("Authorization") String token) {
        gradeTypeService.deleteAllGradeTypes(token);
        return ResponseEntity.ok("All GradeTypes deleted successfully.");
    }

    /*@GetMapping
    public ResponseEntity<Iterable<GradeType>> getGradeTypes() {
        return ResponseEntity.ok(gradeTypeRepository.findAll());
    }*/

    /*@PatchMapping("/{id}")
    public ResponseEntity<GradeType> partialUpdateGradeType(@PathVariable Integer id, @RequestBody GradeType gradeType) {
        if (gradeType.factor() == null) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(gradeTypeService.updateFactor(id, gradeType.factor()));
    }*/

    @PostMapping("/")
    public GradeType addGradeType(@RequestBody GradeType gradeType) {
        return gradeTypeRepository.save(gradeType);
    }

    @GetMapping("/")
    public Iterable<GradeType> getAllGradeTypes() {
        return gradeTypeRepository.findAll();
    }

    /*@GetMapping("/{id}")
    public GradeType getGradeTypeById(@PathVariable Integer id) {
        return gradeTypeRepository.findById(id).orElse(null);
    }*/

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

    /*@DeleteMapping("/{id}")
    public String deleteGradeType(@PathVariable Integer id) {
        gradeTypeRepository.deleteById(id);
        return "GradeType deleted";
    }*/

}
