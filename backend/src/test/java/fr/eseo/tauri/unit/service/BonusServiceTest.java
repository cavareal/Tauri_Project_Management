package fr.eseo.tauri.unit.service;

import fr.eseo.tauri.model.Bonus;
import fr.eseo.tauri.repository.BonusRepository;
import fr.eseo.tauri.exception.ResourceNotFoundException;
import fr.eseo.tauri.service.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@Nested
class BonusServiceTest {

    @Mock
    ValidationBonusService validationBonusService;

    @Mock
    UserService userService;

    @Mock
    StudentService studentService;

    @Mock
    SprintService sprintService;

    @InjectMocks
    BonusService bonusService;

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

//    @Test
//    void createBonusShouldSaveBonusWhenPermissionExistsAndBonusIsValid() {
//        String token = "validToken";
//        Bonus bonus = new Bonus();
//        bonus.limited(true);
//        bonus.value(3F);
//
//        when(authService.checkAuth(token, "addBonus")).thenReturn(true);
//        when(userService.getUserById(token, bonus.authorId())).thenReturn(new User());
//        when(studentService.getStudentById(token, bonus.studentId())).thenReturn(new Student());
//        when(sprintService.getSprintById(token, bonus.sprintId())).thenReturn(new Sprint());
//
//        bonusService.createBonus(token, bonus);
//
//        verify(bonusRepository, times(1)).save(bonus);
//        verify(validationBonusService, times(1)).createValidationBonuses(token, bonus);
//    }


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
}
