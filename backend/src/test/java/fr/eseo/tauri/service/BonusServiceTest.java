package fr.eseo.tauri.service;

import fr.eseo.tauri.model.Bonus;
import fr.eseo.tauri.model.Sprint;
import fr.eseo.tauri.model.Student;
import fr.eseo.tauri.model.User;
import fr.eseo.tauri.repository.BonusRepository;
import fr.eseo.tauri.exception.ResourceNotFoundException;
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
    AuthService authService;

    @Mock
    BonusRepository bonusRepository;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getBonusByIdShouldReturnBonusWhenPermissionExistsAndBonusExists() {
        String token = "validToken";
        Integer id = 1;
        Bonus bonus = new Bonus();

        when(authService.checkAuth(token, "readBonus")).thenReturn(true);
        when(bonusRepository.findById(id)).thenReturn(Optional.of(bonus));

        Bonus result = bonusService.getBonusById(token, id);

        assertEquals(bonus, result);
    }

    @Test
    void getBonusByIdShouldThrowSecurityExceptionWhenPermissionDoesNotExist() {
        String token = "validToken";
        Integer id = 1;

        when(authService.checkAuth(token, "readBonus")).thenReturn(false);

        assertThrows(SecurityException.class, () -> bonusService.getBonusById(token, id));
    }

    @Test
    void getBonusByIdShouldThrowResourceNotFoundExceptionWhenBonusDoesNotExist() {
        String token = "validToken";
        Integer id = 1;

        when(authService.checkAuth(token, "readBonus")).thenReturn(true);
        when(bonusRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> bonusService.getBonusById(token, id));
    }

    @Test
    void getAllBonusesByProjectShouldReturnBonusesWhenPermissionExists() {
        String token = "validToken";
        Integer projectId = 1;
        List<Bonus> bonuses = new ArrayList<>();
        bonuses.add(new Bonus());

        when(authService.checkAuth(token, "readBonuses")).thenReturn(true);
        when(bonusRepository.findAllByProject(projectId)).thenReturn(bonuses);

        List<Bonus> result = bonusService.getAllBonusesByProject(token, projectId);

        assertEquals(bonuses, result);
    }

    @Test
    void getAllBonusesByProjectShouldThrowSecurityExceptionWhenPermissionDoesNotExist() {
        String token = "validToken";
        Integer projectId = 1;

        when(authService.checkAuth(token, "readBonuses")).thenReturn(false);

        assertThrows(SecurityException.class, () -> bonusService.getAllBonusesByProject(token, projectId));
    }

    @Test
    void getAllBonusesByProjectShouldHandleNoBonuses() {
        String token = "validToken";
        Integer projectId = 1;
        List<Bonus> bonuses = new ArrayList<>();

        when(authService.checkAuth(token, "readBonuses")).thenReturn(true);
        when(bonusRepository.findAllByProject(projectId)).thenReturn(bonuses);

        List<Bonus> result = bonusService.getAllBonusesByProject(token, projectId);

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
    void createBonusShouldThrowSecurityExceptionWhenPermissionDoesNotExist() {
        String token = "validToken";
        Bonus bonus = new Bonus();

        when(authService.checkAuth(token, "addBonus")).thenReturn(false);

        assertThrows(SecurityException.class, () -> bonusService.createBonus(token, bonus));
    }

    @Test
    void updateBonusShouldThrowSecurityExceptionWhenPermissionDoesNotExist() {
        String token = "validToken";
        Integer id = 1;
        Bonus updatedBonus = new Bonus();

        when(authService.checkAuth(token, "updateBonus")).thenReturn(false);

        assertThrows(SecurityException.class, () -> bonusService.updateBonus(token, id, updatedBonus));
    }

    @Test
    void updateBonusShouldThrowIllegalArgumentExceptionWhenBonusIsLimitedAndValueIsOutOfRange() {
        String token = "validToken";
        Integer id = 1;
        Bonus updatedBonus = new Bonus();
        updatedBonus.limited(true);
        updatedBonus.value(5F);

        when(authService.checkAuth(token, "updateBonus")).thenReturn(true);
        when(bonusRepository.findById(id)).thenReturn(Optional.of(new Bonus()));

        assertThrows(IllegalArgumentException.class, () -> bonusService.updateBonus(token, id, updatedBonus));
    }

    @Test
    void deleteBonusShouldThrowSecurityExceptionWhenPermissionDoesNotExist() {
        String token = "validToken";
        Integer id = 1;

        when(authService.checkAuth(token, "deleteBonus")).thenReturn(false);

        assertThrows(SecurityException.class, () -> bonusService.deleteBonus(token, id));
    }

    @Test
    void deleteAllBonusesByProjectShouldDeleteBonusesWhenPermissionExists() {
        String token = "validToken";
        Integer projectId = 1;

        when(authService.checkAuth(token, "deleteBonus")).thenReturn(true);

        bonusService.deleteAllBonusesByProject(token, projectId);

        verify(bonusRepository, times(1)).deleteAllByProject(projectId);
    }

    @Test
    void deleteAllBonusesByProjectShouldThrowSecurityExceptionWhenPermissionDoesNotExist() {
        String token = "validToken";
        Integer projectId = 1;

        when(authService.checkAuth(token, "deleteBonus")).thenReturn(false);

        assertThrows(SecurityException.class, () -> bonusService.deleteAllBonusesByProject(token, projectId));
    }

}
