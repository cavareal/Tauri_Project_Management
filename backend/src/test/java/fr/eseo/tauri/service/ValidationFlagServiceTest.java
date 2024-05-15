package fr.eseo.tauri.service;

import fr.eseo.tauri.model.*;
import fr.eseo.tauri.model.enumeration.RoleType;
import fr.eseo.tauri.repository.ValidationFlagRepository;
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

class ValidationFlagServiceTest {

    @Mock
    AuthService authService;

    @Mock
    ValidationFlagRepository validationFlagRepository;

    @Mock
    RoleService roleService;

    @InjectMocks
    ValidationFlagService validationFlagService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getValidationFlagByAuthorIdShouldReturnFlagWhenAuthorized() {
        when(authService.checkAuth(anyString(), anyString())).thenReturn(true);
        when(validationFlagRepository.findByAuthorIdAndFlagId(anyInt(), anyInt())).thenReturn(new ValidationFlag());

        ValidationFlag result = validationFlagService.getValidationFlagByAuthorId("token", 1, 1);

        assertNotNull(result);
    }

    @Test
    void getValidationFlagByAuthorIdShouldThrowExceptionWhenNotAuthorized() {
        when(authService.checkAuth(anyString(), anyString())).thenReturn(false);

        assertThrows(SecurityException.class, () -> validationFlagService.getValidationFlagByAuthorId("token", 1, 1));
    }

    @Test
    void getAllValidationFlagsShouldReturnFlagsWhenAuthorized() {
        when(authService.checkAuth(anyString(), anyString())).thenReturn(true);
        when(validationFlagRepository.findAllByFlag(anyInt())).thenReturn(Arrays.asList(new ValidationFlag(), new ValidationFlag()));

        List<ValidationFlag> result = validationFlagService.getAllValidationFlags("token", 1);

        assertEquals(2, result.size());
    }

    @Test
    void getAllValidationFlagsShouldThrowExceptionWhenNotAuthorized() {
        when(authService.checkAuth(anyString(), anyString())).thenReturn(false);

        assertThrows(SecurityException.class, () -> validationFlagService.getAllValidationFlags("token", 1));
    }

    @Test
    void getAllValidationFlagsShouldReturnEmptyListWhenNoFlags() {
        when(authService.checkAuth(anyString(), anyString())).thenReturn(true);
        when(validationFlagRepository.findAllByFlag(anyInt())).thenReturn(Collections.emptyList());

        List<ValidationFlag> result = validationFlagService.getAllValidationFlags("token", 1);

        assertTrue(result.isEmpty());
    }

    @Test
    void createValidationFlagsShouldCreateFlagsWhenAuthorizedAndRoleIsProjectLeader() {
        when(authService.checkAuth(anyString(), anyString())).thenReturn(true);
        when(roleService.getUsersByRoleType(anyString(), any(RoleType.class))).thenReturn(Collections.singletonList(new User()));
        Flag flag = new Flag();
        flag.author(new User());

        validationFlagService.createValidationFlags("token", flag);

        verify(validationFlagRepository, times(1)).save(any(ValidationFlag.class));
    }

    @Test
    void createValidationFlagsShouldThrowExceptionWhenNotAuthorized() {
        when(authService.checkAuth(anyString(), anyString())).thenReturn(false);
        Flag flag = new Flag();

        assertThrows(SecurityException.class, () -> validationFlagService.createValidationFlags("token", flag));
    }
}
