package fr.eseo.tauri.service;

import fr.eseo.tauri.model.Project;
import fr.eseo.tauri.model.Student;
import fr.eseo.tauri.model.Team;
import fr.eseo.tauri.model.User;
import fr.eseo.tauri.model.enumeration.Gender;
import fr.eseo.tauri.repository.StudentRepository;
import fr.eseo.tauri.repository.TeamRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.lang.reflect.Method;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@Nested
class TeamServiceTest {

    @Mock
    private TeamRepository teamRepository;

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private ProjectService projectService;

    @InjectMocks
    private TeamService teamService;

    @BeforeEach
    void init_mocks() {
        MockitoAnnotations.openMocks(this);
        teamService = spy(teamService);
    }

    @Test
    void testGetAllTeams() {
        // Arrange
        Team team1 = new Team();
        Team team2 = new Team();
        Project project = new Project();
        when(teamRepository.findAllByProjectId(project.id())).thenReturn(Arrays.asList(team1, team2));
        when(projectService.getCurrentProject()).thenReturn(project);

        // Act
        List<Team> teams = teamService.getAllTeams();

        // Assert
        assertThat(teams).hasSize(2);
        verify(teamRepository, times(1)).findAllByProjectId(project.id());
        verify(projectService, times(1)).getCurrentProject();

    }

    @Test
    void testGetAllTeamsEmptyList() {
        // Arrange
        Project project = new Project();
        when(projectService.getCurrentProject()).thenReturn(project);
        when(teamRepository.findAllByProjectId(project.id())).thenReturn(List.of());

        // Action
        List<Team> teams = teamService.getAllTeams();

        // Assert
        assertThat(teams).isEmpty();
        verify(teamRepository, times(1)).findAllByProjectId(project.id());
        verify(projectService, times(1)).getCurrentProject();
    }

    @Test
    void testGetTeamById() {
        // Arrange
        Team team = new Team();
        when(teamRepository.findById(1)).thenReturn(Optional.of(team));

        // Act
        Team result = teamService.getTeamById(1);

        // Assert
        assertThat(result).isEqualTo(team);
        verify(teamRepository, times(1)).findById(1);
    }

    @Test
    void testGetTeamByName() {
        // Arrange
        Team team = new Team();
        String teamName = "Team 1";
        team.name(teamName);
        when(teamRepository.findByName(teamName)).thenReturn(team);

        // Act
        Team result = teamRepository.findByName(teamName);

        // Assert
        assertThat(result).isEqualTo(team);
        verify(teamRepository, times(1)).findByName(teamName);
    }

    @Test
    void testGetTeamByIdNull() {
        // Arrange
        when(teamRepository.findById(1)).thenReturn(Optional.empty());

        // Act
        Team result = teamService.getTeamById(1);

        // Assert
        assertThat(result).isNull();
    }

    @Test
    void testGetNbWomanByTeamId() {
        // Arrange
        Team team = new Team();
        Student student1 = new Student();
        student1.gender(Gender.WOMAN);
        Student student2 = new Student();
        student2.gender(Gender.MAN);
        when(teamRepository.findById(1)).thenReturn(Optional.of(team));
        when(studentRepository.findByTeamId(team)).thenReturn(Arrays.asList(student1, student2));

        // Act
        Integer nbWomanTeam1 = teamService.getNbWomanByTeamId(1);
        Integer nbWomanTeam2 = teamService.getNbWomanByTeamId(2);

        // Assert
        assertThat(nbWomanTeam1).isEqualTo(1);
        assertThat(nbWomanTeam2).isNull();
    }

    @Test
    void testGetNbWomanByTeamIdZero() {
        // Arrange
        Team team = new Team();
        Student student1 = new Student();
        student1.gender(Gender.MAN);
        Student student2 = new Student();
        student2.gender(Gender.MAN);
        when(teamRepository.findById(1)).thenReturn(Optional.of(team));
        when(studentRepository.findByTeamId(team)).thenReturn(Arrays.asList(student1, student2));

        // Act
        Integer nbWoman = teamService.getNbWomanByTeamId(1);

        // Assert
        assertThat(nbWoman).isZero();
    }

