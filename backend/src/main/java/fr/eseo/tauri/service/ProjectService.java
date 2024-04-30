package fr.eseo.tauri.service;

import fr.eseo.tauri.controller.GlobalExceptionHandler;
import fr.eseo.tauri.model.Project;
import fr.eseo.tauri.model.exception.ResourceNotFoundException;
import fr.eseo.tauri.repository.ProjectRepository;
import fr.eseo.tauri.validator.project.UpdateProjectValidator;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProjectService {

    private final AuthService authService;
    private final ProjectRepository projectRepository;

    public List<Project> getAllProjects(String token) {
        if (Boolean.TRUE.equals(authService.checkAuth(token, "readProjects"))) {
            return projectRepository.findAll();
        } else {
            throw new SecurityException(GlobalExceptionHandler.UNAUTHORIZED_ACTION);
        }
    }

    public Project getProjectById(String token, Integer id) {
        if (Boolean.TRUE.equals(authService.checkAuth(token, "readProject"))) {
            return projectRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("project", id));
        } else {
            throw new SecurityException(GlobalExceptionHandler.UNAUTHORIZED_ACTION);
        }
    }

    public void addProjects(String token, List<Project> projects) {
        if (Boolean.TRUE.equals(authService.checkAuth(token, "addProject"))) {
            int projectsNumber = projectRepository.findAll().size();
            for(Project project : projects) {
                projectRepository.save(project);
                if(projectRepository.findAll().size() == projectsNumber){
                    throw new DataAccessException("Error : Could not add project") {};
                } else {
                    projectsNumber++;
                }
            }
        } else {
            throw new SecurityException(GlobalExceptionHandler.UNAUTHORIZED_ACTION);
        }
    }

    public void updateProject(String token, Integer id, @Valid UpdateProjectValidator properties) {
        if (!Boolean.TRUE.equals(authService.checkAuth(token, "updateProject"))) {
            throw new SecurityException(GlobalExceptionHandler.UNAUTHORIZED_ACTION);
        }

        Project project = getProjectById(token, id);

        if (properties.nbTeams() != null) project.nbTeams(properties.nbTeams());
        if (properties.womenPerTeam() != null) project.nbWomen(properties.womenPerTeam());
        if (properties.nbSprints() != null) project.nbSprint(properties.nbSprints());
        if (properties.phase() != null) project.phase(properties.phase());

        projectRepository.save(project);
    }

    public void deleteAllProjects(String token) {
        if (Boolean.TRUE.equals(authService.checkAuth(token, "deleteProject"))) {
            projectRepository.deleteAll();
            if(!projectRepository.findAll().isEmpty()){
                throw new DataAccessException("Error : Could not delete all projects") {};
            }
        } else {
            throw new SecurityException(GlobalExceptionHandler.UNAUTHORIZED_ACTION);
        }
    }

    public void deleteProject(String token, Integer id) {
        if (Boolean.TRUE.equals(authService.checkAuth(token, "deleteProject"))) {
            getProjectById(token, id);
            int projectsNumber = projectRepository.findAll().size();
            projectRepository.deleteById(id);
            if(projectRepository.findAll().size() == projectsNumber){
                throw new DataAccessException("Error : Could not delete project with id : " + id) {};
            }
        } else {
            throw new SecurityException(GlobalExceptionHandler.UNAUTHORIZED_ACTION);
        }
    }
}
