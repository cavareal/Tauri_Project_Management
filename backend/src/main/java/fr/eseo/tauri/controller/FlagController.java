package fr.eseo.tauri.controller;

import fr.eseo.tauri.model.Flag;
import fr.eseo.tauri.repository.FlagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/flags")
public class FlagController {

    private final FlagRepository flagRepository;

    @Autowired
    public FlagController(FlagRepository flagRepository) {
        this.flagRepository = flagRepository;
    }

    @PostMapping("/add")
    public Flag addFlag(@RequestBody Flag flag) {
        return flagRepository.save(flag);
    }

    @GetMapping("/all")
    public Iterable<Flag> getAllFlags() {
        return flagRepository.findAll();
    }

    @GetMapping("/{id}")
    public Flag getFlagById(@PathVariable Integer id) {
        return flagRepository.findById(id).orElse(null);
    }

    @PutMapping("/update/{id}")
    public Flag updateFlag(@PathVariable Integer id, @RequestBody Flag flagDetails) {
        Flag flag = flagRepository.findById(id).orElse(null);
        if (flag != null) {
            flag.description(flagDetails.description());
            flag.type(flagDetails.type());
            return flagRepository.save(flag);
        }
        return null;
    }

    @DeleteMapping("/delete/{id}")
    public String deleteFlag(@PathVariable Integer id) {
        flagRepository.deleteById(id);
        return "Flag deleted";
    }
}

