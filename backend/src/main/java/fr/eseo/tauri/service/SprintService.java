package fr.eseo.tauri.service;

import fr.eseo.tauri.exception.GlobalExceptionHandler;
import fr.eseo.tauri.model.PresentationOrder;
import fr.eseo.tauri.model.Sprint;
import fr.eseo.tauri.exception.ResourceNotFoundException;
import fr.eseo.tauri.model.Student;
import fr.eseo.tauri.repository.SprintRepository;
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

    public void createSprint(String token, Sprint sprint, Integer projectId) {
        System.out.println("create sprint");

        if (!Boolean.TRUE.equals(authService.checkAuth(token, "addSprint"))) {
            throw new SecurityException(GlobalExceptionHandler.UNAUTHORIZED_ACTION);
        }

        if(sprint.projectId() != null) sprint.project(projectService.getProjectById(token, sprint.projectId()));
        sprintRepository.save(sprint);
        List<Student> students = studentService.getAllStudentsByProject(token, sprint.projectId());
        if(!students.isEmpty()) {
            for (Student student : students) {
                PresentationOrder presentationOrder = new PresentationOrder();
                presentationOrder.sprint(sprint);
                presentationOrder.student(student);
                presentationOrderService.createPresentationOrder(token, presentationOrder);
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
        getSprintById(token, id);
        sprintRepository.deleteById(id);
    }

    public void deleteAllSprintsByProject(String token, Integer projectId) {
        if (!Boolean.TRUE.equals(authService.checkAuth(token, "deleteSprint"))) {
            throw new SecurityException(GlobalExceptionHandler.UNAUTHORIZED_ACTION);
        }
        sprintRepository.deleteAllByProject(projectId);
    }
}