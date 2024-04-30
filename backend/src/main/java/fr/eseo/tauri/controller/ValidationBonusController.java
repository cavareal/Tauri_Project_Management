package fr.eseo.tauri.controller;

import fr.eseo.tauri.model.ValidationBonus;
import fr.eseo.tauri.model.id_class.ValidationBonusId;
import fr.eseo.tauri.service.ValidationBonusService;
import fr.eseo.tauri.util.CustomLogger;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/validation-bonuses")
@Tag(name = "validation-bonuses")
public class ValidationBonusController {

    private final ValidationBonusService validationBonusService;

    @GetMapping
    public ResponseEntity<List<ValidationBonus>> getAllValidationBonuses(@RequestHeader("Authorization") String token) {
        List<ValidationBonus> validationBonuses = validationBonusService.getAllValidationBonuses(token);
        return ResponseEntity.ok(validationBonuses);
    }

    /*@GetMapping("/{id}")
    public ResponseEntity<ValidationBonus> getValidationBonusById(@RequestHeader("Authorization") String token, @PathVariable ValidationBonusId id) {
        /*ValidationBonus validationBonus = validationBonusService.getValidationBonusById(token, id);
        return ResponseEntity.ok(validationBonus);
    }*/

    @PostMapping
    public ResponseEntity<String> addValidationBonuses(@RequestHeader("Authorization") String token, @RequestBody List<ValidationBonus> validationBonuses) {
        validationBonusService.addValidationBonuses(token, validationBonuses);
        CustomLogger.info("The validation bonus(es) have been added");
        return ResponseEntity.ok("The validation bonus(es) have been added");
    }

    @PatchMapping("/{id}")
    public ResponseEntity<String> updateValidationBonus(@RequestHeader("Authorization") String token, @PathVariable ValidationBonusId id, @RequestBody Map<String, Object> request) {
        validationBonusService.updateValidationBonus(token, id, request);
        CustomLogger.info("The validation bonus has been updated");
        return ResponseEntity.ok("The validation bonus has been updated");
    }

    @DeleteMapping
    public ResponseEntity<String> deleteAllValidationBonuses(@RequestHeader("Authorization") String token) {
        validationBonusService.deleteAllValidationBonuses(token);
        CustomLogger.info("All the validation bonuses have been deleted");
        return ResponseEntity.ok("All the validation bonuses have been deleted");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteValidationBonus(@RequestHeader("Authorization") String token, @PathVariable ValidationBonusId id) {
        validationBonusService.deleteValidationBonus(token, id);
        CustomLogger.info("The validation bonus has been deleted");
        return ResponseEntity.ok("The validation bonus has been deleted");
    }
}
