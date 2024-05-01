package fr.eseo.tauri.controller;

import fr.eseo.tauri.model.Bonus;
import fr.eseo.tauri.service.BonusService;
import fr.eseo.tauri.util.CustomLogger;
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
@RequestMapping("/api/bonuses")
@Tag(name = "bonuses")
public class BonusController {

    private final BonusService bonusService;

    @GetMapping("/{id}")
    public ResponseEntity<Bonus> getBonusById(@RequestHeader("Authorization") String token, @PathVariable Integer id) {
        Bonus bonus = bonusService .getBonusById(token, id);
        return ResponseEntity.ok(bonus);
    }

    @GetMapping
    public ResponseEntity<List<Bonus>> getAllBonusesByProject(@RequestHeader("Authorization") String token, @RequestParam("projectId") Integer projectId) {
        List<Bonus> bonuses = bonusService.getAllBonusesByProject(token, projectId);
        return ResponseEntity.ok(bonuses);
    }

    @PostMapping
    public ResponseEntity<String> createBonus(@RequestHeader("Authorization") String token, @Validated(Create.class) @RequestBody Bonus bonus) {
        bonusService.createBonus(token, bonus);
        CustomLogger.info("The bonus(es) have been added");
        return ResponseEntity.ok("The bonus(es) have been added");
    }

    @PatchMapping("/{id}")
    public ResponseEntity<String> updateBonus(@RequestHeader("Authorization") String token, @PathVariable Integer id, @Validated(Update.class)@RequestBody Bonus updatedBonus) {
        bonusService.updateBonus(token, id, updatedBonus);
        CustomLogger.info("The bonus has been updated");
        return ResponseEntity.ok("The bonus has been updated");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteBonus(@RequestHeader("Authorization") String token, @PathVariable Integer id) {
        bonusService.deleteBonus(token, id);
        CustomLogger.info("The bonus has been deleted");
        return ResponseEntity.ok("The bonus has been deleted");
    }

    @DeleteMapping
    public ResponseEntity<String> deleteAllBonuses(@RequestHeader("Authorization") String token, @RequestParam("projectId") Integer projectId) {
        bonusService.deleteAllBonuses(token, projectId);
        CustomLogger.info("All the bonuses have been deleted");
        return ResponseEntity.ok("All the bonuses have been deleted");
    }

}
