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



    @PostMapping("/new-project")
    public ResponseEntity<String> newProject(@RequestParam Integer teamsNumber, @RequestParam Integer genderRatio, @RequestParam Integer sprintsNumber, @RequestParam String phase) {
        // Check for token, if user is GOOD, with authService ??
        if(true) {
            try {
                Project project = projectService.newProject(teamsNumber, genderRatio, sprintsNumber, phase);
                if (project != null) {
                    return ResponseEntity.ok("L'ajout a bien été enregistré"); // Retourne true avec code 200
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


    @PutMapping("/update-teams-number")
    public ResponseEntity<String> updateProjectSprintsNumber(@RequestParam Integer id, @RequestParam Integer newSprintsNumber) {
        // Check for token, if user is GOOD, with authService ??
        if(true) {
            try {
                Project project = projectService.updateProjectSprintsNumber(id, newSprintsNumber);
                if (project != null) {
                    return ResponseEntity.ok("L'édit à bien été enregistré"); // Retourne true avec code 200
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

    @PutMapping("/update-sprints-number")
    public ResponseEntity<String> updateProjectTeamsNumber(@RequestParam Integer id, @RequestParam Integer newTeamsNumber) {
        // Check for token, if user is GOOD, with authService ??
        if(true) {
            try {
                Project project = projectService.updateProjectTeamsNumber(id, newTeamsNumber);
                if (project != null) {
                    return ResponseEntity.ok("L'édit à bien été enregistré"); // Retourne true avec code 200
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

    @PutMapping("/update-ratio-gender")
    public ResponseEntity<String> updateProjectRatioGender(@RequestParam Integer id, @RequestParam Integer newRatioGender) {
        // Check for token, if user is GOOD, with authService ??
        if(true) {
            try {
                Project project = projectService.updateProjectRatioGender(id, newRatioGender);
                if (project != null) {
                    return ResponseEntity.ok("L'édit à bien été enregistré"); // Retourne true avec code 200
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
