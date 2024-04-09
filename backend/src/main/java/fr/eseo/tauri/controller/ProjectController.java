package fr.eseo.tauri.controller;

import fr.eseo.tauri.model.Project;
import fr.eseo.tauri.model.enumeration.ProjectPhase;
import fr.eseo.tauri.repository.ProjectRepository;
import fr.eseo.tauri.service.AuthService;
import fr.eseo.tauri.service.ProjectService;
import fr.eseo.tauri.util.CustomLogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

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
        Optional<Project> projectOptional = projectRepository.findAll().stream().findFirst();
        if (projectOptional.isPresent()) {
            Project project = projectOptional.get();
            return ResponseEntity.status(HttpStatus.OK).body(project);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
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
    public ResponseEntity<String> newProject(@RequestHeader("Authorization") String token, @RequestParam Integer teamsNumber, @RequestParam Integer genderRatio, @RequestParam Integer sprintsNumber, @RequestParam ProjectPhase phase) {
        // Check for token, if user is GOOD, with authService ??
        String permission = "teamCreation";
        if(authService.checkAuth(token, permission)) {
            try {
                Project project = projectService.newProject(teamsNumber, genderRatio, sprintsNumber, phase);
                if (project != null) {
                    CustomLogger.logInfo("New project created");
                    return ResponseEntity.ok("L'ajout a bien été enregistré");
                } else {
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erreur lors de la mise à jour");
                }
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erreur lors de la mise à jour : " + e.getMessage());
            }
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Non autorisé");
        }
    }

    /**
     * Update the number of sprints for a project.
     * @param token the authentication token
     * @param idProject the project ID
     * @param request data from the front
     * @return a response entity with a success message or an error message
     */
    @PutMapping("/update-sprints-number/{idProject}")
    public ResponseEntity<String> updateProjectSprintsNumber(@RequestHeader("Authorization") String token, @PathVariable Integer idProject,  @RequestBody Map<String, String> request) {
        // Check token, if user is GOOD
        Integer newSprintsNumber = Integer.valueOf(request.get("nbSprints"));

        String permission = "ManageTeamsNumber";
        if(authService.checkAuth(token, permission)) {
            try {
                Project project = projectService.updateProjectSprintsNumber(idProject, newSprintsNumber);
                if (project != null) {
                    CustomLogger.logInfo("Sprints number updated");
                    return ResponseEntity.ok("L'edit à bien été enregistré");
                } else {
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erreur lors de la mise à jour");
                }
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erreur lors de la mise à jour : " + e.getMessage());
            }
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Non autorisé");
        }
    }
    /**
     * Get the number of sprints of this project.
     *
     * @param token the authentication token
     * @return a response entity with the nb of sprints of the project if successful, or an error message if an exception occurs or if the user is not authorized
     */
    @GetMapping("/sprints-number")
    public ResponseEntity<String> getNumberSprints(@RequestHeader("Authorization") String token) {
        String permission = "readSprintNumber";
        if (authService.checkAuth(token, permission)) {
            try {
                String currentPhase = projectService.getNumberSprints();
                return ResponseEntity.status(HttpStatus.OK).body(currentPhase);
            }catch (Exception e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erreur lors de la récupération de la phase actuelle du projet : " + e.getMessage());
            }
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Non autorisé");
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
                    CustomLogger.logInfo("Teams number updated");
                    return ResponseEntity.ok("L'edit à bien été enregistré");
                } else {
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erreur lors de la mise à jour");
                }
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erreur lors de la mise à jour : " + e.getMessage());
            }
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Non autorisé");
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
                    CustomLogger.logInfo("The gender ratio has been updated");
                    return ResponseEntity.ok("L'edit à bien été enregistré");
                } else {
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erreur lors de la mise à jour");
                }
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erreur lors de la mise à jour : " + e.getMessage());
            }
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Non autorisé");
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
    public ResponseEntity<String> updateProjectPhase(@RequestHeader("Authorization") String token, @PathVariable Integer idProject, @RequestParam ProjectPhase newPhase) {
        // Check token, if user is GOOD
        String permission = "manageProjectPhase";
        if(authService.checkAuth(token, permission)) {
            try {
                Project project = projectService.updateProjectPhase(idProject, newPhase);
                if (project != null) {
                    CustomLogger.logInfo("The project phase has been updated");
                    return ResponseEntity.ok("L'edit à bien été enregistré");
                } else {
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erreur lors de la mise à jour");
                }
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erreur lors de la mise à jour : " + e.getMessage());
            }
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Non autorisé");
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
                    return ResponseEntity.ok("La suppression a bien été prise en compte");
                } else {
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erreur lors de la mise à jour");
                }
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erreur lors de la mise à jour : " + e.getMessage());
            }
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Non autorisé");
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

    @PutMapping("/current-phase")
    public ResponseEntity<String> updateCurrentPhase(@RequestHeader("Authorization") String token, @RequestBody Map<String, String> request) {
        String permission = "manageProjectPhase";
        if (authService.checkAuth(token, permission)) {
            try {
                ProjectPhase newPhase = ProjectPhase.valueOf(request.get("phase"));
                Project project = projectService.updateCurrentPhase(newPhase);
                if (project != null) {
                    return ResponseEntity.ok("La modification a bien été prise en compte");
                } else {
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erreur lors de la mise à jour");
                }
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erreur lors de la mise à jour : " + e.getMessage());
            }
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Non autorisé");
        }
    }

}
