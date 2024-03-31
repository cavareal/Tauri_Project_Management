package fr.eseo.tauri.controller;

import fr.eseo.tauri.model.Project;
import fr.eseo.tauri.repository.ProjectRepository;
import fr.eseo.tauri.service.AuthService;
import fr.eseo.tauri.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Controller class for managing projects.
 */
@RestController
@RequestMapping("/api/projects")
public class ProjectController {

    private final ProjectRepository projectRepository;
    private final ProjectService projectService;
    private final AuthService authService;

    /**
     * Constructor for ProjectController.
     * @param projectRepository the project repository
     * @param projectService the project service
     * @param authService the authentication service
     */
    @Autowired
    public ProjectController(ProjectRepository projectRepository, ProjectService projectService, AuthService authService) {
        this.projectRepository = projectRepository;
        this.projectService = projectService;
        this.authService = authService;
    }

    @GetMapping("/current")
    public ResponseEntity<Project> getCurrentProject() {
        var project = projectRepository.findAll().get(0);
        if (project == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        // return ResponseEntity.ok(project);
        return ResponseEntity.status(HttpStatus.OK).body(project);
    }

    /**
     * Create a new project.
     * @param token the authentication token
     * @param teamsNumber the number of teams
     * @param genderRatio the gender ratio
     * @param sprintsNumber the number of sprints
     * @param phase the project phase
     * @return a response entity with a success message or an error message
     */
    @PostMapping("/new-project")
    public ResponseEntity<String> newProject(@RequestHeader("Authorization") String token, @RequestParam Integer teamsNumber, @RequestParam Integer genderRatio, @RequestParam Integer sprintsNumber, @RequestParam String phase) {
        // Check for token, if user is GOOD, with authService ??
        String permission = "teamCreation";
        if(authService.checkAuth(token, permission)) {
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

    /**
     * Update the number of sprints for a project.
     * @param token the authentication token
     * @param idProject the project ID
     * @param newSprintsNumber the new number of teams
     * @return a response entity with a success message or an error message
     */
    @PutMapping("/update-sprints-number/{idProject}")
    public ResponseEntity<String> updateProjectSprintsNumber(@RequestHeader("Authorization") String token, @PathVariable Integer idProject, @RequestParam Integer newSprintsNumber) {
        // Check token, if user is GOOD
        String permission = "ManageTeamsNumber";
        if(authService.checkAuth(token, permission)) {
            try {
                Project project = projectService.updateProjectSprintsNumber(idProject, newSprintsNumber);
                if (project != null) {
                    return ResponseEntity.ok("L'edit à bien été enregistré"); // Retourne true avec code 200
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

    /**
     * Update the number of teams for a project.
     * @param token the authentication token
     * @param idProject the project ID
     * @param newTeamsNumber the new number of teams
     * @return a response entity with a success message or an error message
     */
    @PutMapping("/update-teams-number/{idProject}")
    public ResponseEntity<String> updateProjectTeamsNumber(@RequestHeader("Authorization") String token, @PathVariable Integer idProject, @RequestParam Integer newTeamsNumber) {
        // Check token, if user is GOOD
        String permission = "manageSprint";
        if(authService.checkAuth(token, permission)) {
            try {
                Project project = projectService.updateProjectTeamsNumber(idProject, newTeamsNumber);
                if (project != null) {
                    return ResponseEntity.ok("L'edit à bien été enregistré"); // Retourne true avec code 200
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

    /**
     * Update the gender ratio for a project.
     * @param token the authentication token
     * @param idProject the project ID
     * @param newRatioGender the new gender ratio
     * @return a response entity with a success message or an error message
     */
    @PutMapping("/update-ratio-gender/{idProject}")
    public ResponseEntity<String> updateProjectRatioGender(@RequestHeader("Authorization") String token, @PathVariable Integer idProject, @RequestParam Integer newRatioGender) {
        // Check token, if user is GOOD
        String permission = "manageRatioGender";
        if(authService.checkAuth(token, permission)) {
            try {
                Project project = projectService.updateProjectRatioGender(idProject, newRatioGender);
                if (project != null) {
                    return ResponseEntity.ok("L'edit à bien été enregistré"); // Retourne true avec code 200
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

    /**
     * Update the gender ratio for a project.
     * @param token the authentication token
     * @param idProject the project ID
     * @param newPhase the new phase of the project
     * @return a response entity with a success message or an error message
     */
    @PutMapping("/update-project-phase/{idProject}")
    public ResponseEntity<String> updateProjectPhase(@RequestHeader("Authorization") String token, @PathVariable Integer idProject, @RequestParam String newPhase) {
        // Check token, if user is GOOD
        String permission = "manageProjectPhase";
        if(authService.checkAuth(token, permission)) {
            try {
                Project project = projectService.updateProjectPhase(idProject, newPhase);
                if (project != null) {
                    return ResponseEntity.ok("L'edit à bien été enregistré"); // Retourne true avec code 200
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

    /**
     * Delete a project.
     * @param token the authentication token
     * @param id the project ID
     * @return a response entity with a success message or an error message
     */
    @DeleteMapping("/delete-project")
    public ResponseEntity<String> deleteProject(@RequestHeader("Authorization") String token, @RequestParam Integer id) {
        // Check token, if user is GOOD
        String permission = "teamDelete";
        if(authService.checkAuth(token, permission)) {
            try {
                Project project = projectService.deleteProject(id);
                if (project != null) {
                    return ResponseEntity.ok("La suppression a bien été prise en compte"); // Retourne true avec code 200
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


    /**
     * Get the current phase of the project.
     *
     * @param token the authentication token
     * @return a response entity with the current phase of the project if successful, or an error message if an exception occurs or if the user is not authorized
     */
    @GetMapping("/current-phase")
    public ResponseEntity<String> getCurrentPhase(@RequestHeader("Authorization") String token) {
        String permission = "readProjectPhase";
        if (authService.checkAuth(token, permission)) {
            try {
                String currentPhase = projectService.getCurrentPhase();
                return ResponseEntity.status(HttpStatus.OK).body(currentPhase);
            }catch (Exception e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erreur lors de la récupération de la phase actuelle du projet : " + e.getMessage());
            }
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Non autorisé");
        }
    }

}
