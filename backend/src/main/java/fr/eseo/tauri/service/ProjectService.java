package fr.eseo.tauri.service;

import fr.eseo.tauri.controller.GlobalExceptionHandler;
import fr.eseo.tauri.model.Project;
import fr.eseo.tauri.model.enumeration.ProjectPhase;
import fr.eseo.tauri.model.exception.ResourceNotFoundException;
import fr.eseo.tauri.repository.ProjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

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

    public void updateProject(String token, Integer id, Map<String, Object> projectDetails) {
        if (Boolean.TRUE.equals(authService.checkAuth(token, "updateProject"))) {
            Project project = getProjectById(token, id);

            for (Map.Entry<String, Object> entry : projectDetails.entrySet()) {
                String key = entry.getKey();
                Object value = entry.getValue();

                if (value == null) {
                    continue;
                }

                switch (key) {
                    case "nbTeams":
                        project.nbTeams((Integer) value);
                        break;
                    case "nbWomen":
                        project.nbWomen((Integer) value);
                        break;
                    case "nbSprint":
                        project.nbSprint((Integer) value);
                        break;
                    case "phase":
                        project.phase(ProjectPhase.valueOf((String) value));
                        break;
                    default:
                        throw new IllegalArgumentException("Invalid key: " + key);
                }
            }
            projectRepository.save(project);

        } else {
            throw new SecurityException(GlobalExceptionHandler.UNAUTHORIZED_ACTION);
        }
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
