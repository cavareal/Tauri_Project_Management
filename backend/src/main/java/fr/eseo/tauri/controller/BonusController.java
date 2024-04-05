package fr.eseo.tauri.controller;

import fr.eseo.tauri.model.Bonus;
import fr.eseo.tauri.repository.BonusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/bonuses")
public class BonusController {

    private final BonusRepository bonusRepository;

    @Autowired
    public BonusController(BonusRepository bonusRepository) {
        this.bonusRepository = bonusRepository;
    }

    @PostMapping("/add")
    public Bonus addBonus(@RequestBody Bonus bonus) {
        return bonusRepository.save(bonus);
    }

    @GetMapping("/all")
    public Iterable<Bonus> getAllBonuses() {
        return bonusRepository.findAll();
    }

    @GetMapping("/{id}")
    public Bonus getBonusById(@PathVariable Integer id) {
        return bonusRepository.findById(id).orElse(null);
    }

    @PutMapping("/update/{id}")
    public Bonus updateBonus(@PathVariable Integer id, @RequestBody Bonus bonusDetails) {
        Bonus bonus = bonusRepository.findById(id).orElse(null);
        if (bonus != null) {
            bonus.value(bonusDetails.value());
            bonus.comment(bonusDetails.comment());
            bonus.limited(bonusDetails.limited());
//            bonus.confirmed(bonusDetails.confirmed());
            return bonusRepository.save(bonus);
        }
        return null;
    }

    @DeleteMapping("/delete/{id}")
    public String deleteBonus(@PathVariable Integer id) {
        bonusRepository.deleteById(id);
        return "Bonus deleted";
    }
}
