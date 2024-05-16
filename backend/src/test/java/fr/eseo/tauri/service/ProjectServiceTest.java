package fr.eseo.tauri.service;

import fr.eseo.tauri.exception.ResourceNotFoundException;
import fr.eseo.tauri.model.Project;
import fr.eseo.tauri.model.enumeration.ProjectPhase;
import fr.eseo.tauri.repository.ProjectRepository;
import org.junit.jupiter.api.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@Nested
class ProjectServiceTest {

    @Mock
    private ProjectRepository projectRepository;

    @Mock
    private AuthService authService;

    @InjectMocks
    private ProjectService projectService;

    @BeforeEach
    void init_mocks() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getProjectByIdShouldReturnProjectWhenAuthorizedAndIdExists() {
        Project project = new Project();
        when(authService.checkAuth(anyString(), anyString())).thenReturn(true);
        when(projectRepository.findById(anyInt())).thenReturn(Optional.of(project));

        Project result = projectService.getProjectById("token", 1);

        assertEquals(project, result);
    }

    @Test
    void getProjectByIdShouldThrowSecurityExceptionWhenUnauthorized() {
        when(authService.checkAuth(anyString(), anyString())).thenReturn(false);

        assertThrows(SecurityException.class, () -> projectService.getProjectById("token", 1));
    }

    @Test
    void getProjectByIdShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() {
        when(authService.checkAuth(anyString(), anyString())).thenReturn(true);
        when(projectRepository.findById(anyInt())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> projectService.getProjectById("token", 1));
    }

    @Test
    void getAllProjectsShouldReturnProjectsWhenAuthorized() {
        List<Project> projects = new ArrayList<>();
        when(authService.checkAuth(anyString(), anyString())).thenReturn(true);
        when(projectRepository.findAll()).thenReturn(projects);

        List<Project> result = projectService.getAllProjects("token");

        assertEquals(projects, result);
    }

    @Test
    void getAllProjectsShouldThrowSecurityExceptionWhenUnauthorized() {
        when(authService.checkAuth(anyString(), anyString())).thenReturn(false);

        assertThrows(SecurityException.class, () -> projectService.getAllProjects("token"));
    }

    @Test
    void createProjectShouldCreateWhenAuthorized() {
        Project project = new Project();
        when(authService.checkAuth(anyString(), anyString())).thenReturn(true);

        projectService.createProject("token", project);

        verify(projectRepository, times(1)).save(project);
    }

    @Test
    void createProjectShouldThrowSecurityExceptionWhenUnauthorized() {
        Project project = new Project();
        when(authService.checkAuth(anyString(), anyString())).thenReturn(false);

        assertThrows(SecurityException.class, () -> projectService.createProject("token", project));
    }

    @Test
    void updateProjectShouldUpdateWhenAuthorizedAndIdExists() {
        Project project = new Project();
        Project updatedProject = new Project();
        updatedProject.nbTeams(5);
        updatedProject.nbWomen(3);
        updatedProject.phase(ProjectPhase.COMPOSING);
        when(authService.checkAuth(anyString(), anyString())).thenReturn(true);
        when(projectRepository.findById(anyInt())).thenReturn(Optional.of(project));

        projectService.updateProject("token", 1, updatedProject);

        verify(projectRepository, times(1)).save(project);
    }

    @Test
    void updateProjectShouldThrowSecurityExceptionWhenUnauthorized() {
        Project updatedProject = new Project();
        when(authService.checkAuth(anyString(), anyString())).thenReturn(false);

        assertThrows(SecurityException.class, () -> projectService.updateProject("token", 1, updatedProject));
    }

    @Test
    void updateProjectShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() {
        Project updatedProject = new Project();
        when(authService.checkAuth(anyString(), anyString())).thenReturn(true);
        when(projectRepository.findById(anyInt())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> projectService.updateProject("token", 1, updatedProject));
    }

    @Test
    void deleteAllProjectsShouldDeleteWhenAuthorized() {
        when(authService.checkAuth(anyString(), anyString())).thenReturn(true);

        projectService.deleteAllProjects("token");

        verify(projectRepository, times(1)).deleteAll();
    }

    @Test
    void deleteAllProjectsShouldThrowSecurityExceptionWhenUnauthorized() {
        when(authService.checkAuth(anyString(), anyString())).thenReturn(false);

        assertThrows(SecurityException.class, () -> projectService.deleteAllProjects("token"));
    }

    @Test
    void deleteProjectByIdShouldDeleteWhenAuthorizedAndIdExists() {
        when(authService.checkAuth(anyString(), anyString())).thenReturn(true);
        when(projectRepository.findById(anyInt())).thenReturn(Optional.of(new Project()));

        projectService.deleteProjectById("token", 1);

        verify(projectRepository, times(1)).deleteById(1);
    }

    @Test
    void deleteProjectByIdShouldThrowSecurityExceptionWhenUnauthorized() {
        when(authService.checkAuth(anyString(), anyString())).thenReturn(false);

        assertThrows(SecurityException.class, () -> projectService.deleteProjectById("token", 1));
    }

    @Test
    void deleteProjectByIdShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() {
        when(authService.checkAuth(anyString(), anyString())).thenReturn(true);
        when(projectRepository.findById(anyInt())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> projectService.deleteProjectById("token", 1));
    }
}