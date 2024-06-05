package fr.eseo.tauri.unit.service;

import fr.eseo.tauri.exception.ResourceNotFoundException;
import fr.eseo.tauri.model.Flag;
import fr.eseo.tauri.model.enumeration.FlagType;
import fr.eseo.tauri.repository.FlagRepository;
import fr.eseo.tauri.service.AuthService;
import fr.eseo.tauri.service.FlagService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

@Nested
class FlagServiceTest {

    @InjectMocks
    private FlagService flagService;

    @Mock
    private AuthService authService;



    @Mock
    private FlagRepository flagRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getFlagByIdShouldReturnFlagWhenAuthorized() {
        Flag flag = new Flag();

        when(flagRepository.findById(anyInt())).thenReturn(java.util.Optional.of(flag));

        Flag result = flagService.getFlagById(1);

        assertEquals(flag, result);
    }

    @Test
    void getAllFlagsByProjectShouldReturnFlagsWhenAuthorized() {
        List<Flag> flags = Collections.singletonList(new Flag());

        when(flagRepository.findAllByProject(anyInt())).thenReturn(flags);

        List<Flag> result = flagService.getAllFlagsByProject(1);

        assertEquals(flags, result);
    }

    @Test
    void updateFlagShouldUpdateFlagWhenAuthorized() {
        Flag flag = new Flag();
        Flag updatedFlag = new Flag();
        updatedFlag.description("updated description");

        when(flagRepository.findById(anyInt())).thenReturn(java.util.Optional.of(flag));

        flagService.updateFlag( 1, updatedFlag);

        verify(flagRepository, times(1)).save(any(Flag.class));
    }

    @Test
    void updateFlagShouldThrowExceptionWhenFlagNotFound() {
        Flag updatedFlag = new Flag();

        when(flagRepository.findById(anyInt())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> flagService.updateFlag(1, updatedFlag));
    }

    @Test
    void deleteFlagShouldDeleteFlagWhenAuthorized() {
        Flag flag = new Flag();

        when(flagRepository.findById(anyInt())).thenReturn(Optional.of(flag));

        flagService.deleteFlag(1);

        verify(flagRepository, times(1)).deleteById(anyInt());
    }

    @Test
    void deleteFlagShouldThrowExceptionWhenFlagNotFound() {
        when(flagRepository.findById(anyInt())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> flagService.deleteFlag(1));
    }

    @Test
    void deleteAllFlagsByProjectShouldDeleteFlagsWhenAuthorized() {
        flagService.deleteAllFlagsByProject(1);

        verify(flagRepository, times(1)).deleteAllByProject(anyInt());
    }

    @Test
    void getFlagsByAuthorAndTypeShouldReturnFlagsWhenAuthorized() {
        Integer authorId = 1;
        FlagType type = FlagType.REPORTING;

        when(flagRepository.findByAuthorIdAndType(authorId, type)).thenReturn(Collections.singletonList(new Flag()));

        List<Flag> result = flagService.getFlagsByAuthorAndType(authorId, type);

        assertFalse(result.isEmpty());
    }

    @Test
    void getFlagsByAuthorAndTypeShouldReturnEmptyListWhenNoFlagsFound() {
        Integer authorId = 1;
        FlagType type = FlagType.REPORTING;

        when(flagRepository.findByAuthorIdAndType(authorId, type)).thenReturn(Collections.emptyList());

        List<Flag> result = flagService.getFlagsByAuthorAndType(authorId, type);

        assertTrue(result.isEmpty());
    }
}
