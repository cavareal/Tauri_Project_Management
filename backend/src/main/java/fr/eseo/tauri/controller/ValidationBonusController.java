package fr.eseo.tauri.controller;

import fr.eseo.tauri.model.User;
import fr.eseo.tauri.model.ValidationBonus;
import fr.eseo.tauri.service.ValidationBonusService;
import fr.eseo.tauri.util.CustomLogger;
import fr.eseo.tauri.util.ResponseMessage;
import fr.eseo.tauri.util.valid.Create;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/bonuses/{bonusId}/validation")
@Tag(name = "validationBonuses")
public class ValidationBonusController {

    private final ValidationBonusService validationBonusService;
    private final ResponseMessage responseMessage = new ResponseMessage("validationBonus");

    @GetMapping("/{authorId}")
    public ResponseEntity<ValidationBonus> getValidationBonusByAuthorId(@RequestHeader("Authorization") String token, @PathVariable Integer bonusId, @PathVariable Integer authorId) {
        var validationBonus = validationBonusService.getValidationBonusByAuthorId(token, bonusId, authorId);
        return ResponseEntity.ok(validationBonus);
    }

    @GetMapping
    public ResponseEntity<List<ValidationBonus>> getAllValidationBonuses(@RequestHeader("Authorization") String token, @PathVariable Integer bonusId) {
        List<ValidationBonus> validationBonuses = validationBonusService.getAllValidationBonuses(token, bonusId);
        return ResponseEntity.ok(validationBonuses);
    }

    @PatchMapping("/{authorId}")
    public ResponseEntity<String> updateValidationBonus(@RequestHeader("Authorization") String token, @PathVariable Integer bonusId, @PathVariable Integer authorId, @Validated(Create.class) @RequestBody ValidationBonus updatedValidationBonus) {
        validationBonusService.updateValidationBonus(token, bonusId, authorId, updatedValidationBonus);
        CustomLogger.info(responseMessage.update());
        return ResponseEntity.ok(responseMessage.update());
    }

}