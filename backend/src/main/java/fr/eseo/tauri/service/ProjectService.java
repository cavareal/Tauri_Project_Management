package fr.eseo.tauri.service;

import fr.eseo.tauri.model.Project;
import fr.eseo.tauri.model.enumeration.ProjectPhase;
import fr.eseo.tauri.repository.ProjectRepository;
import fr.eseo.tauri.util.CustomLogger;
import jakarta.annotation.PostConstruct;
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

    @PostConstruct
    public void initDataIfTableIsEmpty() {
        if (projectRepository.count() == 0) {
            // Add a default project if the table is empty
            Project project = new Project();
            project.nbTeams(6);
            project.ratioGender(10);
            project.nbSprint(3);
            project.phase(ProjectPhase.COMPOSING); // Default phase
            projectRepository.save(project);
        }
    }

    /**
     * Create a new project.
     * @param teamsNumber the number of teams
     * @param genderRatio the gender ratio
     * @param nbSprints the number of sprints
     * @param phase the project phase
     * @return the newly created project, or null if an error occurred
     */
    public Project newProject(Integer teamsNumber, Integer genderRatio, Integer nbSprints, ProjectPhase phase) {
        Project project = new Project();
        project.nbTeams(teamsNumber);
        project.ratioGender(genderRatio);
        project.nbTeams(nbSprints);
        project.phase(phase);
        try {
            return projectRepository.save(project);
        } catch (DataAccessException e) {
            CustomLogger.logError("Error while creating a new project", e);
            return null;
        }
    }

    /**
     * Update the number of sprints in a project.
     * @param projectId the ID of the project
     * @param newSprintsNumber the new number of sprints
     * @return the updated project, or null if the project was not found
     */
    public Project updateProjectSprintsNumber(Integer projectId, Integer newSprintsNumber) {
        Project project = projectRepository.findById(projectId).orElse(null);
        if (project != null) {
            project.nbSprint(newSprintsNumber);
            return projectRepository.save(project);
        }
        return null;
    }
    /**
     * Get the nb of sprints of the project.
     *
     * @return the nb of sprints of the project, or "Aucun projet trouvé" if no project is found
     */
    public String getNumberSprints() {
        Project currentProject = projectRepository.findAll().get(0);
        if (currentProject != null) {
            return String.valueOf(currentProject.nbSprint());
        } else {
            return "Aucun projet trouvé";
        }
    }


    /**
     * Update the number of teams in a project.
     * @param projectId the ID of the project
     * @param newTeamsNumber the new number of teams
     * @return the updated project, or null if the project was not found
     */
    public Project updateProjectTeamsNumber(Integer projectId, Integer newTeamsNumber) {
        Project project = projectRepository.findById(projectId).orElse(null);
        if (project != null) {
            project.nbTeams(newTeamsNumber);
            return projectRepository.save(project);
        }
        return null;
    }

    /**
     * Update the gender ratio in a project.
     * @param projectId the ID of the project
     * @param newRatioGender the new gender ratio
     * @return the updated project, or null if the project was not found
     */
    public Project updateProjectRatioGender(Integer projectId, Integer newRatioGender) {
        Project project = projectRepository.findById(projectId).orElse(null);
        if (project != null) {
            project.ratioGender(newRatioGender);
            return projectRepository.save(project);
        }
        return null;
    }

    /**
     * Update the gender ratio in a project.
     * @param projectId the ID of the project
     * @param newPhase the new phase of the project
     * @return the updated project, or null if the project was not found
     */
    public Project updateProjectPhase(Integer projectId, ProjectPhase newPhase) {
        Project project = projectRepository.findById(projectId).orElse(null);
        if (project != null) {
            project.phase(newPhase);
            return projectRepository.save(project);
        }
        return null;
    }

    /**
     * Delete a project.
     * @param id the ID of the project to delete
     * @return the deleted project, or null if the project was not found
     */
    public Project deleteProject(Integer id) {
        try {
            Project project = projectRepository.findById(id).orElseThrow(() -> new Exception("Project not found"));
            projectRepository.delete(project);
            CustomLogger.logInfo("Deleted project with ID " + id);
            return project;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Get the current phase of the project.
     *
     * @return the current phase of the project, or "Aucun projet trouvé" if no project is found
     */
    public String getCurrentPhase() {
        Project currentProject = projectRepository.findAll().get(0);
        if (currentProject != null) {
            return currentProject.phase().name();
        } else {
            return "Aucun projet trouvé";
        }
    }

    public Project updateCurrentPhase(ProjectPhase newPhase) {
        Project currentProject = projectRepository.findAll().get(0);
        if (currentProject != null) {
            currentProject.phase(newPhase);
            projectRepository.save(currentProject);
            CustomLogger.logInfo("Updated phase of current project to " + newPhase.name());
            return currentProject;
        } else {
            return null;
        }
    }

    public List<Project> getAllProjects() {
        return projectRepository.findAll();
    }


    public Integer getRatioGender() {
        Project currentProject = getCurrentProject();
        if (currentProject != null ) {
            return currentProject.ratioGender();
        } else {
            return 0;
        }
    }

    public Project getCurrentProject() {
        if (projectRepository.findAll().isEmpty()) {
            return null;
        }
        return projectRepository.findAll().get(0);
    }
}
