package fr.eseo.tauri.controller;

import fr.eseo.tauri.model.Bonus;
import fr.eseo.tauri.repository.BonusRepository;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/bonuses")
@Tag(name = "bonuses")
public class BonusController {

    private final BonusRepository bonusRepository;

    @PostMapping("/")
    public Bonus addBonus(@RequestBody Bonus bonus) {
        return bonusRepository.save(bonus);
    }

    @GetMapping("/")
    public Iterable<Bonus> getAllBonuses() {
        return bonusRepository.findAll();
    }

    @GetMapping("/{id}")
    public Bonus getBonusById(@PathVariable Integer id) {
        return bonusRepository.findById(id).orElse(null);
    }

    @PutMapping("/{id}")
    public Bonus updateBonus(@PathVariable Integer id, @RequestBody Bonus bonusDetails) {
        Bonus bonus = bonusRepository.findById(id).orElse(null);
        if (bonus != null) {
            bonus.value(bonusDetails.value());
            bonus.comment(bonusDetails.comment());
            bonus.limited(bonusDetails.limited());
            return bonusRepository.save(bonus);
        }
        return null;
    }

    @DeleteMapping("/{id}")
    public String deleteBonus(@PathVariable Integer id) {
        bonusRepository.deleteById(id);
        return "Bonus deleted";
    }
}
