package fr.eseo.tauri.controller;

import fr.eseo.tauri.model.GradeType;
import fr.eseo.tauri.service.GradeTypeService;
import fr.eseo.tauri.util.ResponseMessage;
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
    private final ResponseMessage responseMessage = new ResponseMessage("gradetype");

    @GetMapping("/{id}")
    public ResponseEntity<GradeType> getGradeTypeById(@RequestHeader("Authorization") String token, @PathVariable Integer id) {
        GradeType gradeType = gradeTypeService.getGradeTypeById(token, id);
        return ResponseEntity.ok(gradeType);
    }

    @GetMapping("/imported")
    public ResponseEntity<List<GradeType>> getAllImportedGradeTypes(@RequestHeader("Authorization") String token, @RequestParam("projectId") Integer projectId) {
        List<GradeType> importedGradeTypes = gradeTypeService.getAllImportedGradeTypes(token, projectId);
        return ResponseEntity.ok(importedGradeTypes);
    }

    @GetMapping("/unimported")
    public ResponseEntity<List<GradeType>> getAllUnimportedGradeTypes(@RequestHeader("Authorization") String token,  @RequestParam("projectId") Integer projectId) {
        List<GradeType> gradeTypes = gradeTypeService.getAllUnimportedGradeTypes(token, projectId);
        return ResponseEntity.ok(gradeTypes);
    }

    @PostMapping
    public ResponseEntity<String> createGradeType(@RequestHeader("Authorization") String token, @RequestBody GradeType gradeType) {
        gradeTypeService.createGradeType(token, gradeType);
        return ResponseEntity.ok(responseMessage.create());
    }

    @PatchMapping("/{id}")
    public ResponseEntity<String> updateGradeType(@RequestHeader("Authorization") String token, @PathVariable Integer id, @RequestBody GradeType updatedGradeType) {
        gradeTypeService.updateGradeType(token, id, updatedGradeType);
        return ResponseEntity.ok(responseMessage.update());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteGradeTypeById(@RequestHeader("Authorization") String token, @PathVariable Integer id) {
        gradeTypeService.deleteGradeTypeById(token, id);
        return ResponseEntity.ok(responseMessage.delete());
    }

    @DeleteMapping("/imported")
    public ResponseEntity<String> deleteAllImportedGradeTypes(@RequestHeader("Authorization") String token) {
        gradeTypeService.deleteAllImportedGradeTypes(token);
        return ResponseEntity.ok(responseMessage.deleteAll());
    }

    @DeleteMapping("/unimported")
    public ResponseEntity<String> deleteAllUnimportedGradeTypes(@RequestHeader("Authorization") String token) {
        gradeTypeService.deleteAllUnimportedGradeTypes(token);
        return ResponseEntity.ok(responseMessage.deleteAll());
    }

    @GetMapping("/name")
    public ResponseEntity<GradeType> getGradeTypeByName(@RequestHeader("Authorization") String token, @RequestParam("name") String name) {
        GradeType gradeType = gradeTypeService.findByName(name, token);
        return ResponseEntity.ok(gradeType);
    }
}
