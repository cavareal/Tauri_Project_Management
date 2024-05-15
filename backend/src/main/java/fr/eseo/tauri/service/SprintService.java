package fr.eseo.tauri.service;

import fr.eseo.tauri.exception.GlobalExceptionHandler;
import fr.eseo.tauri.model.Bonus;
import fr.eseo.tauri.model.PresentationOrder;
import fr.eseo.tauri.model.Sprint;
import fr.eseo.tauri.exception.ResourceNotFoundException;
import fr.eseo.tauri.model.Student;
import fr.eseo.tauri.repository.SprintRepository;
import fr.eseo.tauri.util.CustomLogger;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SprintService {

    private final AuthService authService;
    private final SprintRepository sprintRepository;
    private final ProjectService projectService;
    private final StudentService studentService;
    private final PresentationOrderService presentationOrderService;
    private final BonusService bonusService;

    public Sprint getSprintById(String token, Integer id) {
        if (!Boolean.TRUE.equals(authService.checkAuth(token, "readSprint"))) {
            throw new SecurityException(GlobalExceptionHandler.UNAUTHORIZED_ACTION);
        }
        return sprintRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("sprint", id));
    }

    public List<Sprint> getAllSprintsByProject(String token, Integer projectId) {
        if (!Boolean.TRUE.equals(authService.checkAuth(token, "readSprints"))) {
            throw new SecurityException(GlobalExceptionHandler.UNAUTHORIZED_ACTION);
        }
        return sprintRepository.findAllByProject(projectId);
    }

    public void createSprint(String token, Sprint sprint, int sprintId) {
        CustomLogger.info("Creating sprint " + sprintId);
        if (!Boolean.TRUE.equals(authService.checkAuth(token, "addSprint"))) {
            throw new SecurityException(GlobalExceptionHandler.UNAUTHORIZED_ACTION);
        }

        sprint.project(projectService.getProjectById(token, sprint.projectId()));
        sprintRepository.save(sprint);
        List<Student> students = studentService.getAllStudentsByProject(token, sprint.projectId());
        if(!students.isEmpty()) {
            for (Student student : students) {
                PresentationOrder presentationOrder = new PresentationOrder(sprint, student);
                presentationOrderService.createPresentationOrder(token, presentationOrder);
                Bonus limitedBonus = new Bonus((float) 0, true, sprint, student);
                Bonus unlimitedBonus = new Bonus((float) 0, false, sprint, student);
                bonusService.createBonus(token, limitedBonus);
                bonusService.createBonus(token, unlimitedBonus);
            }
        }
    }

    public void updateSprint(String token, Integer id, Sprint updatedSprint) {
        if (!Boolean.TRUE.equals(authService.checkAuth(token, "updateSprint"))) {
            throw new SecurityException(GlobalExceptionHandler.UNAUTHORIZED_ACTION);
        }

        Sprint sprint = getSprintById(token, id);

        if (updatedSprint.startDate() != null) sprint.startDate(updatedSprint.startDate());
        if (updatedSprint.endDate() != null) sprint.endDate(updatedSprint.endDate());
        if (updatedSprint.endType() != null) sprint.endType(updatedSprint.endType());
        if (updatedSprint.sprintOrder() != null) sprint.sprintOrder(updatedSprint.sprintOrder());
        if (updatedSprint.projectId() != null) sprint.project(projectService.getProjectById(token, updatedSprint.projectId()));

        sprintRepository.save(sprint);
    }

    public void deleteSprint(String token, Integer id) {
        if (!Boolean.TRUE.equals(authService.checkAuth(token, "deleteSprint"))) {
            throw new SecurityException(GlobalExceptionHandler.UNAUTHORIZED_ACTION);
        }

        var deletedSprint = getSprintById(token, id);
        sprintRepository.deleteById(id);

        var sprints = sprintRepository.findAllByProject(id);
        for (var sprint : sprints) {
            if (sprint.sprintOrder() > deletedSprint.sprintOrder()) {
                sprint.sprintOrder(sprint.sprintOrder() - 1);
                sprintRepository.save(sprint);
            }
        }
    }

    public void deleteAllSprintsByProject(String token, Integer projectId) {
        if (!Boolean.TRUE.equals(authService.checkAuth(token, "deleteSprint"))) {
            throw new SecurityException(GlobalExceptionHandler.UNAUTHORIZED_ACTION);
        }
        sprintRepository.deleteAllByProject(projectId);
    }
}