package fr.eseo.tauri.service;

import fr.eseo.tauri.model.Bonus;
import fr.eseo.tauri.model.User;
import fr.eseo.tauri.model.ValidationBonus;
import fr.eseo.tauri.repository.ValidationBonusRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ValidationBonusServiceTest {

    @Mock
    AuthService authService;

    @Mock
    ValidationBonusRepository validationBonusRepository;

    @Mock
    UserService userService;

    @Mock
    BonusService bonusService;

    @InjectMocks
    ValidationBonusService validationBonusService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getValidationBonusByAuthorIdShouldReturnBonusWhenAuthorized() {
        when(authService.checkAuth(anyString(), anyString())).thenReturn(true);
        when(validationBonusRepository.findByAuthorIdAndBonusId(anyInt(), anyInt())).thenReturn(new ValidationBonus());

        ValidationBonus result = validationBonusService.getValidationBonusByAuthorId("token", 1, 1);

        assertNotNull(result);
    }

    @Test
    void getValidationBonusByAuthorIdShouldThrowExceptionWhenNotAuthorized() {
        when(authService.checkAuth(anyString(), anyString())).thenReturn(false);

        assertThrows(SecurityException.class, () -> validationBonusService.getValidationBonusByAuthorId("token", 1, 1));
    }

    @Test
    void getAllValidationBonusesShouldReturnBonusesWhenAuthorized() {
        when(authService.checkAuth(anyString(), anyString())).thenReturn(true);
        when(validationBonusRepository.findAllByBonusId(anyInt())).thenReturn(Arrays.asList(new ValidationBonus(), new ValidationBonus()));

        List<ValidationBonus> result = validationBonusService.getAllValidationBonuses("token", 1);

        assertEquals(2, result.size());
    }

    @Test
    void getAllValidationBonusesShouldThrowExceptionWhenNotAuthorized() {
        when(authService.checkAuth(anyString(), anyString())).thenReturn(false);

        assertThrows(SecurityException.class, () -> validationBonusService.getAllValidationBonuses("token", 1));
    }

    @Test
    void getAllValidationBonusesShouldReturnEmptyListWhenNoBonuses() {
        when(authService.checkAuth(anyString(), anyString())).thenReturn(true);
        when(validationBonusRepository.findAllByBonusId(anyInt())).thenReturn(Collections.emptyList());

        List<ValidationBonus> result = validationBonusService.getAllValidationBonuses("token", 1);

        assertTrue(result.isEmpty());
    }

    @Test
    void createValidationBonusShouldSaveBonusWhenAuthorized() {
        when(authService.checkAuth(anyString(), anyString())).thenReturn(true);
        when(bonusService.getBonusById(anyString(), anyInt())).thenReturn(new Bonus());
        when(userService.getUserById(anyString(), anyInt())).thenReturn(new User());
        ValidationBonus validationBonus = new ValidationBonus();
        validationBonus.bonusId(1);
        validationBonus.authorId(1);

        validationBonusService.createValidationBonus("token", validationBonus);

        verify(validationBonusRepository, times(1)).save(any(ValidationBonus.class));
    }

    @Test
    void createValidationBonusShouldThrowExceptionWhenNotAuthorized() {
        when(authService.checkAuth(anyString(), anyString())).thenReturn(false);
        ValidationBonus validationBonus = new ValidationBonus();

        assertThrows(SecurityException.class, () -> validationBonusService.createValidationBonus("token", validationBonus));
    }

    @Test
    void deleteAllValidationBonusesShouldDeleteBonusesWhenAuthorized() {
        when(authService.checkAuth(anyString(), anyString())).thenReturn(true);

        validationBonusService.deleteAllValidationBonuses("token", 1);

        verify(validationBonusRepository, times(1)).deleteAllByBonusId(anyInt());
    }

    @Test
    void deleteAllValidationBonusesShouldThrowExceptionWhenNotAuthorized() {
        when(authService.checkAuth(anyString(), anyString())).thenReturn(false);

        assertThrows(SecurityException.class, () -> validationBonusService.deleteAllValidationBonuses("token", 1));
    }

}