    @Test
    void testGetNbBachelorsByTeamId() {
        // Given
        Team team = new Team();
        Student student1 = new Student();
        student1.bachelor(true);
        Student student2 = new Student();
        student2.bachelor(false);
        when(teamRepository.findById(1)).thenReturn(Optional.of(team));
        when(studentRepository.findByTeamId(team)).thenReturn(Arrays.asList(student1, student2));

        // When
        Integer nbBachelorsTeam1 = teamService.getNbBachelorByTeamId(1);
        Integer nbBachelorsTeam2 = teamService.getNbBachelorByTeamId(2);

        // Then
        assertThat(nbBachelorsTeam1).isEqualTo(1);
        assertThat(nbBachelorsTeam2).isNull();
    }

    @Test
    void testGetNbBachelorsByTeamIdZero() {
        // Arrange
        Team team = new Team();
        Student student1 = new Student();
        student1.bachelor(false);
        Student student2 = new Student();
        student2.bachelor(false);
        when(teamRepository.findById(1)).thenReturn(Optional.of(team));
        when(studentRepository.findByTeamId(team)).thenReturn(Arrays.asList(student1, student2));

        // Act
        Integer nbBachelors = teamService.getNbBachelorByTeamId(1);

        // Assert
        assertThat(nbBachelors).isZero();
    }

    @Test
    void testGetNbStudentsByTeamId() {
        // Arrange
        Team team = new Team();
        Student student1 = new Student();
        Student student2 = new Student();
        when(teamRepository.findById(1)).thenReturn(Optional.of(team));
        when(studentRepository.findByTeamId(team)).thenReturn(Arrays.asList(student1, student2));

        // Act
        Integer nbStudentsTeam1 = teamService.getNbStudentsByTeamId(1);
        Integer nbStudentsTeam2 = teamService.getNbStudentsByTeamId(2);


        // Assert
        assertThat(nbStudentsTeam1).isEqualTo(2);
        assertThat(nbStudentsTeam2).isNull();
    }

    @Test
    void testGetNbStudentsByTeamIdZero() {
        // Arrange
        Team team = new Team();
        when(teamRepository.findById(1)).thenReturn(Optional.of(team));
        when(studentRepository.findByTeamId(team)).thenReturn(List.of());

        // Act
        Integer nbStudents = teamService.getNbStudentsByTeamId(1);

        // Assert
        assertThat(nbStudents).isZero();
    }

    @Test
    void testGetTeamBySSId_ExistingId() {
        // Arrange
        User leader = new User();
        leader.id(1);
        Team team1 = new Team();
        team1.leader(leader);
        Team team2 = new Team();
        doReturn(Arrays.asList(team1, team2)).when(teamService).getAllTeams();

        // Act
        Team result = teamService.getTeamBySSId(1);

        // Assert
        assertThat(result).isEqualTo(team1);
        verify(teamService, times(1)).getAllTeams();
    }

    @Test
    void testGetTeamBySSId_NonExistingId() {
        // Arrange
        User leader = new User();
        leader.id(1);
        Team team1 = new Team();
        Team team2 = new Team();
        doReturn(Arrays.asList(team1, team2)).when(teamService).getAllTeams();

        // Act
        Team result = teamService.getTeamBySSId(1);

        // Assert
        assertThat(result).isNull();
        verify(teamService, times(1)).getAllTeams();
    }

