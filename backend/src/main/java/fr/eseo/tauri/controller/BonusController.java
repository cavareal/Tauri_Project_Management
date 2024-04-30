package fr.eseo.tauri.controller;

import fr.eseo.tauri.model.Bonus;
import fr.eseo.tauri.service.BonusService;
import fr.eseo.tauri.util.CustomLogger;
import fr.eseo.tauri.validator.bonus.CreateBonusValidator;
import fr.eseo.tauri.validator.bonus.UpdateBonusValidator;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/bonuses")
@Tag(name = "bonuses")
public class BonusController {

    private final BonusService bonusService;

    @GetMapping
    public ResponseEntity<List<Bonus>> getAllBonuses(@RequestHeader("Authorization") String token) {
        List<Bonus> bonuses = bonusService.getAllBonuses(token);
        return ResponseEntity.ok(bonuses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Bonus> getBonusById(@RequestHeader("Authorization") String token, @PathVariable Integer id) {
        Bonus bonus = bonusService .getBonusById(token, id);
        return ResponseEntity.ok(bonus);
    }

    @PostMapping
    public ResponseEntity<String> addBonuses(@RequestHeader("Authorization") String token, @RequestBody List<CreateBonusValidator> bonusesDetails) {
        bonusService.addBonuses(token, bonusesDetails);
        CustomLogger.logInfo("The bonus(es) have been added");
        return ResponseEntity.ok("The bonus(es) have been added");
    }

    @PatchMapping("/{id}")
    public ResponseEntity<String> updateBonus(@RequestHeader("Authorization") String token, @PathVariable Integer id, @RequestBody UpdateBonusValidator bonusDetails) {
        bonusService.updateBonus(token, id, bonusDetails);
        CustomLogger.logInfo("The bonus has been updated");
        return ResponseEntity.ok("The bonus has been updated");
    }

    @DeleteMapping
    public ResponseEntity<String> deleteAllBonuses(@RequestHeader("Authorization") String token) {
        bonusService.deleteAllBonuses(token);
        CustomLogger.logInfo("All the bonuses have been deleted");
        return ResponseEntity.ok("All the bonuses have been deleted");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteBonus(@RequestHeader("Authorization") String token, @PathVariable Integer id) {
        bonusService.deleteBonus(token, id);
        CustomLogger.logInfo("The bonus has been deleted");
        return ResponseEntity.ok("The bonus has been deleted");
    }
}
