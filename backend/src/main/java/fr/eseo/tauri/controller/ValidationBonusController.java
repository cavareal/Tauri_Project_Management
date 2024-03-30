package fr.eseo.tauri.controller;

import fr.eseo.tauri.model.ValidationBonus;
import fr.eseo.tauri.repository.ValidationBonusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/validation_bonuses")
public class ValidationBonusController {

    private final ValidationBonusRepository validationBonusRepository;

    @Autowired
    public ValidationBonusController(ValidationBonusRepository validationBonusRepository) {
        this.validationBonusRepository = validationBonusRepository;
    }
    @PostMapping("/add")
    public ValidationBonus addValidationBonus(@RequestBody ValidationBonus validationBonus) {
        return validationBonusRepository.save(validationBonus);
    }

    @GetMapping("/all")
    public Iterable<ValidationBonus> getAllValidationBonuses() {
        return validationBonusRepository.findAll();
    }

    @GetMapping("/{id}")
    public ValidationBonus getValidationBonusById(@PathVariable Integer id) {
        return validationBonusRepository.findById(id).orElse(null);
    }

    @PutMapping("/update/{id}")
    public ValidationBonus updateValidationBonus(@PathVariable Integer id, @RequestBody ValidationBonus validationBonusDetails) {
        ValidationBonus validationBonus = validationBonusRepository.findById(id).orElse(null);
        if (validationBonus != null) {
            validationBonus.confirmed(validationBonusDetails.confirmed());
            return validationBonusRepository.save(validationBonus);
        }
        return null;
    }

    @DeleteMapping("/delete/{id}")
    public String deleteValidationBonus(@PathVariable Integer id) {
        validationBonusRepository.deleteById(id);
        return "ValidationBonus deleted";
    }
}
