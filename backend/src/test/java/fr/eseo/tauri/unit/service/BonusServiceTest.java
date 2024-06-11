package fr.eseo.tauri.unit.service;

import fr.eseo.tauri.model.Bonus;
import fr.eseo.tauri.model.Student;
import fr.eseo.tauri.model.Team;
import fr.eseo.tauri.model.User;
import fr.eseo.tauri.repository.BonusRepository;
import fr.eseo.tauri.exception.ResourceNotFoundException;
import fr.eseo.tauri.repository.StudentRepository;
import fr.eseo.tauri.repository.TeamRepository;
import fr.eseo.tauri.service.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@Nested
class BonusServiceTest {

    @Mock
    ValidationBonusService validationBonusService;

    @Mock
    UserService userService;


    @InjectMocks
    BonusService bonusService;

    @Mock
    StudentRepository studentRepository;

    @Mock
    TeamRepository teamRepository;

    @Mock
    BonusRepository bonusRepository;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getBonusByIdShouldReturnBonusWhenPermissionExistsAndBonusExists() {
        Integer id = 1;
        Bonus bonus = new Bonus();

        when(bonusRepository.findById(id)).thenReturn(Optional.of(bonus));

        Bonus result = bonusService.getBonusById(id);

        assertEquals(bonus, result);
    }

    @Test
    void getBonusByIdShouldThrowResourceNotFoundExceptionWhenBonusDoesNotExist() {
        Integer id = 1;

        when(bonusRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> bonusService.getBonusById(id));
    }

    @Test
    void getAllBonusesByProjectShouldReturnBonusesWhenPermissionExists() {
        Integer projectId = 1;
        List<Bonus> bonuses = new ArrayList<>();
        bonuses.add(new Bonus());

        when(bonusRepository.findAllByProject(projectId)).thenReturn(bonuses);

        List<Bonus> result = bonusService.getAllBonusesByProject(projectId);

        assertEquals(bonuses, result);
    }

    @Test
    void getAllBonusesByProjectShouldHandleNoBonuses() {
        Integer projectId = 1;
        List<Bonus> bonuses = new ArrayList<>();

        when(bonusRepository.findAllByProject(projectId)).thenReturn(bonuses);

        List<Bonus> result = bonusService.getAllBonusesByProject(projectId);

        assertTrue(result.isEmpty());
    }

    @Test
    void createBonusShouldSaveBonusWhenBonusIsValid() {
        Bonus bonus = new Bonus();
        bonus.id(1);
        bonus.value(3F);

        when(bonusRepository.save(any(Bonus.class))).thenReturn(bonus);

        bonusService.createBonus(bonus);

        verify(bonusRepository, times(1)).save(bonus);
    }


    @Test
    void updateBonusShouldThrowIllegalArgumentExceptionWhenBonusIsLimitedAndValueIsOutOfRange() {
        Integer id = 1;
        Bonus updatedBonus = new Bonus();
        updatedBonus.limited(true);
        updatedBonus.value(5F);

        when(bonusRepository.findById(id)).thenReturn(Optional.of(new Bonus()));

        assertThrows(IllegalArgumentException.class, () -> bonusService.updateBonus(id, updatedBonus));
    }


    @Test
    void deleteAllBonusesByProjectShouldDeleteBonusesWhenPermissionExists() {
        Integer projectId = 1;

        bonusService.deleteAllBonusesByProject(projectId);

        verify(bonusRepository, times(1)).deleteAllByProject(projectId);
    }

    @Test
    void deleteBonusShouldDeleteBonusWhenBonusExists() {
        Integer id = 1;
        Bonus bonus = new Bonus();

        when(bonusRepository.findById(id)).thenReturn(Optional.of(bonus));

        bonusService.deleteBonus(id);

        verify(bonusRepository, times(1)).deleteById(id);
    }

