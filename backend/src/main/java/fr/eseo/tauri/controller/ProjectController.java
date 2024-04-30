package fr.eseo.tauri.controller;

import fr.eseo.tauri.model.Project;
import fr.eseo.tauri.model.enumeration.ProjectPhase;
import fr.eseo.tauri.service.ProjectService;
import fr.eseo.tauri.util.CustomLogger;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Controller class for managing projects.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/projects")
@Tag(name = "projects")
public class ProjectController {

    private final ProjectService projectService;

    /**
     * Get all projects.
     * @return a response entity with the list of all projects
     */
    @GetMapping("/")
    public ResponseEntity<List<Project>> getAllProjects() {
        List<Project> projects = projectService.getAllProjects();
        return ResponseEntity.ok(projects);
    }

    /**
     * Create a new project.
     * @param token the authentication token
     * @return a response entity with a success message or an error message
     */
    @PostMapping("/")
    public ResponseEntity<String> newProject(@RequestHeader("Authorization") String token) {
        projectService.newProject(token);
        CustomLogger.logInfo("A new project has successfully been created");
        return ResponseEntity.ok("The project has been created");
    }

    /**
     * Get the number of sprints of this project.
     *
     * @param token the authentication token
     * @param id the project id
     * @return a response entity with the nb of sprints of the project if successful, or an error message if an exception occurs or if the user is not authorized
     */
    @GetMapping("/sprints/{id}")
    public ResponseEntity<String> getSprintsNumber(@RequestHeader("Authorization") String token, @PathVariable int id) {
            String sprintsNumber = projectService.getSprintsNumber(token, id);
            return ResponseEntity.ok(sprintsNumber);
    }

    /**
     * Update the number of sprints for a project.
     * @param token the authentication token
     * @param id the project id
     * @param request the body of the request containing the new number of sprints
     * @return a response entity with a success message or an error message
     */
    @PutMapping("/sprints/{id}")
    public ResponseEntity<String> updateSprintsNumber(@RequestHeader("Authorization") String token, @PathVariable Integer id,  @RequestBody Map<String, String> request) {
        projectService.updateSprintsNumber(token, id, Integer.valueOf(request.get("nbSprints")));
        CustomLogger.logInfo("Sprints number has successfully been updated");
        return ResponseEntity.ok("The number of sprints has been updated");
    }

    /**
     * Get the number of teams of this project.
     *
     * @param token the authentication token
     * @param id the project id
     * @return a response entity with the nb of teams of the project if successful, or an error message if an exception occurs or if the user is not authorized
     */
    @GetMapping("/teams/{id}")
    public ResponseEntity<String> getTeamsNumber(@RequestHeader("Authorization") String token, @PathVariable int id) {
        String teamsNumber = projectService.getTeamsNumber(token, id);
        return ResponseEntity.ok(teamsNumber);
    }

    /**
     * Update the number of teams for a project.
     * @param token the authentication token
     * @param id the project id
     * @param newTeamsNumber the new number of teams
     * @return a response entity with a success message or an error message
     */
    @PutMapping("/teams/{id}")
    public ResponseEntity<String> updateTeamsNumber(@RequestHeader("Authorization") String token, @PathVariable Integer id, @RequestParam Integer newTeamsNumber) {
        projectService.updateTeamsNumber(token, id, newTeamsNumber);
        CustomLogger.logInfo("Teams number has successfully been updated");
        return ResponseEntity.ok("The number of teams has been updated");
    }

    /**
     * Get the gender ratio of these projects teams.
     *
     * @param token the authentication token
     * @param id the project id
     * @return a response entity with the gender ratio of the project if successful, or an error message if an exception occurs or if the user is not authorized
     */
    @GetMapping("/women/{id}")
    public ResponseEntity<String> getNbWomen(@RequestHeader("Authorization") String token, @PathVariable Integer id) {
        String nbWomen = projectService.getNbWomen(token, id);
        return ResponseEntity.ok(nbWomen);
    }

    /**
     * Update the gender ratio for a project.
     * @param token the authentication token
     * @param id the project id
     * @param newNbWomen the new gender ratio
     * @return a response entity with a success message or an error message
     */
    @PutMapping("/women/{id}")
    public ResponseEntity<String> updateNbWomen(@RequestHeader("Authorization") String token, @PathVariable Integer id, @RequestParam Integer newNbWomen) {
        projectService.updateNbWomen(token, id, newNbWomen);
        CustomLogger.logInfo("Gender ratio has successfully been updated");
        return ResponseEntity.ok("The gender ratio has been updated");
    }

    /**
     * Get the phase of a project.
     * @param token the authentication token
     * @param id the project id
     * @return a response entity with the phase of the project
     */
    @GetMapping("/phase/{id}")
    public ResponseEntity<String> getProjectPhase(@RequestHeader("Authorization") String token, @PathVariable Integer id) {
        String projectPhase = projectService.getProjectPhase(token, id);
        return ResponseEntity.ok(projectPhase);
    }

    /**
     * Update the phase for a project.
     * @param token the authentication token
     * @param id the project id
     * @param request the body of the request containing the new phase of the project
     * @return a response entity with a success message or an error message
     */
    @PutMapping("/phase/{id}")
    public ResponseEntity<String> updateProjectPhase(@RequestHeader("Authorization") String token, @PathVariable Integer id,  @RequestBody Map<String, String> request) {
        projectService.updateProjectPhase(token, id, ProjectPhase.valueOf(request.get("phase")));
        return ResponseEntity.ok("The project phase has been updated");
    }

    /**
     * Update the phase for a project.
     * @param token the authentication token
     * @param id the project id
     * @param request the body of the request containing the new phase of the project
     * @return a response entity with a success message or an error message
     */
    @PutMapping("/{id}")
    public ResponseEntity<String> updateProject(@RequestHeader("Authorization") String token, @PathVariable Integer id,  @RequestBody Map<String, String> request) {
        projectService.updateProjectPhase(token, id, ProjectPhase.valueOf(request.get("phase")));
        return ResponseEntity.ok("The project phase has been updated");
    }


    /**
     * Delete a project.
     * @param token the authentication token
     * @param id the project ID
     * @return a response entity with a success message or an error message
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProject(@RequestHeader("Authorization") String token, @PathVariable Integer id) {
        projectService.deleteProject(token, id);
        return ResponseEntity.ok("The project with id : " + id + " has been deleted");
    }

}
