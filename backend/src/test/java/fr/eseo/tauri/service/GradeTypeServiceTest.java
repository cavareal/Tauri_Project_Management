package fr.eseo.tauri.service;

import fr.eseo.tauri.exception.ResourceNotFoundException;
import fr.eseo.tauri.model.GradeType;
import fr.eseo.tauri.model.enumeration.GradeTypeName;
import fr.eseo.tauri.repository.GradeTypeRepository;
import fr.eseo.tauri.util.CustomLogger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@Nested
class GradeTypeServiceTest {

    @InjectMocks
    private GradeTypeService gradeTypeService;

    @Mock
    private AuthService authService;

    @Mock
    private GradeTypeRepository gradeTypeRepository;

    @Mock
    private GradeService gradeService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getGradeTypeByIdShouldReturnGradeTypeWhenIdExists() {
        GradeType gradeType = new GradeType();
        gradeType.id(1);
        when(authService.checkAuth(anyString(), anyString())).thenReturn(true);
        when(gradeTypeRepository.findById(anyInt())).thenReturn(Optional.of(gradeType));

        GradeType result = gradeTypeService.getGradeTypeById("token", 1);

        assertEquals(gradeType, result);
    }

    @Test
    void getGradeTypeByIdShouldThrowSecurityExceptionWhenUnauthorized() {
        when(authService.checkAuth(anyString(), anyString())).thenReturn(false);

        assertThrows(SecurityException.class, () -> gradeTypeService.getGradeTypeById("token", 1));
    }

    @Test
    void getGradeTypeByIdShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() {
        when(authService.checkAuth(anyString(), anyString())).thenReturn(true);
        when(gradeTypeRepository.findById(anyInt())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> gradeTypeService.getGradeTypeById("token", 1));
    }

    @Test
    void getAllImportedGradeTypesShouldReturnGradeTypesWhenAuthorized() {
        List<GradeType> gradeTypes = new ArrayList<>();
        gradeTypes.add(new GradeType());
        when(authService.checkAuth(anyString(), anyString())).thenReturn(true);
        when(gradeTypeRepository.findAllImported()).thenReturn(gradeTypes);

        List<GradeType> result = gradeTypeService.getAllImportedGradeTypes("token");

        assertEquals(gradeTypes, result);
    }

    @Test
    void getAllImportedGradeTypesShouldThrowSecurityExceptionWhenUnauthorized() {
        when(authService.checkAuth(anyString(), anyString())).thenReturn(false);

        assertThrows(SecurityException.class, () -> gradeTypeService.getAllImportedGradeTypes("token"));
    }

    @Test
    void getAllImportedGradeTypesShouldReturnEmptyListWhenNoGradeTypes() {
        when(authService.checkAuth(anyString(), anyString())).thenReturn(true);
        when(gradeTypeRepository.findAllImported()).thenReturn(new ArrayList<>());

        List<GradeType> result = gradeTypeService.getAllImportedGradeTypes("token");

        assertTrue(result.isEmpty());
    }

    @Test
    void getAllUnimportedGradeTypesShouldReturnGradeTypesWhenAuthorized() {
        List<GradeType> gradeTypes = new ArrayList<>();
        gradeTypes.add(new GradeType());
        when(authService.checkAuth(anyString(), anyString())).thenReturn(true);
        when(gradeTypeRepository.findAllUnimported()).thenReturn(gradeTypes);

        List<GradeType> result = gradeTypeService.getAllUnimportedGradeTypes("token");

        assertEquals(gradeTypes, result);
    }

    @Test
    void getAllUnimportedGradeTypesShouldThrowSecurityExceptionWhenUnauthorized() {
        when(authService.checkAuth(anyString(), anyString())).thenReturn(false);

        assertThrows(SecurityException.class, () -> gradeTypeService.getAllUnimportedGradeTypes("token"));
    }

    @Test
    void getAllUnimportedGradeTypesShouldReturnEmptyListWhenNoGradeTypes() {
        when(authService.checkAuth(anyString(), anyString())).thenReturn(true);
        when(gradeTypeRepository.findAllUnimported()).thenReturn(new ArrayList<>());

        List<GradeType> result = gradeTypeService.getAllUnimportedGradeTypes("token");

        assertTrue(result.isEmpty());
    }

    @Test
    void createGradeTypeShouldSaveGradeTypeWhenAuthorized() {
        GradeType gradeType = new GradeType();
        when(authService.checkAuth(anyString(), anyString())).thenReturn(true);

        gradeTypeService.createGradeType("token", gradeType);

        verify(gradeTypeRepository, times(1)).save(gradeType);
    }

    @Test
    void createGradeTypeShouldThrowSecurityExceptionWhenUnauthorized() {
        GradeType gradeType = new GradeType();
        when(authService.checkAuth(anyString(), anyString())).thenReturn(false);

        assertThrows(SecurityException.class, () -> gradeTypeService.createGradeType("token", gradeType));
    }