    /*@Test
    void testGenerateTeams() {
        // Arrange
        Integer nbTeams = 3;
        Integer womenPerTeam = 2;
        List<Team> expectedTeams = new ArrayList<>();
        for (int i = 0; i < nbTeams; i++) {
            Team team = new Team();
            expectedTeams.add(team);
        }

        // Mock the dependencies
        when(studentRepository.findByGender(Gender.WOMAN)).thenReturn(new ArrayList<>());
        when(studentRepository.findByGenderOrderByBachelorAndImportedAvgDesc(Gender.MAN)).thenReturn(new ArrayList<>());
        doReturn(expectedTeams).when(teamService).createTeams(nbTeams);
        doNothing().when(teamService).fillTeams(anyList(), anyList(), anyList(), anyInt());

        // Act
        List<Team> result = teamService.generateTeams(nbTeams, womenPerTeam);

        // Assert
        assertEquals(expectedTeams, result);
        verify(studentRepository, times(1)).findByGender(Gender.WOMAN);
        verify(studentRepository, times(1)).findByGenderOrderByBachelorAndImportedAvgDesc(Gender.MAN);
        verify(teamService, times(1)).createTeams(nbTeams);
        verify(teamService, times(1)).fillTeams(anyList(), anyList(), anyList(), anyInt());
    }*/

//    @Test
//    void testFillTeams() throws Exception {
//        // Arrange
//        List<Team> teams = new ArrayList<>();
//        List<Student> women = new ArrayList<>();
//        List<Student> men = new ArrayList<>();
//        int nbTeams = 3;
//        int womenPerTeam = 2;
//
//        for (int i = 0; i < nbTeams; i++) {
//            Team team = new Team();
//            teams.add(team);
//        }
//
//        for (int i = 0; i < nbTeams * womenPerTeam; i++) {
//            Student woman = new Student();
//            woman.gender(Gender.WOMAN);
//            women.add(woman);
//
//            Student man = new Student();
//            man.gender(Gender.MAN);
//            men.add(man);
//        }
//
//        when(teamRepository.findAllOrderByAvgGradeOrderByAsc()).thenReturn(teams);
//
//        // Act
//        Method method = TeamService.class.getDeclaredMethod("fillTeams", List.class, List.class, List.class, Integer.class);
//        method.setAccessible(true);
//        method.invoke(teamService, teams, women, men, womenPerTeam);
//
//        // Assert
//        for (Team team : teams) {
//            /*int womenCount = (int) studentRepository.findByTeam(team).stream()
//                    .filter(student -> student.gender() == Gender.WOMAN)
//                    .count();*/
//            int womenCount = 0;
//            List<Student> studentsInTeam = studentRepository.findByTeam(team);
//            assertEquals(4, studentsInTeam.size());
//            for (Student student : studentsInTeam) {
//                if (student.gender() == Gender.WOMAN) {
//                    womenCount++;
//                }
//            }
//            assertEquals(womenPerTeam, womenCount);
//        }
//    }

    @Test
    void getTeamAvgGradeReturnsCorrectAverageWhenTeamExists() {
        // Arrange
        Integer teamId = 1;
        double expectedAverage = 85.0;
        Team team = new Team();
        when(teamRepository.findById(teamId)).thenReturn(Optional.of(team));
        when(teamRepository.findAvgGradeByTeam(team)).thenReturn(expectedAverage);

        // Act
        double result = teamService.getTeamAvgGrade(teamId);

        // Assert
        assertThat(result).isEqualTo(expectedAverage);
        verify(teamRepository, times(1)).findById(teamId);
        verify(teamRepository, times(1)).findAvgGradeByTeam(team);
    }

    @Test
    void getTeamAvgGradeThrowsExceptionWhenTeamDoesNotExist() {
        // Arrange
        Integer teamId = 1;
        when(teamRepository.findById(teamId)).thenReturn(Optional.empty());

        // Act and Assert
        assertThrows(IllegalArgumentException.class, () -> teamService.getTeamAvgGrade(teamId));
        verify(teamRepository, times(1)).findById(teamId);
    }

    @Test
    void createTeamsReturnsCorrectNumberOfTeamsWhenTeamsDoNotExist() {
        // Arrange
        Integer nbTeams = 3;
        int projectId = 1;
        List<Team> result;

        // Mocks
        Project projectMock = mock(Project.class);

        // Mocks behavior
        when(projectService.getCurrentProject()).thenReturn(projectMock);
        when(projectMock.id()).thenReturn(projectId);
        when(teamRepository.findAllByProjectId(projectMock.id())).thenReturn(List.of());

        try {
            Method method = TeamService.class.getDeclaredMethod("createTeams", Integer.class);
            method.setAccessible(true);

            // Act
            result = (List<Team>) method.invoke(teamService, nbTeams);

            // Asserts
            assertNotNull(result, "The result should not be null");
            assertEquals(nbTeams,result.size(), "The result should contain " + nbTeams + " teams");
            verify(teamRepository, times(nbTeams)).save(any(Team.class));
        }catch (Exception e) {
            fail(e.getMessage());
        }
    }

}




