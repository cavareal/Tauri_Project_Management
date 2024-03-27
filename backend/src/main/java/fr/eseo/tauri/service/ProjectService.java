package fr.eseo.tauri.service;

import fr.eseo.tauri.model.Project;
import fr.eseo.tauri.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.dao.DataAccessException;

@Service
public class ProjectService {

    private final ProjectRepository projectRepository;

    @Autowired
    public ProjectService(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    // Create new project
    public Project newProject(Integer teamsNumber, Integer genderRatio, Integer nbSprints, String phase) {
        Project project = new Project();
        project.nbTeams(teamsNumber);
        project.ratioGender(genderRatio);
        project.nbSprint(nbSprints);
        project.phase(phase);
        try {
            return projectRepository.save(project);
        } catch (DataAccessException e) {
            e.printStackTrace();
            return null;
        }
    }

    // Update nb of sprints in the project, with id of project and nb
    public Project updateProjectSprintsNumber(Integer projectId, Integer newSprintsNumber) {
        Project project = projectRepository.findById(projectId).orElse(null);
        if (project != null) {
            project.nbSprint(newSprintsNumber);
            return projectRepository.save(project);
        }
        return null;
    }

    // Update nb of teams in the project, with id of project and nb
    public Project updateProjectTeamsNumber(Integer projectId, Integer newTeamsNumber) {
        Project project = projectRepository.findById(projectId).orElse(null);
        if (project != null) {
            project.nbTeams(newTeamsNumber);
            return projectRepository.save(project);
        }
        return null;
    }

    // Update ratio gender in the project, with id of project and nb
    public Project updateProjectRatioGender(Integer projectId, Integer newRatioGender) {
        Project project = projectRepository.findById(projectId).orElse(null);
        if (project != null) {
            project.ratioGender(newRatioGender);
            return projectRepository.save(project);
        }
        return null;
    }
}