    @Test
    void updateGradeTypeShouldUpdateWhenAuthorizedAndIdExists() {
        GradeType updatedGradeType = new GradeType();
        updatedGradeType.name("UpdatedName");
        GradeType existingGradeType = new GradeType();
        existingGradeType.id(1);
        when(authService.checkAuth(anyString(), anyString())).thenReturn(true);
        when(gradeTypeRepository.findById(anyInt())).thenReturn(Optional.of(existingGradeType));

        gradeTypeService.updateGradeType("token", 1, updatedGradeType);

        assertEquals("UpdatedName", existingGradeType.name());
        verify(gradeTypeRepository, times(1)).save(existingGradeType);
    }

    @Test
    void updateGradeTypeShouldThrowSecurityExceptionWhenUnauthorized() {
        GradeType updatedGradeType = new GradeType();
        when(authService.checkAuth(anyString(), anyString())).thenReturn(false);

        assertThrows(SecurityException.class, () -> gradeTypeService.updateGradeType("token", 1, updatedGradeType));
    }

    @Test
    void updateGradeTypeShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() {
        GradeType updatedGradeType = new GradeType();
        when(authService.checkAuth(anyString(), anyString())).thenReturn(true);
        when(gradeTypeRepository.findById(anyInt())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> gradeTypeService.updateGradeType("token", 1, updatedGradeType));
    }

    @Test
    void deleteGradeTypeByIdShouldDeleteWhenAuthorizedAndIdExists() {
        GradeType gradeType = new GradeType();
        gradeType.id(1);
        when(authService.checkAuth(anyString(), anyString())).thenReturn(true);
        when(gradeTypeRepository.findById(anyInt())).thenReturn(Optional.of(gradeType));

        gradeTypeService.deleteGradeTypeById("token", 1);

        verify(gradeTypeRepository, times(1)).deleteById(1);
    }

    @Test
    void deleteGradeTypeByIdShouldThrowSecurityExceptionWhenUnauthorized() {
        when(authService.checkAuth(anyString(), anyString())).thenReturn(false);

        assertThrows(SecurityException.class, () -> gradeTypeService.deleteGradeTypeById("token", 1));
    }

    @Test
    void deleteGradeTypeByIdShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() {
        when(authService.checkAuth(anyString(), anyString())).thenReturn(true);
        when(gradeTypeRepository.findById(anyInt())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> gradeTypeService.deleteGradeTypeById("token", 1));
    }

    @Test
    void deleteAllImportedGradeTypesShouldDeleteWhenAuthorized() {
        when(authService.checkAuth(anyString(), anyString())).thenReturn(true);

        gradeTypeService.deleteAllImportedGradeTypes("token");

        verify(gradeTypeRepository, times(1)).deleteAllImported();
    }

    @Test
    void deleteAllImportedGradeTypesShouldThrowSecurityExceptionWhenUnauthorized() {
        when(authService.checkAuth(anyString(), anyString())).thenReturn(false);

        assertThrows(SecurityException.class, () -> gradeTypeService.deleteAllImportedGradeTypes("token"));
    }


