package fr.eseo.tauri.service;

import fr.eseo.tauri.exception.ResourceNotFoundException;
import fr.eseo.tauri.model.Flag;
import fr.eseo.tauri.model.enumeration.FlagType;
import fr.eseo.tauri.repository.FlagRepository;
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

    private final String TEST_TOKEN = "testToken";

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
    void createFlagShouldThrowExceptionWhenNotAuthorized() {
        Flag flag = new Flag();
        when(authService.checkAuth(anyString(), anyString())).thenReturn(false);

        assertThrows(SecurityException.class, () -> flagService.createFlag("token", flag));
    }

    @Test
    void getFlagByIdShouldReturnFlagWhenAuthorized() {
        Flag flag = new Flag();
        when(authService.checkAuth(anyString(), anyString())).thenReturn(true);
        when(flagRepository.findById(anyInt())).thenReturn(java.util.Optional.of(flag));

        Flag result = flagService.getFlagById("token", 1);

        assertEquals(flag, result);
    }

    @Test
    void getFlagByIdShouldThrowExceptionWhenNotAuthorized() {
        when(authService.checkAuth(anyString(), anyString())).thenReturn(false);

        assertThrows(SecurityException.class, () -> flagService.getFlagById("token", 1));
    }

    @Test
    void getAllFlagsByProjectShouldReturnFlagsWhenAuthorized() {
        List<Flag> flags = Collections.singletonList(new Flag());
        when(authService.checkAuth(anyString(), anyString())).thenReturn(true);
        when(flagRepository.findAllByProject(anyInt())).thenReturn(flags);

        List<Flag> result = flagService.getAllFlagsByProject("token", 1);

        assertEquals(flags, result);
    }

    @Test
    void getAllFlagsByProjectShouldThrowExceptionWhenNotAuthorized() {
        when(authService.checkAuth(anyString(), anyString())).thenReturn(false);

        assertThrows(SecurityException.class, () -> flagService.getAllFlagsByProject("token", 1));
    }

    @Test
    void updateFlagShouldUpdateFlagWhenAuthorized() {
        Flag flag = new Flag();
        Flag updatedFlag = new Flag();
        updatedFlag.description("updated description");
        when(authService.checkAuth(anyString(), anyString())).thenReturn(true);
        when(flagRepository.findById(anyInt())).thenReturn(java.util.Optional.of(flag));

        flagService.updateFlag(TEST_TOKEN, 1, updatedFlag);

        verify(flagRepository, times(1)).save(any(Flag.class));
    }

    @Test
    void updateFlagShouldThrowExceptionWhenNotAuthorized() {
        Flag updatedFlag = new Flag();
        when(authService.checkAuth(anyString(), anyString())).thenReturn(false);

        assertThrows(SecurityException.class, () -> flagService.updateFlag(TEST_TOKEN, 1, updatedFlag));
    }

    @Test
    void updateFlagShouldThrowExceptionWhenFlagNotFound() {
        Flag updatedFlag = new Flag();
        when(authService.checkAuth(anyString(), anyString())).thenReturn(true);
        when(flagRepository.findById(anyInt())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> flagService.updateFlag(TEST_TOKEN, 1, updatedFlag));
    }

    @Test
    void deleteFlagShouldDeleteFlagWhenAuthorized() {
        Flag flag = new Flag();
        when(authService.checkAuth(anyString(), anyString())).thenReturn(true);
        when(flagRepository.findById(anyInt())).thenReturn(Optional.of(flag));

        flagService.deleteFlag(TEST_TOKEN, 1);

        verify(flagRepository, times(1)).deleteById(anyInt());
    }

    @Test
    void deleteFlagShouldThrowExceptionWhenNotAuthorized() {
        when(authService.checkAuth(anyString(), anyString())).thenReturn(false);

        assertThrows(SecurityException.class, () -> flagService.deleteFlag(TEST_TOKEN, 1));
    }

    @Test
    void deleteFlagShouldThrowExceptionWhenFlagNotFound() {
        when(authService.checkAuth(anyString(), anyString())).thenReturn(true);
        when(flagRepository.findById(anyInt())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> flagService.deleteFlag(TEST_TOKEN, 1));
    }

    @Test
    void deleteAllFlagsByProjectShouldDeleteFlagsWhenAuthorized() {
        when(authService.checkAuth(anyString(), anyString())).thenReturn(true);

        flagService.deleteAllFlagsByProject(TEST_TOKEN, 1);

        verify(flagRepository, times(1)).deleteAllByProject(anyInt());
    }

    @Test
    void deleteAllFlagsByProjectShouldThrowExceptionWhenNotAuthorized() {
        when(authService.checkAuth(anyString(), anyString())).thenReturn(false);

        assertThrows(SecurityException.class, () -> flagService.deleteAllFlagsByProject(TEST_TOKEN, 1));
    }

    @Test
    void getFlagsByAuthorAndTypeShouldReturnFlagsWhenAuthorized() {
        String token = "validToken";
        Integer authorId = 1;
        FlagType type = FlagType.REPORTING;

        when(authService.checkAuth(token, "readFlags")).thenReturn(true);
        when(flagRepository.findByAuthorIdAndType(authorId, type)).thenReturn(Collections.singletonList(new Flag()));

        List<Flag> result = flagService.getFlagsByAuthorAndType(token, authorId, type);

        assertFalse(result.isEmpty());
    }

    @Test
    void getFlagsByAuthorAndTypeShouldThrowSecurityExceptionWhenNotAuthorized() {
        String token = "validToken";
        Integer authorId = 1;
        FlagType type = FlagType.REPORTING;

        when(authService.checkAuth(token, "readFlags")).thenReturn(false);

        assertThrows(SecurityException.class, () -> flagService.getFlagsByAuthorAndType(token, authorId, type));
    }

    @Test
    void getFlagsByAuthorAndTypeShouldReturnEmptyListWhenNoFlagsFound() {
        String token = "validToken";
        Integer authorId = 1;
        FlagType type = FlagType.REPORTING;

        when(authService.checkAuth(token, "readFlags")).thenReturn(true);
        when(flagRepository.findByAuthorIdAndType(authorId, type)).thenReturn(Collections.emptyList());

        List<Flag> result = flagService.getFlagsByAuthorAndType(token, authorId, type);

        assertTrue(result.isEmpty());
    }
}
