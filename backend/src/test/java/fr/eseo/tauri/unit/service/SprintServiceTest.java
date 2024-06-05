package fr.eseo.tauri.unit.service;

import fr.eseo.tauri.exception.ResourceNotFoundException;
import fr.eseo.tauri.model.Project;
import fr.eseo.tauri.model.Sprint;
import fr.eseo.tauri.model.enumeration.SprintEndType;
import fr.eseo.tauri.repository.SprintRepository;
import fr.eseo.tauri.service.AuthService;
import fr.eseo.tauri.service.ProjectService;
import fr.eseo.tauri.service.SprintService;
import fr.eseo.tauri.service.StudentService;
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
        Integer id = 1;
        Sprint sprint = new Sprint();

        when(sprintRepository.findById(id)).thenReturn(Optional.of(sprint));

        Sprint result = sprintService.getSprintById(id);

        assertEquals(sprint, result);
    }

    @Test
    void getSprintByIdShouldThrowResourceNotFoundExceptionWhenSprintDoesNotExist() {
        Integer id = 1;

        when(sprintRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> sprintService.getSprintById(id));
    }

    @Test
    void getAllSprintsByProjectShouldReturnSprintsWhenAuthorizedAndProjectExists() {
        Integer projectId = 1;
        List<Sprint> sprints = Arrays.asList(new Sprint(), new Sprint());

        when(sprintRepository.findAllByProject(projectId)).thenReturn(sprints);

        List<Sprint> result = sprintService.getAllSprintsByProject(projectId);

        assertEquals(sprints, result);
    }

    @Test
    void getAllSprintsByProjectShouldHandleNoSprints() {
        Integer projectId = 1;
        List<Sprint> sprints = Collections.emptyList();

        when(sprintRepository.findAllByProject(projectId)).thenReturn(sprints);

        List<Sprint> result = sprintService.getAllSprintsByProject(projectId);

        assertEquals(sprints, result);
    }

    @Test
    void createSprintShouldSaveSprintWhenAuthorizedAndProjectExists() {
        Integer projectId = 1;
        Sprint sprint = new Sprint();

        when(projectService.getProjectById(projectId)).thenReturn(new Project());
        when(studentService.getAllStudentsByProject(projectId)).thenReturn(Collections.emptyList());

        sprintService.createSprint(sprint, projectId);

        verify(sprintRepository, times(1)).save(sprint);
    }

    @Test
    void deleteSprintShouldDeleteSprintWhenAuthorizedAndSprintExists() {
        Integer id = 1;
        Sprint sprint = new Sprint();

        when(sprintRepository.findById(id)).thenReturn(Optional.of(sprint));

        sprintService.deleteSprint(id);

        verify(sprintRepository, times(1)).deleteById(id);
    }

    @Test
    void deleteSprintShouldThrowResourceNotFoundExceptionWhenSprintDoesNotExist() {
        Integer id = 1;

        when(sprintRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> sprintService.deleteSprint(id));
    }

    @Test
    void deleteAllSprintsByProjectShouldDeleteSprintsWhenAuthorizedAndProjectExists() {
        Integer projectId = 1;

        sprintService.deleteAllSprintsByProject(projectId);

        verify(sprintRepository, times(1)).deleteAllByProject(projectId);
    }

    @Test
    void deleteAllSprintsByProjectShouldHandleNoSprints() {
        Integer projectId = 1;

        doNothing().when(sprintRepository).deleteAllByProject(projectId);

        sprintService.deleteAllSprintsByProject(projectId);

        verify(sprintRepository, times(1)).deleteAllByProject(projectId);
    }
}
