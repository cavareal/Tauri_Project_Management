package fr.eseo.tauri.controller;

import fr.eseo.tauri.model.Flag;
import fr.eseo.tauri.repository.FlagRepository;
import fr.eseo.tauri.service.AuthService;
import fr.eseo.tauri.service.FlagService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/flags")
@Tag(name = "flags")
public class FlagController {

    private final FlagRepository flagRepository;
    private final FlagService flagService;
    private final AuthService authService;

    @Autowired
    public FlagController(FlagRepository flagRepository, FlagService flagService, AuthService authService) {
        this.flagRepository = flagRepository;
        this.flagService = flagService;
        this.authService = authService;
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

    @PostMapping("/")
    public ResponseEntity<Flag> addFlag(@RequestBody Flag flag, @RequestHeader("Authorization") String token){
        String permission = "create Flag";
        if (Boolean.TRUE.equals(authService.checkAuth(token, permission))) {
            try{
                return ResponseEntity.status(HttpStatus.OK).body(flagService.addFlag(flag));
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
            }
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
    }
}

