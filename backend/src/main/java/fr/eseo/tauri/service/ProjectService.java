package fr.eseo.tauri.service;

import fr.eseo.tauri.exception.GlobalExceptionHandler;
import fr.eseo.tauri.model.Project;
import fr.eseo.tauri.exception.ResourceNotFoundException;
import fr.eseo.tauri.repository.ProjectRepository;
import fr.eseo.tauri.validator.project.CreateProjectValidator;
import fr.eseo.tauri.validator.project.UpdateProjectValidator;
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
        if (!Boolean.TRUE.equals(authService.checkAuth(token, "readProjects"))) {
            throw new SecurityException(GlobalExceptionHandler.UNAUTHORIZED_ACTION);
        }
        return projectRepository.findAll();
    }

    public Project getProjectById(String token, Integer id) {
        if (!Boolean.TRUE.equals(authService.checkAuth(token, "readProject"))) {
            throw new SecurityException(GlobalExceptionHandler.UNAUTHORIZED_ACTION);
        }
        return projectRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("project", id));
    }

    public void createProject(String token, CreateProjectValidator projectDetails) {
        if (!Boolean.TRUE.equals(authService.checkAuth(token, "addProject"))) {
            throw new SecurityException(GlobalExceptionHandler.UNAUTHORIZED_ACTION);
        }
        int projectsNumber = projectRepository.findAll().size();
        Project project = new Project();
        if(projectDetails.nbTeams() != null) project.nbTeams(projectDetails.nbTeams());
        if(projectDetails.womenPerTeam() != null) project.nbWomen(projectDetails.womenPerTeam());
        if(projectDetails.nbSprints() != null) project.nbSprint(projectDetails.nbSprints());
        if(projectDetails.phase() != null) project.phase(projectDetails.phase());
        projectRepository.save(project);
        if(projectRepository.findAll().size() == projectsNumber){
            throw new DataAccessException("Error : Could not add project") {};
        }
    }

    public void updateProject(String token, Integer id,  UpdateProjectValidator properties) {
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
        if (!Boolean.TRUE.equals(authService.checkAuth(token, "deleteProject"))) {
            throw new SecurityException(GlobalExceptionHandler.UNAUTHORIZED_ACTION);
        }
        projectRepository.deleteAll();
        if(!projectRepository.findAll().isEmpty()){
            throw new DataAccessException("Error : Could not delete all projects") {};
        }
    }

    public void deleteProject(String token, Integer id) {
        if (!Boolean.TRUE.equals(authService.checkAuth(token, "deleteProject"))) {
            throw new SecurityException(GlobalExceptionHandler.UNAUTHORIZED_ACTION);
        }
        getProjectById(token, id);
        int projectsNumber = projectRepository.findAll().size();
        projectRepository.deleteById(id);
        if(projectRepository.findAll().size() == projectsNumber){
            throw new DataAccessException("Error : Could not delete project with id : " + id) {};
        }
    }

}
