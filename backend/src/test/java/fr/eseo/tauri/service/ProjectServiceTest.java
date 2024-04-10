package fr.eseo.tauri.service;

import fr.eseo.tauri.model.Project;
import fr.eseo.tauri.model.enumeration.ProjectPhase;
import fr.eseo.tauri.repository.ProjectRepository;
import org.junit.jupiter.api.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.EmptyResultDataAccessException;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;

@Nested
class ProjectServiceTest {

    @Mock
    private ProjectRepository projectRepository;

    @InjectMocks
    private ProjectService projectService;

    @BeforeEach
    void init_mocks() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @Order(1)
    @DisplayName("Test newProject when creation is successful")
    void newProject_returnsProject_whenCreationIsSuccessful() {
        // Arrange
        Project project = new Project();
        when(projectRepository.save(any(Project.class))).thenReturn(project);

        // Act
        Project result = projectService.newProject(1, 1, 1, ProjectPhase.COMPOSING);

        // Assert
        assertEquals(project, result);
    }

    @Test
    @Order(2)
    @DisplayName("Test newProject when creation fails")
    void newProject_returnsNull_whenCreationFails() {
        // Arrange
        when(projectRepository.save(any(Project.class))).thenThrow(new EmptyResultDataAccessException(1));
        // Act
        Project result = projectService.newProject(1, 1, 1, ProjectPhase.COMPOSING);

        // Assert
        assertNull(result);
    }

    @Test
    @Order(3)
    @DisplayName("Test updateProjectSprintsNumber when update is successful")
    void updateProjectSprintsNumber_returnsProject_whenUpdateIsSuccessful() {
        // Arrange
        Project project = new Project();
        when(projectRepository.findById(anyInt())).thenReturn(Optional.of(project));
        when(projectRepository.save(any(Project.class))).thenReturn(project);

        // Act
        Project result = projectService.updateProjectSprintsNumber(1, 5);

        // Assert
        assertEquals(project, result);
    }
    @Test
    @Order(4)
    @DisplayName("Test getNumberSprints when retrieval is successful")
    void getNumberSprints_returnsSprintsNumber_whenRetrievalIsSuccessful() {
        // Arrange
        Project project = new Project();
        project.nbSprint(5);
        when(projectRepository.findAll()).thenReturn(Collections.singletonList(project));

        // Act
        String result = projectService.getNumberSprints();

        // Assert
        assertEquals("5", result);
    }

    @Test
    @Order(5)
    @DisplayName("Test updateProjectTeamsNumber when update is successful")
    void updateProjectTeamsNumber_returnsProject_whenUpdateIsSuccessful() {
        // Arrange
        Project project = new Project();
        when(projectRepository.findById(anyInt())).thenReturn(Optional.of(project));
        when(projectRepository.save(any(Project.class))).thenReturn(project);

        // Act
        Project result = projectService.updateProjectTeamsNumber(1, 5);

        // Assert
        assertEquals(project, result);
    }

    @Test
    @Order(6)
    @DisplayName("Test updateProjectRatioGender when update is successful")
    void updateProjectRatioGender_returnsProject_whenUpdateIsSuccessful() {
        // Arrange
        Project project = new Project();
        when(projectRepository.findById(anyInt())).thenReturn(Optional.of(project));
        when(projectRepository.save(any(Project.class))).thenReturn(project);

        // Act
        Project result = projectService.updateProjectRatioGender(1, 5);

        // Assert
        assertEquals(project, result);
    }

    @Test
    @Order(7)
    @DisplayName("Test updateProjectPhase when update is successful")
    void updateProjectPhase_returnsProject_whenUpdateIsSuccessful() {
        // Arrange
        Project project = new Project();
        when(projectRepository.findById(anyInt())).thenReturn(Optional.of(project));
        when(projectRepository.save(any(Project.class))).thenReturn(project);

        // Act
        Project result = projectService.updateProjectPhase(1, ProjectPhase.COMPOSING);

        // Assert
        assertEquals(project, result);
    }

    @Test
    @Order(8)
    @DisplayName("Test deleteProject when deletion is successful")
    void deleteProject_returnsProject_whenDeletionIsSuccessful() {
        // Arrange
        Project project = new Project();
        when(projectRepository.findById(anyInt())).thenReturn(Optional.of(project));
        doNothing().when(projectRepository).delete(project);

        // Act
        Project result = projectService.deleteProject(1);

        // Assert
        assertEquals(project, result);
    }

    @Test
    @Order(9)
    @DisplayName("Test getCurrentPhase when retrieval is successful")
    void getCurrentPhase_returnsPhase_whenRetrievalIsSuccessful() {
        // Arrange
        Project project = new Project();
        project.phase(ProjectPhase.COMPOSING);
        when(projectRepository.findAll()).thenReturn(Collections.singletonList(project));

        // Act
        String result = projectService.getCurrentPhase();

        // Assert
        assertEquals("COMPOSING", result);
    }

    @Test
    @Order(10)
    @DisplayName("Test initDataIfTableIsEmpty when table is empty")
    void initDataIfTableIsEmpty_createsProject_whenTableIsEmpty() {
        // Arrange
        when(projectRepository.count()).thenReturn(0L);
        Project project = new Project();
        when(projectRepository.save(any(Project.class))).thenReturn(project);

        // Act
        projectService.initDataIfTableIsEmpty();

        // Assert
        verify(projectRepository, times(1)).save(any(Project.class));
    }

    @Test
    @DisplayName("getRatioGender returns ratio when project exists")
    void getRatioGender_returnsRatio_whenProjectExists() {
        // Arrange
        Project project = new Project();
        project.ratioGender(10);
        when(projectRepository.findAll()).thenReturn(Collections.singletonList(project));

        // Act
        Integer ratioGender = projectService.getRatioGender();

        // Assert
        assertEquals(10, ratioGender);
    }

    @Test
    @DisplayName("getRatioGender returns zero when no project exists")
    void getRatioGender_returnsZero_whenNoProjectExists() {
        // Arrange
        when(projectRepository.findAll()).thenReturn(Collections.emptyList());

        // Act
        Integer ratioGender = projectService.getRatioGender();

        // Assert
        assertEquals(0, ratioGender);
    }

    @Test
    @DisplayName("getCurrentProject returns project when project exists")
    void getCurrentProject_returnsProject_whenProjectExists() {
        // Arrange
        Project project = new Project();
        when(projectRepository.findAll()).thenReturn(Collections.singletonList(project));

        // Act
        Project result = projectService.getCurrentProject();

        // Assert
        assertEquals(project, result);
    }

    @Test
    @DisplayName("getCurrentProject returns null when no project exists")
    void getCurrentProject_returnsNull_whenNoProjectExists() {
        // Arrange
        when(projectRepository.findAll()).thenReturn(Collections.emptyList());

        // Act
        Project result = projectService.getCurrentProject();

        // Assert
        assertNull(result);
    }
}