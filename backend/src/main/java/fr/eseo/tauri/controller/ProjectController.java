package fr.eseo.tauri.controller;

import fr.eseo.tauri.model.Project;
import fr.eseo.tauri.repository.ProjectRepository;
import fr.eseo.tauri.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/projects")
public class ProjectController {

    private final ProjectRepository projectRepository;
    private final ProjectService projectService;

    @Autowired
    public ProjectController(ProjectRepository projectRepository, ProjectService projectService) {
        this.projectRepository = projectRepository;
        this.projectService = projectService;
    }


    @PutMapping("/update-ratio-gendre")
    public ResponseEntity<String> updateProjectRatioGendre(@RequestParam Integer id, @RequestParam Integer newRatioGendre) {
        // Check for token, if user is GOOD, with authService ??
        if(true) {
            try {
                Project project = projectService.updateProjectRatioGendre(id, newRatioGendre);
                if (project != null) {
                    return ResponseEntity.ok("Le nouveau ratio à bien été enregistré"); // Retourne true avec code 200
                } else {
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erreur lors de la mise à jour"); // Erreur 500
                }
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erreur lors de la mise à jour : " + e.getMessage()); // Erreur 500
            }
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Non autorisé"); // Code 401
        }
    }
}
