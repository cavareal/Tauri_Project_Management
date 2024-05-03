package fr.eseo.tauri.service;

import fr.eseo.tauri.model.Flag;
import fr.eseo.tauri.repository.FlagRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

class FlagServiceTest {

    @InjectMocks
    private FlagService flagService;

    @Mock
    private FlagRepository flagRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAddFlag() {
        Flag flag = new Flag();
        when(flagRepository.save(any(Flag.class))).thenReturn(flag);

        Flag result = flagService.addFlag(flag);

        assertEquals(flag, result);
        verify(flagRepository, times(1)).save(any(Flag.class));
    }

    @Test
    void testGetFlagsByAuthorAndDescription() {
        Flag flag = new Flag();
        List<Flag> flags = List.of(flag);
        when(flagRepository.findByAuthorIdAndDescription(anyInt(), anyString())).thenReturn(flags);

        List<Flag> result = flagService.getFlagsByAuthorAndDescription(1, "description");

        assertEquals(flags, result);
        verify(flagRepository, times(1)).findByAuthorIdAndDescription(anyInt(), anyString());
    }
}