    /**
    @Test
    void createGradeType_withValidInput_savesGradeType() {
        GradeType gradeType = new GradeType();
        gradeType.name( GradeTypeName.DEFAULT.toString());
        gradeTypeService.createGradeType(gradeType);
        verify(gradeTypeRepository, times(1)).save(gradeType);
    }

    @Test
    void createGradeType_withNullName_throwsException() {
        GradeType gradeType = new GradeType();
        gradeType.name(null);
        assertThrows(IllegalArgumentException.class, () -> gradeTypeService.createGradeType(gradeType));
    }

    @Test
    void createGradeTypesFromCSV_withValidInput_createsGradeTypes() {
        String csvContent = """
                "","","","","","2","3","3","2","2","1","1"
                "","","sexe
                M / F","","","PADL","PDLO","PWND","IRS","STAGE S7","S5","S6"
                "1","ABADIE Cyril","M","B","  12.52 ","","","","","","",""
                "2","ALARD Sébastien","M","","  13.72 ","  15.38 ","  10.97 ","  11.50 ","  18.57 ","  14.50 ","  13.19 ","  14.51 \"""";
        InputStream inputStream = new ByteArrayInputStream(csvContent.getBytes());

        List<GradeType> gradeTypes = gradeTypeService.createGradeTypesFromCSV(inputStream);

        assertEquals(7+1, gradeTypes.size()); // 7 columns + 1 for the average that is added by the service
        verify(gradeTypeRepository, times(7+1)).save(any(GradeType.class));
    }

    @Test
    void createGradeTypesFromCSV_withInvalidInput_logsError() {
        String csvContent = "Invalid content";
        InputStream inputStream = new ByteArrayInputStream(csvContent.getBytes());

        List<GradeType> gradeTypes = gradeTypeService.createGradeTypesFromCSV(inputStream);

        assertTrue(gradeTypes.isEmpty());
        verify(gradeTypeRepository, never()).save(any(GradeType.class));
    }

    @Test
    void createGradeTypesFromCSV_withEmptyInput_returnsEmptyList() {
        String csvContent = "";
        InputStream inputStream = new ByteArrayInputStream(csvContent.getBytes());

        List<GradeType> gradeTypes = gradeTypeService.createGradeTypesFromCSV(inputStream);
        CustomLogger.info("size" + gradeTypes);
        assertTrue(gradeTypes.isEmpty());
        verify(gradeTypeRepository, never()).save(any(GradeType.class));
    }

    @Test
    void createGradeTypesFromCSV_withSummaryData_ignoresSummaryData() {
        String csvContent = """
                "","","","","","2","3","3","2","2","1","1"
                "","","sexe
                M / F","","","PADL","PDLO","PWND","IRS","STAGE S7","S5","S6"
                "1","ABADIE Cyril","M","B","  12.52 ","","","","","","",""
                "2","ALARD Sébastien","M","","  13.72 ","  15.38 ","  10.97 ","  11.50 ","  18.57 ","  14.50 ","  13.19 ","  14.51 "
                "","Nombre F","6","","","","","","","","",""
                "","Nombre H","42","","","","","","","","",""
                "","Nombre B","","6","","","","","","","","\"""";
        InputStream inputStream = new ByteArrayInputStream(csvContent.getBytes());

        List<GradeType> gradeTypes = gradeTypeService.createGradeTypesFromCSV(inputStream);

        assertEquals(7+1, gradeTypes.size()); // 7 columns + 1 for the average that is added by the service
        verify(gradeTypeRepository, times(7+1)).save(any(GradeType.class));
    }

    @Test
    void deleteAllImportedGradeTypes_deletesAllImportedGradeTypes() {
        doNothing().when(gradeTypeRepository).deleteAllImported();

        gradeTypeService.deleteAllImportedGradeTypes();

        verify(gradeTypeRepository, times(1)).deleteAllImported();
    }

    @Test
    void deleteAllImportedGradeTypes_handlesException() {
        doThrow(new RuntimeException()).when(gradeTypeRepository).deleteAllImported();

        gradeTypeService.deleteAllImportedGradeTypes();

        verify(gradeTypeRepository, times(1)).deleteAllImported();
    }

    @Test
    @DisplayName("getImportedGradeTypes returns all imported grade types")
    void getImportedGradeTypes_returnsAllImportedGradeTypes() {
        GradeType gradeType1 = new GradeType();
        gradeType1.name("GradeType1");
        gradeType1.imported(true);

        GradeType gradeType2 = new GradeType();
        gradeType2.name("GradeType2");
        gradeType2.imported(true);

        when(gradeTypeRepository.findAllImported()).thenReturn(Arrays.asList(gradeType1, gradeType2));

        List<GradeType> actualGradeTypes = gradeTypeService.getImportedGradeTypes();

        assertEquals(2, actualGradeTypes.size());
        assertTrue(actualGradeTypes.contains(gradeType1));
        assertTrue(actualGradeTypes.contains(gradeType2));
    }

    @Test
    @DisplayName("getImportedGradeTypes returns empty list when no imported grade types exist")
    void getImportedGradeTypes_returnsEmptyListWhenNoImportedGradeTypesExist() {
        when(gradeTypeRepository.findAllImported()).thenReturn(Collections.emptyList());

        List<GradeType> actualGradeTypes = gradeTypeService.getImportedGradeTypes();

        assertTrue(actualGradeTypes.isEmpty());
    }

    @Test
    @DisplayName("getImportedGradeTypes handles exceptions gracefully")
    void getImportedGradeTypes_handlesExceptionsGracefully() {
        when(gradeTypeRepository.findAllImported()).thenThrow(new RuntimeException());

        assertThrows(RuntimeException.class, () -> gradeTypeService.getImportedGradeTypes());
    }

    @Test
    @DisplayName("updateFactor handles exceptions gracefully")
    void updateFactor_handlesExceptionsGracefully() {
        GradeType gradeType = new GradeType();
        gradeType.id(1);
        gradeType.factor(1.0f);

        when(gradeTypeRepository.findById(1)).thenReturn(Optional.of(gradeType));
        doThrow(new RuntimeException()).when(gradeTypeRepository).save(gradeType);

        assertThrows(RuntimeException.class, () -> gradeTypeService.updateFactor(1, 2.0f));
    }*/

}