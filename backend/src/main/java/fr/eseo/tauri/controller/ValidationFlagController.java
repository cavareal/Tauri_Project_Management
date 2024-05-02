package fr.eseo.tauri.controller;

import fr.eseo.tauri.model.ValidationFlag;
import fr.eseo.tauri.service.ValidationFlagService;
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
@RequestMapping("/api/validationFlags")
@Tag(name = "validationFlags")
public class ValidationFlagController {

    private final ValidationFlagService validationFlagService;
    private final ResponseMessage responseMessage = new ResponseMessage("validationFlag");

    @GetMapping("/{id}")
    public ResponseEntity<ValidationFlag> getValidationFlagById(@RequestHeader("Authorization") String token, @PathVariable Integer id) {
        ValidationFlag validationFlag = validationFlagService.getValidationFlagById(token, id);
        return ResponseEntity.ok(validationFlag);
    }

    @GetMapping
    public ResponseEntity<List<ValidationFlag>> getAllValidationFlagsByProject(@RequestHeader("Authorization") String token, @RequestParam Integer projectId) {
        List<ValidationFlag> validationFlags = validationFlagService.getAllValidationFlagsByProject(token, projectId);
        return ResponseEntity.ok(validationFlags);
    }

    @PostMapping
    public ResponseEntity<String> createValidationFlag(@RequestHeader("Authorization") String token, @Validated(Create.class) @RequestBody ValidationFlag validationFlag) {
        validationFlagService.createValidationFlag(token, validationFlag);
        CustomLogger.info(responseMessage.create());
        return ResponseEntity.ok(responseMessage.create());
    }

    @PatchMapping("/{id}")
    public ResponseEntity<String> updateValidationFlag(@RequestHeader("Authorization") String token, @PathVariable Integer id, @Validated(Update.class) @RequestBody ValidationFlag updatedValidationFlag) {
        validationFlagService.updateValidationFlag(token, id, updatedValidationFlag);
        CustomLogger.info(responseMessage.update());
        return ResponseEntity.ok(responseMessage.update());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteValidationFlag(@RequestHeader("Authorization") String token, @PathVariable Integer id) {
        validationFlagService.deleteValidationFlag(token, id);
        CustomLogger.info(responseMessage.delete());
        return ResponseEntity.ok(responseMessage.delete());
    }

    @DeleteMapping
    public ResponseEntity<String> deleteAllValidationFlagsByProject(@RequestHeader("Authorization") String token, @RequestParam Integer projectId) {
        validationFlagService.deleteAllValidationFlagsByProject(token, projectId);
        CustomLogger.info(responseMessage.deleteAllFromCurrentProject());
        return ResponseEntity.ok(responseMessage.deleteAllFromCurrentProject());
    }
}