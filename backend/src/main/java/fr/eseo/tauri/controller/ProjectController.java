package fr.eseo.tauri.controller;

import fr.eseo.tauri.model.Project;
import fr.eseo.tauri.service.ProjectService;
import fr.eseo.tauri.util.CustomLogger;
import fr.eseo.tauri.validator.project.UpdateProjectValidator;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/projects")
@Tag(name = "projects")
public class ProjectController {

    private final ProjectService projectService;

    @GetMapping
    public ResponseEntity<List<Project>> getAllProjects(@RequestHeader("Authorization") String token) {
        List<Project> projects = projectService.getAllProjects(token);
        return ResponseEntity.ok(projects);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Project> getProjectById(@RequestHeader("Authorization") String token, @PathVariable Integer id) {
        Project project = projectService.getProjectById(token, id);
        return ResponseEntity.ok(project);
    }

    @PostMapping
    public ResponseEntity<String> addProjects(@RequestHeader("Authorization") String token, @RequestBody List<Project> projects) {
        projectService.addProjects(token, projects);
        CustomLogger.logInfo("The project(s) have been added");
        return ResponseEntity.ok("The project(s) have been added");
    }

    @PatchMapping("/{id}")
    public ResponseEntity<String> updateProject(@RequestHeader("Authorization") String token, @PathVariable Integer id, @Valid @RequestBody UpdateProjectValidator body) {
        projectService.updateProject(token, id, body);
        CustomLogger.logInfo("The project has been updated");
        return ResponseEntity.ok("The project has been updated");
    }

    @DeleteMapping
    public ResponseEntity<String> deleteAllProjects(@RequestHeader("Authorization") String token) {
        projectService.deleteAllProjects(token);
        CustomLogger.logInfo("All the projects have been deleted");
        return ResponseEntity.ok("All the projects have been deleted");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProject(@RequestHeader("Authorization") String token, @PathVariable Integer id) {
        projectService.deleteProject(token, id);
        CustomLogger.logInfo("The project has been deleted");
        return ResponseEntity.ok("The project has been deleted");
    }

}
