package fr.eseo.tauri.unit.service;

import fr.eseo.tauri.model.*;
import fr.eseo.tauri.repository.ValidationFlagRepository;
import fr.eseo.tauri.service.AuthService;
import fr.eseo.tauri.service.ValidationFlagService;
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

    @InjectMocks
    ValidationFlagService validationFlagService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getValidationFlagByAuthorIdShouldReturnFlagWhenAuthorized() {
        when(validationFlagRepository.findByAuthorIdAndFlagId(anyInt(), anyInt())).thenReturn(new ValidationFlag());

        ValidationFlag result = validationFlagService.getValidationFlagByAuthorId(1, 1);

        assertNotNull(result);
    }

    @Test
    void getAllValidationFlagsShouldReturnFlagsWhenAuthorized() {
        when(validationFlagRepository.findAllByFlag(anyInt())).thenReturn(Arrays.asList(new ValidationFlag(), new ValidationFlag()));

        List<ValidationFlag> result = validationFlagService.getAllValidationFlags( 1);

        assertEquals(2, result.size());
    }

    @Test
    void getAllValidationFlagsShouldReturnEmptyListWhenNoFlags() {
        when(validationFlagRepository.findAllByFlag(anyInt())).thenReturn(Collections.emptyList());

        List<ValidationFlag> result = validationFlagService.getAllValidationFlags(1);

        assertTrue(result.isEmpty());
    }

    @Test
    void updateValidationFlagShouldUpdateFlagWhenAuthorizedAndConfirmedIsNotNull() {
        Integer flagId = 1;
        Integer authorId = 1;
        ValidationFlag updatedValidationFlag = new ValidationFlag();
        updatedValidationFlag.confirmed(true);

        when(validationFlagService.getValidationFlagByAuthorId(flagId, authorId)).thenReturn(new ValidationFlag());

        validationFlagService.updateValidationFlag(flagId, authorId, updatedValidationFlag);

        verify(validationFlagRepository, times(1)).save(any(ValidationFlag.class));
    }
}
