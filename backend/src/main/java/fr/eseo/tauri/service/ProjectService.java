package fr.eseo.tauri.service;

import fr.eseo.tauri.model.Project;
import fr.eseo.tauri.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProjectService {

    private final ProjectRepository projectRepository;

    @Autowired
    public ProjectService(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
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


    public Project updateProjectRatioGendre(Integer projectId, Integer newRatioGender) {
        Project project = projectRepository.findById(projectId).orElse(null);
        if (project != null) {
            project.ratioGender(newRatioGender);
            return projectRepository.save(project);
        }
        return null;
    }
}
