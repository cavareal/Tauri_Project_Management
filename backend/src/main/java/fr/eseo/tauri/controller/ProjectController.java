package fr.eseo.tauri.controller;

import fr.eseo.tauri.model.Project;
import fr.eseo.tauri.service.ProjectService;
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
@RequestMapping("/api/projects")
@Tag(name = "projects")
public class ProjectController {

    private final ProjectService projectService;
    private final ResponseMessage responseMessage = new ResponseMessage("project");

    @GetMapping("/{id}")
    public ResponseEntity<Project> getProjectById(@RequestHeader("Authorization") String token, @PathVariable Integer id) {
        Project project = projectService.getProjectById(token, id);
        return ResponseEntity.ok(project);
    }

    @GetMapping
    public ResponseEntity<List<Project>> getAllProjects(@RequestHeader("Authorization") String token) {
        List<Project> projects = projectService.getAllProjects(token);
        return ResponseEntity.ok(projects);
    }

    @GetMapping("/actual")
    public ResponseEntity<Project> getActualProject() {
        Project projects = projectService.getActualProject();
        return ResponseEntity.ok(projects);
    }

    @PostMapping("/actual/{idNewProject}")
    public ResponseEntity<String> setActualProject(@PathVariable Integer idNewProject) {
        projectService.setActualProject(idNewProject);
        CustomLogger.info(responseMessage.update());
        return ResponseEntity.ok(responseMessage.update());
    }

    @PostMapping
    public ResponseEntity<String> createProject(@RequestHeader("Authorization") String token, @Validated(Create.class) @RequestBody Project project) {
        projectService.createProject(token, project);
        CustomLogger.info(responseMessage.create());
        return ResponseEntity.ok(responseMessage.create());
    }

    @PatchMapping("/{id}")
    public ResponseEntity<String> updateProject(@RequestHeader("Authorization") String token, @PathVariable Integer id, @Validated(Update.class) @RequestBody Project updatedProject) {
        projectService.updateProject(token, id, updatedProject);
        CustomLogger.info(responseMessage.update());
        return ResponseEntity.ok(responseMessage.update());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProjectById(@RequestHeader("Authorization") String token, @PathVariable Integer id) {
        projectService.deleteProjectById(token, id);
        CustomLogger.info(responseMessage.delete());
        return ResponseEntity.ok(responseMessage.delete());
    }

    @DeleteMapping
    public ResponseEntity<String> deleteAllProjects(@RequestHeader("Authorization") String token) {
        projectService.deleteAllProjects(token);
        CustomLogger.info(responseMessage.deleteAll());
        return ResponseEntity.ok(responseMessage.deleteAll());
    }
}