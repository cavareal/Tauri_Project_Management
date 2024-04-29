package fr.eseo.tauri.service;

import fr.eseo.tauri.model.Project;
import fr.eseo.tauri.model.enumeration.ProjectPhase;
import fr.eseo.tauri.model.exception.ResourceNotFoundException;
import fr.eseo.tauri.repository.ProjectRepository;
import fr.eseo.tauri.util.CustomLogger;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.dao.DataAccessException;

import java.util.List;

/**
 * Service class for managing projects.
 */
@Service
@RequiredArgsConstructor
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final AuthService authService;

    /**
     * Get all projects.
     * @return a list of all projects
     */
    public List<Project> getAllProjects() {
        return projectRepository.findAll();
    }

    /**
     * Create a new project.
     * @param token the token of the user
     */
    public void newProject(String token) {
        if(Boolean.TRUE.equals(authService.checkAuth(token, "projectCreation"))) { //A terme, on enlèvera tous les paramètres sauf le token
            int numberOfProjects = projectRepository.findAll().size();
            projectRepository.save(new Project());

            if (projectRepository.findById(numberOfProjects + 1).isPresent()) {
                CustomLogger.logInfo("New project created");
            } else {
                throw new DataAccessException("Error while creating the project") {};
            }
        } else {
            throw new SecurityException("Unauthorized action");
        }
    }

    /**
     * Get the nb of sprints of the project.
     * @param token the token of the user
     * @param id the project id
     * @return the nb of sprints of the project, or "Aucun projet trouvé" if no project is found
     */
    public String getSprintsNumber(String token, int id) {
        if (Boolean.TRUE.equals(authService.checkAuth(token, "readSprintNumber"))) {
            Project project = projectRepository.findById(id).orElse(null);
            if(project != null){
                return String.valueOf(project.nbSprint());
            } else {
                throw new ResourceNotFoundException("project", id);
            }
        } else {
            throw new SecurityException("Unauthorized action");
        }
    }

    /**
     * Update the number of sprints in a project.
     * @param id the project id
     * @param newSprintsNumber the new number of sprints
     * @return the updated project, or null if the project was not found
     */
    public Project updateSprintsNumber(String token, Integer id, Integer newSprintsNumber) {

        if(Boolean.TRUE.equals(authService.checkAuth(token,  "ManageSprintsNumber"))) {
            Project project = projectRepository.findById(id).orElse(null);
            if(project != null){
                project.nbSprint(newSprintsNumber);
                return projectRepository.save(project);
            } else {
                throw new ResourceNotFoundException("project", id);
            }
        } else {
            throw new SecurityException("Unauthorized action");
        }
    }

    /**
     * Get the nb of teams of the project.
     * @param token the token of the user
     * @param id the project id
     * @return the nb of sprints of the project, or "Aucun projet trouvé" if no project is found
     */
    public String getTeamsNumber(String token, int id) {
        if (Boolean.TRUE.equals(authService.checkAuth(token, "readSprintNumber"))) {
            Project project = projectRepository.findById(id).orElse(null);
            if(project != null){
                return String.valueOf(project.nbTeams());
            } else {
                throw new ResourceNotFoundException("project", id);
            }
        } else {
            throw new SecurityException("Unauthorized action");
        }
    }

    /**
     * Update the number of teams in a project.
     * @param id the project id
     * @param newTeamsNumber the new number of teams
     * @return the updated project, or null if the project was not found
     */
    public Project updateTeamsNumber(String token, Integer id, Integer newTeamsNumber) {

        if(Boolean.TRUE.equals(authService.checkAuth(token,  "ManageTeamsNumber"))) {
            Project project = projectRepository.findById(id).orElse(null);
            if(project != null){
                project.nbTeams(newTeamsNumber);
                return projectRepository.save(project);
            } else {
                throw new ResourceNotFoundException("project", id);
            }
        } else {
            throw new SecurityException("Unauthorized action");
        }
    }

    /**
     * Get the number of women of the project teams
     * @param token the token of the user
     * @param id the project id
     * @return the gender ratio
     */
    public String getNbWomen(String token, Integer id) {

        if (Boolean.TRUE.equals(authService.checkAuth(token, "readNbWomen"))) {
            Project project = projectRepository.findById(id).orElse(null);
            if(project != null){
                return String.valueOf(project.nbWomen());
            } else {
                throw new ResourceNotFoundException("project", id);
            }
        } else {
            throw new SecurityException("Unauthorized action");
        }
    }

    /**
     * Update the gender ratio in a project.
     * @param token the token of the user
     * @param id the project id
     * @param newNbWomen the new gender ratio
     * @return the updated project, or null if the project was not found
     */
    public Project updateNbWomen(String token, Integer id, Integer newNbWomen) {

        if(Boolean.TRUE.equals(authService.checkAuth(token,  "ManageNbWomen"))) {
            Project project = projectRepository.findById(id).orElse(null);
            if(project != null){
                project.nbWomen(newNbWomen);
                return projectRepository.save(project);
            } else {
                throw new ResourceNotFoundException("project", id);
            }
        } else {
            throw new SecurityException("Unauthorized action");
        }
    }

    /**
     * Get the current phase of the project.
     * @param token the token of the user
     * @param id the project id
     * @return the current phase of the project, or "Aucun projet trouvé" if no project is found
     */
    public String getProjectPhase(String token, Integer id) {

        if (Boolean.TRUE.equals(authService.checkAuth(token, "readProjectPhase"))) {
            Project project = projectRepository.findById(id).orElse(null);
            if(project != null){
                return project.phase().name();
            } else {
                throw new ResourceNotFoundException("project", id);
            }
        } else {
            throw new SecurityException("Unauthorized action");
        }
    }

    /**
     * Update the gender ratio in a project.
     * @param id the ID of the project
     * @param newPhase the new phase of the project
     * @return the updated project, or null if the project was not found
     */
    public Project updateProjectPhase(String token, Integer id, ProjectPhase newPhase) {
        if(Boolean.TRUE.equals(authService.checkAuth(token,  "ManagePhase"))) {
            Project project = projectRepository.findById(id).orElse(null);
            if(project != null){
                project.phase(newPhase);
                CustomLogger.logInfo("Project phase has successfully been updated");
                return projectRepository.save(project);
            } else {
                throw new ResourceNotFoundException("project", id);
            }
        } else {
            throw new SecurityException("Unauthorized action");
        }
    }

    /**
     * Delete a project.
     * @param token the token of the user
     * @param id the ID of the project to delete
     */
    public void deleteProject(String token, Integer id) {
        if(Boolean.TRUE.equals(authService.checkAuth(token,  "DeleteProject"))) {
            Project project = projectRepository.findById(id).orElse(null);
            if(project != null){
                projectRepository.delete(project);
                CustomLogger.logInfo("Deleted project with ID " + id);
            } else {
                throw new ResourceNotFoundException("project", id);
            }
        } else {
            throw new SecurityException("Unauthorized action");
        }
    }
}
