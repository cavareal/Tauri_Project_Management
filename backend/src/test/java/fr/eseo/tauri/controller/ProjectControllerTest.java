package fr.eseo.tauri.controller;

import fr.eseo.tauri.model.Project;
import fr.eseo.tauri.model.enumeration.ProjectPhase;
import fr.eseo.tauri.repository.ProjectRepository;
import fr.eseo.tauri.service.AuthService;
import fr.eseo.tauri.service.ProjectService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class ProjectControllerTest {

    @Mock
    private ProjectRepository projectRepository;

    @Mock
    private ProjectService projectService;

    @Mock
    private AuthService authService;

    @InjectMocks
    private ProjectController projectController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getCurrentProject_returnsProject_whenProjectExists() {
        Project project = new Project();
        when(projectRepository.findAll()).thenReturn(Collections.singletonList(project));

        ResponseEntity<Project> response = projectController.getCurrentProject();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(project, response.getBody());
    }

    @Test
    void getCurrentProject_returnsNotFound_whenNoProjectExists() {
        when(projectRepository.findAll()).thenReturn(Collections.emptyList());

        ResponseEntity<Project> response = projectController.getCurrentProject();

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void newProject_returnsOk_whenProjectCreatedSuccessfully() {
        String token = "token";
        when(authService.checkAuth(token, "teamCreation")).thenReturn(true);
        when(projectService.newProject(anyInt(), anyInt(), anyInt(), any(ProjectPhase.class))).thenReturn(new Project());

        ResponseEntity<String> response = projectController.newProject(token, 1, 1, 1, ProjectPhase.INITIATION);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void newProject_returnsUnauthorized_whenUserNotAuthorized() {
        String token = "token";
        when(authService.checkAuth(token, "teamCreation")).thenReturn(false);

        ResponseEntity<String> response = projectController.newProject(token, 1, 1, 1, ProjectPhase.INITIATION);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    @Test
    void newProject_returnsInternalServerError_whenProjectCreationFails() {
        String token = "token";
        when(authService.checkAuth(token, "teamCreation")).thenReturn(true);
        when(projectService.newProject(anyInt(), anyInt(), anyInt(), any(ProjectPhase.class))).thenThrow(new RuntimeException());

        ResponseEntity<String> response = projectController.newProject(token, 1, 1, 1, ProjectPhase.INITIATION);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    // Similar tests can be written for other methods in the ProjectController class
}
