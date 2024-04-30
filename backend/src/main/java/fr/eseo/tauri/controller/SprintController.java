package fr.eseo.tauri.controller;

import fr.eseo.tauri.model.Sprint;
import fr.eseo.tauri.service.SprintService;
import fr.eseo.tauri.util.CustomLogger;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/sprints")
@Tag(name = "sprints")
public class SprintController {

    private final SprintService sprintService;

    @GetMapping
    public ResponseEntity<List<Sprint>> getAllSprints(@RequestHeader("Authorization") String token) {
        List<Sprint> sprints = sprintService.getAllSprints(token);
        return ResponseEntity.ok(sprints);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Sprint> getSprintById(@RequestHeader("Authorization") String token, @PathVariable Integer id) {
        Sprint sprint = sprintService.getSprintById(token, id);
        return ResponseEntity.ok(sprint);
    }

    @PostMapping
    public ResponseEntity<String> addSprints(@RequestHeader("Authorization") String token, @RequestBody List<Sprint> sprints) {
        sprintService.addSprints(token, sprints);
        CustomLogger.info("The sprint(s) have been added");
        return ResponseEntity.ok("The sprint(s) have been added");
    }

    @PatchMapping("/{id}")
    public ResponseEntity<String> updateSprint(@RequestHeader("Authorization") String token, @PathVariable Integer id, @RequestBody Map<String, Object> request) {
        sprintService.updateSprint(token, id, request);
        CustomLogger.info("The sprint has been updated");
        return ResponseEntity.ok("The sprint has been updated");
    }

    @DeleteMapping
    public ResponseEntity<String> deleteAllSprints(@RequestHeader("Authorization") String token) {
        sprintService.deleteAllSprints(token);
        CustomLogger.info("All the sprints have been deleted");
        return ResponseEntity.ok("All the sprints have been deleted");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteSprint(@RequestHeader("Authorization") String token, @PathVariable Integer id) {
        sprintService.deleteSprint(token, id);
        CustomLogger.info("The sprint has been deleted");
        return ResponseEntity.ok("The sprint has been deleted");
    }
}
