package fr.eseo.tauri.controller;

import fr.eseo.tauri.model.ValidationBonus;
import fr.eseo.tauri.service.ValidationBonusService;
import fr.eseo.tauri.util.CustomLogger;
import fr.eseo.tauri.util.ResponseMessage;
import fr.eseo.tauri.util.valid.Create;
import fr.eseo.tauri.util.valid.Update;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/validationBonuses")
@Tag(name = "validationBonuses")
public class ValidationBonusController {

    private final ValidationBonusService validationBonusService;
    private final ResponseMessage responseMessage = new ResponseMessage("validationBonus");

    @GetMapping("/{id}")
    public ResponseEntity<ValidationBonus> getValidationBonusById(@RequestHeader("Authorization") String token, @PathVariable Integer id) {
        ValidationBonus validationBonus = validationBonusService.getValidationBonusById(token, id);
        return ResponseEntity.ok(validationBonus);
    }

    @GetMapping
    public ResponseEntity<List<ValidationBonus>> getAllValidationBonusesByProject(@RequestHeader("Authorization") String token, @RequestParam("projectId") Integer projectId) {
        List<ValidationBonus> validationBonuses = validationBonusService.getAllValidationBonusesByProject(token, projectId);
        return ResponseEntity.ok(validationBonuses);
    }

    @PostMapping
    public ResponseEntity<String> createValidationBonus(@RequestHeader("Authorization") String token, @Validated(Create.class) @RequestBody ValidationBonus validationBonus) {
        validationBonusService.createValidationBonus(token, validationBonus);
        CustomLogger.info(responseMessage.create());
        return ResponseEntity.ok(responseMessage.create());
    }

    @PatchMapping("/{id}")
    public ResponseEntity<String> updateValidationBonus(@RequestHeader("Authorization") String token, @PathVariable Integer id, @Validated(Update.class) @RequestBody ValidationBonus updatedValidationBonus) {
        validationBonusService.updateValidationBonus(token, id, updatedValidationBonus);
        CustomLogger.info(responseMessage.update());
        return ResponseEntity.ok(responseMessage.update());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteValidationBonus(@RequestHeader("Authorization") String token, @PathVariable Integer id) {
        validationBonusService.deleteValidationBonus(token, id);
        CustomLogger.info(responseMessage.delete());
        return ResponseEntity.ok(responseMessage.delete());
    }

    @DeleteMapping
    public ResponseEntity<String> deleteAllValidationBonusesByProject(@RequestHeader("Authorization") String token, @RequestParam("projectId") Integer projectId) {
        validationBonusService.deleteAllValidationBonusesByProject(token, projectId);
        CustomLogger.info(responseMessage.deleteAllFromCurrentProject());
        return ResponseEntity.ok(responseMessage.deleteAllFromCurrentProject());
    }
}