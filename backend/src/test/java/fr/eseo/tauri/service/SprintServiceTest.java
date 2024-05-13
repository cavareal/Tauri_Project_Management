package fr.eseo.tauri.service;

import fr.eseo.tauri.exception.ResourceNotFoundException;
import fr.eseo.tauri.model.Project;
import fr.eseo.tauri.model.Sprint;
import fr.eseo.tauri.model.enumeration.SprintEndType;
import fr.eseo.tauri.repository.SprintRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@Nested
class SprintServiceTest {

    @Mock
    private AuthService authService;

    @Mock
    private SprintRepository sprintRepository;

    @Mock
    private ProjectService projectService;

    @Mock
    private StudentService studentService;

    @InjectMocks
    private SprintService sprintService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getSprintByIdShouldReturnSprintWhenAuthorizedAndSprintExists() {
        String token = "validToken";
        Integer id = 1;
        Sprint sprint = new Sprint();

        when(authService.checkAuth(token, "readSprint")).thenReturn(true);
        when(sprintRepository.findById(id)).thenReturn(Optional.of(sprint));

        Sprint result = sprintService.getSprintById(token, id);

        assertEquals(sprint, result);
    }

    @Test
    void getSprintByIdShouldThrowSecurityExceptionWhenUnauthorized() {
        String token = "validToken";
        Integer id = 1;

        when(authService.checkAuth(token, "readSprint")).thenReturn(false);

        assertThrows(SecurityException.class, () -> sprintService.getSprintById(token, id));
    }

    @Test
    void getSprintByIdShouldThrowResourceNotFoundExceptionWhenSprintDoesNotExist() {
        String token = "validToken";
        Integer id = 1;

        when(authService.checkAuth(token, "readSprint")).thenReturn(true);
        when(sprintRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> sprintService.getSprintById(token, id));
    }

    @Test
    void getAllSprintsByProjectShouldReturnSprintsWhenAuthorizedAndProjectExists() {
        String token = "validToken";
        Integer projectId = 1;
        List<Sprint> sprints = Arrays.asList(new Sprint(), new Sprint());

        when(authService.checkAuth(token, "readSprints")).thenReturn(true);
        when(sprintRepository.findAllByProject(projectId)).thenReturn(sprints);

        List<Sprint> result = sprintService.getAllSprintsByProject(token, projectId);

        assertEquals(sprints, result);
    }

    @Test
    void getAllSprintsByProjectShouldThrowSecurityExceptionWhenUnauthorized() {
        String token = "validToken";
        Integer projectId = 1;

        when(authService.checkAuth(token, "readSprints")).thenReturn(false);

        assertThrows(SecurityException.class, () -> sprintService.getAllSprintsByProject(token, projectId));
    }

    @Test
    void getAllSprintsByProjectShouldHandleNoSprints() {
        String token = "validToken";
        Integer projectId = 1;
        List<Sprint> sprints = Collections.emptyList();

        when(authService.checkAuth(token, "readSprints")).thenReturn(true);
        when(sprintRepository.findAllByProject(projectId)).thenReturn(sprints);

        List<Sprint> result = sprintService.getAllSprintsByProject(token, projectId);

        assertEquals(sprints, result);
    }

    @Test
    void createSprintShouldSaveSprintWhenAuthorizedAndProjectExists() {
        String token = "validToken";
        Integer projectId = 1;
        Sprint sprint = new Sprint();

        when(authService.checkAuth(token, "addSprint")).thenReturn(true);
        when(projectService.getProjectById(token, projectId)).thenReturn(new Project());
        when(studentService.getAllStudentsByProject(token, projectId)).thenReturn(Collections.emptyList());

        sprintService.createSprint(token, sprint, projectId);

        verify(sprintRepository, times(1)).save(sprint);
    }

    @Test
    void createSprintShouldThrowSecurityExceptionWhenUnauthorized() {
        String token = "validToken";
        Integer projectId = 1;
        Sprint sprint = new Sprint();

        when(authService.checkAuth(token, "addSprint")).thenReturn(false);

        assertThrows(SecurityException.class, () -> sprintService.createSprint(token, sprint, projectId));
    }

    @Test
    void deleteSprintShouldDeleteSprintWhenAuthorizedAndSprintExists() {
        String token = "validToken";
        Integer id = 1;
        Sprint sprint = new Sprint();

        when(authService.checkAuth(token, "deleteSprint")).thenReturn(true);
        when(authService.checkAuth(token, "readSprint")).thenReturn(true);
        when(sprintRepository.findById(id)).thenReturn(Optional.of(sprint));

        sprintService.deleteSprint(token, id);

        verify(sprintRepository, times(1)).deleteById(id);
    }

    @Test
    void deleteSprintShouldThrowSecurityExceptionWhenUnauthorized() {
        String token = "validToken";
        Integer id = 1;

        when(authService.checkAuth(token, "deleteSprint")).thenReturn(false);

        assertThrows(SecurityException.class, () -> sprintService.deleteSprint(token, id));
    }

    @Test
    void deleteSprintShouldThrowResourceNotFoundExceptionWhenSprintDoesNotExist() {
        String token = "validToken";
        Integer id = 1;

        when(authService.checkAuth(token, "deleteSprint")).thenReturn(true);
        when(authService.checkAuth(token, "readSprint")).thenReturn(true);
        when(sprintRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> sprintService.deleteSprint(token, id));
    }

    @Test
    void deleteAllSprintsByProjectShouldDeleteSprintsWhenAuthorizedAndProjectExists() {
        String token = "validToken";
        Integer projectId = 1;

        when(authService.checkAuth(token, "deleteSprint")).thenReturn(true);

        sprintService.deleteAllSprintsByProject(token, projectId);

        verify(sprintRepository, times(1)).deleteAllByProject(projectId);
    }

    @Test
    void deleteAllSprintsByProjectShouldThrowSecurityExceptionWhenUnauthorized() {
        String token = "validToken";
        Integer projectId = 1;

        when(authService.checkAuth(token, "deleteSprint")).thenReturn(false);

        assertThrows(SecurityException.class, () -> sprintService.deleteAllSprintsByProject(token, projectId));
    }

    @Test
    void deleteAllSprintsByProjectShouldHandleNoSprints() {
        String token = "validToken";
        Integer projectId = 1;

        when(authService.checkAuth(token, "deleteSprint")).thenReturn(true);
        doNothing().when(sprintRepository).deleteAllByProject(projectId);

        sprintService.deleteAllSprintsByProject(token, projectId);

        verify(sprintRepository, times(1)).deleteAllByProject(projectId);
    }

    @Test
    void updateSprintShouldThrowSecurityExceptionWhenUnauthorized() {
        String token = "validToken";
        Integer id = 1;
        Sprint updatedSprint = new Sprint();

        when(authService.checkAuth(token, "updateSprint")).thenReturn(false);

        assertThrows(SecurityException.class, () -> sprintService.updateSprint(token, id, updatedSprint));
    }

}
