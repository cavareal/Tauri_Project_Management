package fr.eseo.tauri.controller;

import fr.eseo.tauri.model.ValidationFlag;
import fr.eseo.tauri.model.id_class.ValidationFlagId;
import fr.eseo.tauri.service.ValidationFlagService;
import fr.eseo.tauri.util.CustomLogger;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/validation-flags")
@Tag(name = "validation-flags")
public class ValidationFlagController {

    private final ValidationFlagService validationFlagService;

    @GetMapping
    public ResponseEntity<List<ValidationFlag>> getAllValidationFlags(@RequestHeader("Authorization") String token) {
        List<ValidationFlag> validationFlags = validationFlagService.getAllValidationFlags(token);
        return ResponseEntity.ok(validationFlags);
    }

   /* @GetMapping("/{id}")
    public ResponseEntity<ValidationFlag> getValidationFlagById(@RequestHeader("Authorization") String token, @PathVariable ValidationFlagId id) {
        /*ValidationFlag validationFlag = validationFlagService.getValidationFlagById(token, id);
        return ResponseEntity.ok(validationFlag);
    }*/

    @PostMapping
    public ResponseEntity<String> addValidationFlags(@RequestHeader("Authorization") String token, @RequestBody List<ValidationFlag> validationFlags) {
        validationFlagService.addValidationFlags(token, validationFlags);
        CustomLogger.info("The validation flag(s) have been added");
        return ResponseEntity.ok("The validation flag(s) have been added");
    }

    @PatchMapping("/{id}")
    public ResponseEntity<String> updateValidationFlag(@RequestHeader("Authorization") String token, @PathVariable ValidationFlagId id, @RequestBody Map<String, Object> request) {
        validationFlagService.updateValidationFlag(token, id, request);
        CustomLogger.info("The validation flag has been updated");
        return ResponseEntity.ok("The validation flag has been updated");
    }

    @DeleteMapping
    public ResponseEntity<String> deleteAllValidationFlags(@RequestHeader("Authorization") String token) {
        validationFlagService.deleteAllValidationFlags(token);
        CustomLogger.info("All the validation flags have been deleted");
        return ResponseEntity.ok("All the validation flags have been deleted");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteValidationFlag(@RequestHeader("Authorization") String token, @PathVariable ValidationFlagId id) {
        validationFlagService.deleteValidationFlag(token, id);
        CustomLogger.info("The validation flag has been deleted");
        return ResponseEntity.ok("The validation flag has been deleted");
    }
}
