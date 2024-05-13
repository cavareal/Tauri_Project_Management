package fr.eseo.tauri.controller;

import fr.eseo.tauri.model.Sprint;
import fr.eseo.tauri.service.SprintService;
import fr.eseo.tauri.util.CustomLogger;
import fr.eseo.tauri.util.ResponseMessage;
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
@RequestMapping("/api/sprints")
@Tag(name = "sprints")
public class SprintController {

    private final SprintService sprintService;
    private final ResponseMessage responseMessage = new ResponseMessage("sprint");

    @GetMapping("/{id}")
    public ResponseEntity<Sprint> getSprintById(@RequestHeader("Authorization") String token, @PathVariable Integer id) {
        Sprint sprint = sprintService.getSprintById(token, id);
        return ResponseEntity.ok(sprint);
    }

    @GetMapping
    public ResponseEntity<List<Sprint>> getAllSprintsByProject(@RequestHeader("Authorization") String token, @RequestParam Integer projectId) {
        List<Sprint> sprints = sprintService.getAllSprintsByProject(token, projectId);
        return ResponseEntity.ok(sprints);
    }

    @PostMapping
    public ResponseEntity<String> createSprint(@RequestHeader("Authorization") String token, @Validated(Create.class) @RequestBody Sprint sprint, @RequestParam Integer projectId) {
        sprintService.createSprint(token, sprint, projectId);
        CustomLogger.info(responseMessage.create());
        return ResponseEntity.ok(responseMessage.create());
    }

    @PatchMapping("/{id}")
    public ResponseEntity<String> updateSprint(@RequestHeader("Authorization") String token, @PathVariable Integer id, @Validated(Update.class) @RequestBody Sprint updatedSprint) {
        sprintService.updateSprint(token, id, updatedSprint);
        CustomLogger.info(responseMessage.update());
        return ResponseEntity.ok(responseMessage.update());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteSprint(@RequestHeader("Authorization") String token, @PathVariable Integer id) {
        sprintService.deleteSprint(token, id);
        CustomLogger.info(responseMessage.delete());
        return ResponseEntity.ok(responseMessage.delete());
    }

    @DeleteMapping
    public ResponseEntity<String> deleteAllSprintsByProject(@RequestHeader("Authorization") String token, @RequestParam Integer projectId) {
        sprintService.deleteAllSprintsByProject(token, projectId);
        CustomLogger.info(responseMessage.deleteAllFromCurrentProject());
        return ResponseEntity.ok(responseMessage.deleteAllFromCurrentProject());
    }
}