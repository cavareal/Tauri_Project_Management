package fr.eseo.tauri.service;

import fr.eseo.tauri.controller.GlobalExceptionHandler;
import fr.eseo.tauri.model.Sprint;
import fr.eseo.tauri.model.enumeration.SprintEndType;
import fr.eseo.tauri.model.exception.ResourceNotFoundException;
import fr.eseo.tauri.repository.ProjectRepository;
import fr.eseo.tauri.repository.SprintRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class SprintService {

    private final AuthService authService;
    private final SprintRepository sprintRepository;
    private final ProjectRepository projectRepository;

    public List<Sprint> getAllSprints(String token) {
        if (!Boolean.TRUE.equals(authService.checkAuth(token, "readSprints"))) {
            throw new SecurityException(GlobalExceptionHandler.UNAUTHORIZED_ACTION);
        }
        return sprintRepository.findAll();
    }

    public Sprint getSprintById(String token, Integer id) {
        if (!Boolean.TRUE.equals(authService.checkAuth(token, "readSprint"))) {
            throw new SecurityException(GlobalExceptionHandler.UNAUTHORIZED_ACTION);
        }
        return sprintRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("sprint", id));
    }

    public void addSprints(String token, List<Sprint> sprints) {
        if (!Boolean.TRUE.equals(authService.checkAuth(token, "addSprint"))) {
            throw new SecurityException(GlobalExceptionHandler.UNAUTHORIZED_ACTION);
        }
        int sprintsNumber = sprintRepository.findAll().size();
        for(Sprint sprint : sprints) {
            sprintRepository.save(sprint);
            if(sprintRepository.findAll().size() == sprintsNumber){
                throw new DataAccessException("Error : Could not add sprint") {};
            } else {
                sprintsNumber++;
            }
        }
    }

    public void updateSprint(String token, Integer id, Map<String, Object> sprintDetails) {
        if (Boolean.TRUE.equals(authService.checkAuth(token, "updateSprint"))) {
            Sprint sprint = getSprintById(token, id);

            for (Map.Entry<String, Object> entry : sprintDetails.entrySet()) {
                String key = entry.getKey();
                Object value = entry.getValue();

                if (value == null) {
                    continue;
                }

                switch (key) {
                    case "startDate":
                        sprint.startDate((LocalDate) value);
                        break;
                    case "endDate":
                        sprint.endDate((LocalDate) value);
                        break;
                    case "endType":
                        sprint.endType(SprintEndType.valueOf((String) value));
                        break;
                    case "project":
                        Map<String, Object> projectMap = (Map<String, Object>) value;
                        sprint.project(projectRepository.findById((Integer) projectMap.get("id")).orElseThrow(() -> new ResourceNotFoundException("project", (Integer) projectMap.get("id"))));
                        break;
                    default:
                        throw new IllegalArgumentException("Invalid key: " + key);
                }
            }

            sprintRepository.save(sprint);

        } else {
            throw new SecurityException(GlobalExceptionHandler.UNAUTHORIZED_ACTION);
        }
    }

    public void deleteAllSprints(String token) {
        if (!Boolean.TRUE.equals(authService.checkAuth(token, "deleteSprint"))) {
            throw new SecurityException(GlobalExceptionHandler.UNAUTHORIZED_ACTION);
        }
        sprintRepository.deleteAll();
        if(!sprintRepository.findAll().isEmpty()){
            throw new DataAccessException("Error : Could not delete all sprints") {};
        }
    }

    public void deleteSprint(String token, Integer id) {
        if (!Boolean.TRUE.equals(authService.checkAuth(token, "deleteSprint"))) {
            throw new SecurityException(GlobalExceptionHandler.UNAUTHORIZED_ACTION);
        }
        getSprintById(token, id);
        int sprintsNumber = sprintRepository.findAll().size();
        sprintRepository.deleteById(id);
        if(sprintRepository.findAll().size() == sprintsNumber){
            throw new DataAccessException("Error : Could not delete sprint with id : " + id) {};
        }
    }
}
