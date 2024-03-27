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

    // Update ratio gendre of the project, with id of project and new ratio
    public Project updateProjectRatioGendre(Integer projectId, Integer newRatioGendre) {
        Project project = projectRepository.findById(projectId).orElse(null);
        if (project != null) {
            project.ratioGendre(newRatioGendre);
            return projectRepository.save(project);
        }
        return null;
    }
}
