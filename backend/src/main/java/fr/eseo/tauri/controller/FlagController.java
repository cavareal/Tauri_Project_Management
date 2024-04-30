package fr.eseo.tauri.controller;

import fr.eseo.tauri.model.Flag;
import fr.eseo.tauri.service.FlagService;
import fr.eseo.tauri.util.CustomLogger;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/flags")
@Tag(name = "flags")
public class FlagController {

    private final FlagService flagService;

    @GetMapping
    public ResponseEntity<List<Flag>> getAllFlags(@RequestHeader("Authorization") String token) {
        List<Flag> flags = flagService.getAllFlags(token);
        return ResponseEntity.ok(flags);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Flag> getFlagById(@RequestHeader("Authorization") String token, @PathVariable Integer id) {
        Flag flag = flagService.getFlagById(token, id);
        return ResponseEntity.ok(flag);
    }

    @PostMapping
    public ResponseEntity<String> addFlags(@RequestHeader("Authorization") String token, @RequestBody CreateFlagValidator flag) {
        flagService.addFlag(token, flag);
        CustomLogger.logInfo("The flag has been created");
        return ResponseEntity.ok("The flag has been created");
    }

    @PatchMapping("/{id}")
    public ResponseEntity<String> updateFlag(@RequestHeader("Authorization") String token, @PathVariable Integer id, @RequestBody Map<String, Object> request) {
        flagService.updateFlag(token, id, request);
        CustomLogger.logInfo("The flag has been updated");
        return ResponseEntity.ok("The flag has been updated");
    }

    @DeleteMapping
    public ResponseEntity<String> deleteAllFlags(@RequestHeader("Authorization") String token) {
        flagService.deleteAllFlags(token);
        CustomLogger.logInfo("All the flags have been deleted");
        return ResponseEntity.ok("All the flags have been deleted");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteFlag(@RequestHeader("Authorization") String token, @PathVariable Integer id) {
        flagService.deleteFlag(token, id);
        CustomLogger.logInfo("The flag has been deleted");
        return ResponseEntity.ok("The flag has been deleted");
    }
}

