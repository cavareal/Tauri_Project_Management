package fr.eseo.tauri.controller;

import fr.eseo.tauri.model.Flag;
import fr.eseo.tauri.repository.FlagRepository;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/flags")
@Tag(name = "flags")
public class FlagController {

    private final FlagRepository flagRepository;

    @PostMapping("/")
    public Flag addFlag(@RequestBody Flag flag) {
        return flagRepository.save(flag);
    }

    @GetMapping("/")
    public Iterable<Flag> getAllFlags() {
        return flagRepository.findAll();
    }

    @GetMapping("/{id}")
    public Flag getFlagById(@PathVariable Integer id) {
        return flagRepository.findById(id).orElse(null);
    }

    @PutMapping("/{id}")
    public Flag updateFlag(@PathVariable Integer id, @RequestBody Flag flagDetails) {
        Flag flag = flagRepository.findById(id).orElse(null);
        if (flag != null) {
            flag.description(flagDetails.description());
            flag.type(flagDetails.type());
            return flagRepository.save(flag);
        }
        return null;
    }

    @DeleteMapping("/{id}")
    public String deleteFlag(@PathVariable Integer id) {
        flagRepository.deleteById(id);
        return "Flag deleted";
    }
}