    @Test
    void deleteBonusShouldThrowResourceNotFoundExceptionWhenBonusDoesNotExist() {
        Integer id = 1;

        when(bonusRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> bonusService.deleteBonus(id));
    }

    @Test
    void updateBonusShouldUpdateBonusWhenBonusIsValidAndNotLimited() {
        Integer id = 1;
        Bonus updatedBonus = new Bonus();
        updatedBonus.limited(false);
        updatedBonus.value(2F);

        when(bonusRepository.findById(id)).thenReturn(Optional.of(new Bonus()));
        when(userService.getUserById(any())).thenReturn(new User());

        bonusService.updateBonus(id, updatedBonus);

        verify(bonusRepository, times(1)).save(any(Bonus.class));
    }

    @Test
    void updateBonusShouldUpdateBonusAndDeleteValidationsWhenBonusIsValidAndLimited() {
        Integer id = 1;
        Bonus updatedBonus = new Bonus();
        updatedBonus.value(2F);
        updatedBonus.limited(true);

        when(bonusRepository.findById(id)).thenReturn(Optional.of(new Bonus()));
        when(userService.getUserById(any())).thenReturn(new User());

        bonusService.updateBonus(id, updatedBonus);

        verify(bonusRepository, times(1)).save(any(Bonus.class));
        verify(validationBonusService, times(1)).deleteAllValidationBonuses(id);
    }

    @Test
    void getValidationBonusesByTeamShouldReturnBonusesWhenStudentsAndLeaderExist() {
        Integer teamId = 1;
        Student student1 = new Student();
        student1.id(1);
        Student student2 = new Student();
        student2.id(2);
        List<Student> students = Arrays.asList(student1, student2);
        User leader = new User().id(3);
        Bonus bonus1 = new Bonus().id(1);
        Bonus bonus2 = new Bonus().id(2);
        Bonus leaderBonus = new Bonus().id(3);

        when(studentRepository.findByTeam(teamId)).thenReturn(students);
        when(userService.getUserById(1)).thenReturn(new User().id(1));
        when(userService.getUserById(2)).thenReturn(new User().id(2));
        when(bonusRepository.findAllByAuthorId(1)).thenReturn(bonus1);
        when(bonusRepository.findAllByAuthorId(2)).thenReturn(bonus2);
        when(teamRepository.findLeaderByTeamId(teamId)).thenReturn(leader);
        when(bonusRepository.findAllByAuthorId(3)).thenReturn(leaderBonus);

        List<Bonus> result = bonusService.getValidationBonusesByTeam(teamId);

        assertTrue(result.contains(bonus1));
        assertTrue(result.contains(bonus2));
        assertTrue(result.contains(leaderBonus));
    }

    @Test
    void getValidationBonusesByTeamShouldReturnEmptyListWhenNoStudentsExist() {
        Integer teamId = 1;
        Team team = new Team().id(1);
        User leader = new User().id(1);
        team.leader(leader);

        when(teamRepository.findLeaderByTeamId(teamId)).thenReturn(leader);
        when(studentRepository.findByTeam(teamId)).thenReturn(Collections.emptyList());

        List<Bonus> result = bonusService.getValidationBonusesByTeam(teamId);

        assertTrue(result.isEmpty());
    }

    @Test
    void getValidationBonusesByTeamShouldReturnOnlyLeaderBonusWhenNoStudentsExist() {
        Integer teamId = 1;
        User leader = new User().id(1);
        Bonus leaderBonus = new Bonus().id(1);

        when(studentRepository.findByTeam(teamId)).thenReturn(Collections.emptyList());
        when(teamRepository.findLeaderByTeamId(teamId)).thenReturn(leader);
        when(bonusRepository.findAllByAuthorId(1)).thenReturn(leaderBonus);

        List<Bonus> result = bonusService.getValidationBonusesByTeam(teamId);

        assertEquals(1, result.size());
        assertTrue(result.contains(leaderBonus));
    }

}
