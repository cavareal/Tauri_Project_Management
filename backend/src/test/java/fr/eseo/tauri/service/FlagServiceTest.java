package fr.eseo.tauri.service;

import fr.eseo.tauri.exception.ResourceNotFoundException;
import fr.eseo.tauri.model.Flag;
import fr.eseo.tauri.repository.FlagRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

class FlagServiceTest {

    private final String TEST_TOKEN = "testToken";

    @InjectMocks
    private FlagService flagService;

    @Mock
    private AuthService authService;

    @Mock
    private FlagRepository flagRepository;

    @Mock
    private UserService userService;

    @Mock
    private StudentService studentService;

    @Mock
    private ProjectService projectService;

    @Mock
    private ValidationFlagService validationFlagService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createFlagShouldSaveFlagWhenAuthorized() {
        Flag flag = new Flag();
        when(authService.checkAuth(anyString(), anyString())).thenReturn(true);

        flagService.createFlag("token", flag);

        verify(flagRepository, times(1)).save(any(Flag.class));
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

/*    @Test
    void getFlagsByAuthorAndDescriptionShouldReturnFlagsWhenAuthorized() {
        List<Flag> flags = Collections.singletonList(new Flag());
        when(authService.checkAuth(anyString(), anyString())).thenReturn(true);
        when(flagRepository.findByAuthorIdAndDescription(anyInt(), anyString())).thenReturn(flags);

        List<Flag> result = flagService.getFlagsByAuthorAndDescription("token", 1, "description");

        assertEquals(flags, result);
    }*/

    /*
    @Test
    void getFlagsByAuthorAndDescriptionShouldThrowExceptionWhenNotAuthorized() {
        when(authService.checkAuth(anyString(), anyString())).thenReturn(false);

        assertThrows(SecurityException.class, () -> flagService.getFlagsByAuthorAndDescription("token", 1, "description"));
    }
*/

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
}
