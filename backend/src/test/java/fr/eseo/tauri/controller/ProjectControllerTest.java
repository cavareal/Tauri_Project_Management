package fr.eseo.tauri.controller;

import fr.eseo.tauri.model.Project;
import fr.eseo.tauri.model.enumeration.ProjectPhase;
import fr.eseo.tauri.repository.ProjectRepository;
import fr.eseo.tauri.service.AuthService;
import fr.eseo.tauri.service.ProjectService;
import org.junit.jupiter.api.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@Nested
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
    @Order(1)
    @DisplayName("Test getCurrentProject when project exists")
    void getCurrentProject_returnsProject_whenProjectExists() {
        // Arrange
        Project project = new Project();
        when(projectRepository.findAll()).thenReturn(Collections.singletonList(project));

        // Act
        ResponseEntity<Project> response = projectController.getCurrentProject();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(project, response.getBody());
    }

    @Test
    @Order(2)
    @DisplayName("Test getCurrentProject when no project exists")
    void getCurrentProject_returnsNotFound_whenNoProjectExists() {
        // Arrange
        when(projectRepository.findAll()).thenReturn(Collections.emptyList());

        // Act
        ResponseEntity<Project> response = projectController.getCurrentProject();

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    @Order(3)
    @DisplayName("Test newProject when project is created successfully")
    void newProject_returnsOk_whenProjectCreatedSuccessfully() {
        // Arrange
        String token = "token";
        when(authService.checkAuth(token, "teamCreation")).thenReturn(true);
        when(projectService.newProject(anyInt(), anyInt(), anyInt(), any(ProjectPhase.class))).thenReturn(new Project());

        // Act
        ResponseEntity<String> response = projectController.newProject(token, 1, 1, 1, ProjectPhase.COMPOSING);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    @Order(4)
    @DisplayName("Test newProject when user is not authorized")
    void newProject_returnsUnauthorized_whenUserNotAuthorized() {
        // Arrange
        String token = "token";
        when(authService.checkAuth(token, "teamCreation")).thenReturn(false);

        // Act
        ResponseEntity<String> response = projectController.newProject(token, 1, 1, 1, ProjectPhase.COMPOSING);

        // Assert
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    @Test
    @Order(5)
    @DisplayName("Test newProject when project creation fails")
    void newProject_returnsInternalServerError_whenProjectCreationFails() {
        // Arrange
        String token = "token";
        when(authService.checkAuth(token, "teamCreation")).thenReturn(true);
        when(projectService.newProject(anyInt(), anyInt(), anyInt(), any(ProjectPhase.class))).thenThrow(new RuntimeException());

        // Act
        ResponseEntity<String> response = projectController.newProject(token, 1, 1, 1, ProjectPhase.COMPOSING);

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    @Order(6)
    @DisplayName("Test updateProjectSprintsNumber when update is successful")
    void updateProjectSprintsNumber_returnsOk_whenUpdateIsSuccessful() {
        // Arrange
        String token = "token";
        Integer projectId = 1;
        Integer newSprintsNumber = 5;
        Map<String, String> request = new HashMap<>();
        request.put("nbSprints", newSprintsNumber.toString());
        when(authService.checkAuth(token, "ManageTeamsNumber")).thenReturn(true);
        when(projectService.updateProjectSprintsNumber(projectId, newSprintsNumber)).thenReturn(new Project());

        // Act
        ResponseEntity<String> response = projectController.updateProjectSprintsNumber(token, projectId, request);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    @Order(7)
    @DisplayName("Test getNumberSprints when retrieval is successful")
    void getNumberSprints_returnsOk_whenRetrievalIsSuccessful() {
        // Arrange
        String token = "token";
        when(authService.checkAuth(token, "readSprintNumber")).thenReturn(true);
        when(projectService.getNumberSprints()).thenReturn("5");

        // Act
        ResponseEntity<String> response = projectController.getNumberSprints(token);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    @Order(8)
    @DisplayName("Test updateProjectTeamsNumber when update is successful")
    void updateProjectTeamsNumber_returnsOk_whenUpdateIsSuccessful() {
        // Arrange
        String token = "token";
        Integer projectId = 1;
        Integer newTeamsNumber = 5;
        when(authService.checkAuth(token, "manageSprint")).thenReturn(true);
        when(projectService.updateProjectTeamsNumber(projectId, newTeamsNumber)).thenReturn(new Project());

        // Act
        ResponseEntity<String> response = projectController.updateProjectTeamsNumber(token, projectId, newTeamsNumber);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    @Order(9)
    @DisplayName("Test updateProjectRatioGender when update is successful")
    void updateProjectRatioGender_returnsOk_whenUpdateIsSuccessful() {
        // Arrange
        String token = "token";
        Integer projectId = 1;
        Integer newRatioGender = 5;
        when(authService.checkAuth(token, "manageRatioGender")).thenReturn(true);
        when(projectService.updateProjectRatioGender(projectId, newRatioGender)).thenReturn(new Project());

        // Act
        ResponseEntity<String> response = projectController.updateProjectRatioGender(token, projectId, newRatioGender);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    @Order(10)
    @DisplayName("Test updateProjectPhase when update is successful")
    void updateProjectPhase_returnsOk_whenUpdateIsSuccessful() {
        // Arrange
        String token = "token";
        Integer projectId = 1;
        ProjectPhase newPhase = ProjectPhase.COMPOSING;
        when(authService.checkAuth(token, "manageProjectPhase")).thenReturn(true);
        when(projectService.updateProjectPhase(projectId, newPhase)).thenReturn(new Project());

        // Act
        ResponseEntity<String> response = projectController.updateProjectPhase(token, projectId, newPhase);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    @Order(11)
    @DisplayName("Test deleteProject when deletion is successful")
    void deleteProject_returnsOk_whenDeletionIsSuccessful() {
        // Arrange
        String token = "token";
        Integer id = 1;
        when(authService.checkAuth(token, "teamDelete")).thenReturn(true);
        when(projectService.deleteProject(id)).thenReturn(new Project());

        // Act
        ResponseEntity<String> response = projectController.deleteProject(token, id);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    @Order(12)
    @DisplayName("Test getCurrentPhase when retrieval is successful")
    void getCurrentPhase_returnsOk_whenRetrievalIsSuccessful() {
        // Arrange
        String token = "token";
        when(authService.checkAuth(token, "readProjectPhase")).thenReturn(true);
        when(projectService.getCurrentPhase()).thenReturn("COMPOSING");

        // Act
        ResponseEntity<String> response = projectController.getCurrentPhase(token);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

}
