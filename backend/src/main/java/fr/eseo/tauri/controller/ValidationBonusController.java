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
@RequestMapping("/api/bonuses/{bonusId}/validated")
@Tag(name = "validationBonuses")
public class ValidationBonusController {

    private final ValidationBonusService validationBonusService;
    private final ResponseMessage responseMessage = new ResponseMessage("validationBonus");

    @GetMapping("/{authorId}")
    public ResponseEntity<Boolean> checkUserValidatedById(@RequestHeader("Authorization") String token, @PathVariable Integer bonusId, @PathVariable Integer authorId) {
        var userValidated = validationBonusService.checkUserValidatedById(token, bonusId, authorId);
        return ResponseEntity.ok(userValidated);
    }

    @GetMapping
    public ResponseEntity<List<User>> getAllUsersValidated(@RequestHeader("Authorization") String token, @PathVariable Integer bonusId) {
        var usersValidated = validationBonusService.getAllUsersValidated(token, bonusId);
        return ResponseEntity.ok(usersValidated);
    }

    @PostMapping
    public ResponseEntity<String> createValidationBonus(@RequestHeader("Authorization") String token, @Validated(Create.class) @RequestBody ValidationBonus validationBonus) {
        validationBonusService.createValidationBonus(token, validationBonus);
        CustomLogger.info(responseMessage.create());
        return ResponseEntity.ok(responseMessage.create());
    }

    @DeleteMapping("/{authorId}")
    public ResponseEntity<String> deleteValidationBonusByAuthorId(@RequestHeader("Authorization") String token, @PathVariable Integer bonusId, @PathVariable Integer authorId) {
        validationBonusService.deleteValidationBonusByAuthorId(token, bonusId, authorId);
        CustomLogger.info(responseMessage.delete());
        return ResponseEntity.ok(responseMessage.delete());
    }

}