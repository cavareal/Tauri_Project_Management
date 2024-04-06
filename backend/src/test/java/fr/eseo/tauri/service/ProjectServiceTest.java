package fr.eseo.tauri.service;

import fr.eseo.tauri.model.Project;
import fr.eseo.tauri.repository.ProjectRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.Collections;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.*;

@SpringBootTest
public class ProjectServiceTest {

    @Mock
    private ProjectRepository projectRepository;

    @InjectMocks
    private ProjectService projectService;

    @Test
    public void testGetRatioGender() {
        // Arrange
        Project project = new Project();
        project.ratioGender(10);
        when(projectRepository.findAll()).thenReturn(Arrays.asList(project));

        // Act
        Integer ratioGender = projectService.getRatioGender();

        // Assert
        assertThat(ratioGender).isEqualTo(10);
    }

    @Test
    public void testGetRatioGenderZero() {
        // Arrange
        when(projectRepository.findAll()).thenReturn(Collections.emptyList());

        // Act
        Integer ratioGender = projectService.getRatioGender();

        // Assert
        assertThat(ratioGender).isEqualTo(0);
    }
}