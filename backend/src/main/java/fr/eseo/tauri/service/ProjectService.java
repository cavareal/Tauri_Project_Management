package fr.eseo.tauri.service;

import fr.eseo.tauri.exception.GlobalExceptionHandler;
import fr.eseo.tauri.model.Project;
import fr.eseo.tauri.exception.ResourceNotFoundException;
import fr.eseo.tauri.repository.ProjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProjectService {

    private final AuthService authService;
    private final ProjectRepository projectRepository;

    public Project getProjectById(String token, Integer id) {
        if (!Boolean.TRUE.equals(authService.checkAuth(token, "readProject"))) {
            throw new SecurityException(GlobalExceptionHandler.UNAUTHORIZED_ACTION);
        }
        return projectRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("project", id));
    }

    public List<Project> getAllProjects(String token) {
        if (!Boolean.TRUE.equals(authService.checkAuth(token, "readProjects"))) {
            throw new SecurityException(GlobalExceptionHandler.UNAUTHORIZED_ACTION);
        }
        return projectRepository.findAll();
    }

    public void createProject(String token, Project project) {
        if (!Boolean.TRUE.equals(authService.checkAuth(token, "addProject"))) {
            throw new SecurityException(GlobalExceptionHandler.UNAUTHORIZED_ACTION);
        }
        projectRepository.save(project);
    }

    public void updateProject(String token, Integer id, Project updatedProject) {
        if (!Boolean.TRUE.equals(authService.checkAuth(token, "updateProject"))) {
            throw new SecurityException(GlobalExceptionHandler.UNAUTHORIZED_ACTION);
        }

        Project project = getProjectById(token, id);

        if (updatedProject.nbTeams() != null) project.nbTeams(updatedProject.nbTeams());
        if (updatedProject.nbWomen() != null) project.nbWomen(updatedProject.nbWomen());
        if (updatedProject.phase() != null) project.phase(updatedProject.phase());

        projectRepository.save(project);
    }

    public void deleteProjectById(String token, Integer id) {
        if (!Boolean.TRUE.equals(authService.checkAuth(token, "deleteProject"))) {
            throw new SecurityException(GlobalExceptionHandler.UNAUTHORIZED_ACTION);
        }
        getProjectById(token, id);
        projectRepository.deleteById(id);
    }

    public void deleteAllProjects(String token) {
        if (!Boolean.TRUE.equals(authService.checkAuth(token, "deleteProject"))) {
            throw new SecurityException(GlobalExceptionHandler.UNAUTHORIZED_ACTION);
        }
        projectRepository.deleteAll();
    }
}