package fr.eseo.tauri.controller;

import fr.eseo.tauri.model.ValidationFlag;
import fr.eseo.tauri.repository.ValidationFlagRepository;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/validation_flags")
@Tag(name = "validation-flags")
public class ValidationFlagController {

    private final ValidationFlagRepository validationFlagRepository;

    @Autowired
    public ValidationFlagController(ValidationFlagRepository validationFlagRepository) {
        this.validationFlagRepository = validationFlagRepository;
    }

    @PostMapping("/add")
    public ValidationFlag addValidationFlag(@RequestBody ValidationFlag validationFlag) {
        return validationFlagRepository.save(validationFlag);
    }

    @GetMapping("/all")
    public Iterable<ValidationFlag> getAllValidationFlags() {
        return validationFlagRepository.findAll();
    }

    @GetMapping("/{id}")
    public ValidationFlag getValidationFlagById(@PathVariable Integer id) {
        return validationFlagRepository.findById(id).orElse(null);
    }

    @PutMapping("/update/{id}")
    public ValidationFlag updateValidationFlag(@PathVariable Integer id, @RequestBody ValidationFlag validationFlagDetails) {
        ValidationFlag validationFlag = validationFlagRepository.findById(id).orElse(null);
        if (validationFlag != null) {
            validationFlag.confirmed(validationFlagDetails.confirmed());
            return validationFlagRepository.save(validationFlag);
        }
        return null;
    }

    @DeleteMapping("/delete/{id}")
    public String deleteValidationFlag(@PathVariable Integer id) {
        validationFlagRepository.deleteById(id);
        return "ValidationFlag deleted";
    }
}
