package fr.eseo.tauri.unit.service;

import com.opencsv.exceptions.CsvValidationException;
import fr.eseo.tauri.exception.EmptyResourceException;
import fr.eseo.tauri.exception.ResourceNotFoundException;
import fr.eseo.tauri.model.GradeType;
import fr.eseo.tauri.repository.GradeTypeRepository;
import fr.eseo.tauri.service.AuthService;
import fr.eseo.tauri.service.GradeTypeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.ByteArrayInputStream;
import java.io.IOException;
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

    @Test
    void deleteAllUnimportedGradeTypesShouldDeleteWhenAuthorized() {
        String token = "validToken";

        when(authService.checkAuth(token, "deleteGradeType")).thenReturn(true);

        gradeTypeService.deleteAllUnimportedGradeTypes(token);

        verify(gradeTypeRepository, times(1)).deleteAllUnimported();
    }

    @Test
    void deleteAllUnimportedGradeTypesShouldThrowSecurityExceptionWhenUnauthorized() {
        String token = "validToken";

        when(authService.checkAuth(token, "deleteGradeType")).thenReturn(false);

        assertThrows(SecurityException.class, () -> gradeTypeService.deleteAllUnimportedGradeTypes(token));
    }

    @Test
    void generateImportedGradeTypesShouldCreateGradeTypesWhenAuthorized() {
        String token = "validToken";
        List<String> coefficients = Arrays.asList("1.0", "2.0", "3.0");
        List<String> names = Arrays.asList("Type1", "Type2", "Type3");

        when(authService.checkAuth(token, "addGradeType")).thenReturn(true);

        List<GradeType> result = gradeTypeService.generateImportedGradeTypes(token, coefficients, names);

        assertEquals(4, result.size()); // 3 types + 1 average
    }

    @Test
    void generateImportedGradeTypesShouldThrowSecurityExceptionWhenUnauthorized() {
        String token = "validToken";
        List<String> coefficients = Arrays.asList("1.0", "2.0", "3.0");
        List<String> names = Arrays.asList("Type1", "Type2", "Type3");

        when(authService.checkAuth(token, "addGradeType")).thenReturn(false);

        assertThrows(SecurityException.class, () -> gradeTypeService.generateImportedGradeTypes(token, coefficients, names));
    }

    @Test
    void generateImportedGradeTypesShouldThrowEmptyResourceExceptionWhenCoefficientsEmpty() {
        String token = "validToken";
        List<String> coefficients = new ArrayList<>();
        List<String> names = Arrays.asList("Type1", "Type2", "Type3");

        when(authService.checkAuth(token, "addGradeType")).thenReturn(true);

        assertThrows(EmptyResourceException.class, () -> gradeTypeService.generateImportedGradeTypes(token, coefficients, names));
    }

    @Test
    void generateImportedGradeTypesShouldThrowEmptyResourceExceptionWhenNamesEmpty() {
        String token = "validToken";
        List<String> coefficients = Arrays.asList("1.0", "2.0", "3.0");
        List<String> names = new ArrayList<>();

        when(authService.checkAuth(token, "addGradeType")).thenReturn(true);

        assertThrows(EmptyResourceException.class, () -> gradeTypeService.generateImportedGradeTypes(token, coefficients, names));
    }

    @Test
    void createImportedGradeTypeShouldReturnGradeTypeWhenAuthorized() {
        String token = "validToken";
        String name = "Type1";
        Float factor = 1.0f;

        when(authService.checkAuth(token, "addGradeType")).thenReturn(true);
        when(gradeTypeRepository.save(any(GradeType.class))).thenAnswer(i -> i.getArguments()[0]);

        GradeType result = gradeTypeService.createImportedGradeType(name, factor);

        assertEquals(name, result.name());
        assertEquals(factor, result.factor());
        assertFalse(result.forGroup());
        assertTrue(result.imported());
    }

    @Test
    void createGradeTypesFromCSVShouldReturnGradeTypesWhenAuthorized() throws IOException, CsvValidationException {
        String token = "validToken";
        InputStream inputStream = new ByteArrayInputStream("1.0,2.0,3.0\nType1,Type2,Type3".getBytes());

        when(authService.checkAuth(token, "addGradeType")).thenReturn(true);

        List<GradeType> result = gradeTypeService.createGradeTypesFromCSV(token, inputStream);

        assertEquals(4, result.size()); // 3 types + 1 average
    }

    @Test
    void createGradeTypesFromCSVShouldThrowSecurityExceptionWhenUnauthorized(){
        String token = "validToken";
        InputStream inputStream = new ByteArrayInputStream("1.0,2.0,3.0\nType1,Type2,Type3".getBytes());

        when(authService.checkAuth(token, "addGradeType")).thenReturn(false);

        assertThrows(SecurityException.class, () -> gradeTypeService.createGradeTypesFromCSV(token, inputStream));
    }

    @Test
    void createGradeTypesFromCSVShouldThrowEmptyResourceExceptionWhenCoefficientsEmpty() {
        String token = "validToken";
        InputStream inputStream = new ByteArrayInputStream("\nType1,Type2,Type3".getBytes());

        when(authService.checkAuth(token, "addGradeType")).thenReturn(true);

        assertThrows(EmptyResourceException.class, () -> gradeTypeService.createGradeTypesFromCSV(token, inputStream));
    }

    @Test
    void createGradeTypesFromCSVShouldThrowEmptyResourceExceptionWhenNamesEmpty() {
        String token = "validToken";
        InputStream inputStream = new ByteArrayInputStream("1.0,2.0,3.0\n".getBytes());

        when(authService.checkAuth(token, "addGradeType")).thenReturn(true);

        assertThrows(EmptyResourceException.class, () -> gradeTypeService.createGradeTypesFromCSV(token, inputStream));
    }

    @Test
    void processLineForCoefficientsShouldReturnCorrectStartingIndexAndFillCoefficients() {
        String[] nextLine = {"Not a number", "2.0", "3.0", "4.0"};
        List<String> coefficients = new ArrayList<>();

        int result = gradeTypeService.processLineForCoefficients(nextLine, coefficients);

        assertEquals(2, result);
        assertEquals(Arrays.asList("2.0", "3.0", "4.0"), coefficients);
    }

    @Test
    void processLineForCoefficientsShouldReturnOneWhenAllAreCoefficients() {
        String[] nextLine = {"1.0", "2.0", "3.0"};
        List<String> coefficients = new ArrayList<>();

        int result = gradeTypeService.processLineForCoefficients(nextLine, coefficients);

        assertEquals(1, result);
        assertEquals(Arrays.asList("1.0", "2.0", "3.0"), coefficients);
    }

    @Test
    void processLineForCoefficientsShouldReturnLengthPlusOneWhenNoCoefficients() {
        String[] nextLine = {"Not a number", "Also not a number", "Still not a number"};
        List<String> coefficients = new ArrayList<>();

        int result = gradeTypeService.processLineForCoefficients(nextLine, coefficients);

        assertEquals(4, result);
        assertTrue(coefficients.isEmpty());
    }

    @Test
    void processLineForCoefficientsShouldHandleEmptyLine() {
        String[] nextLine = {};
        List<String> coefficients = new ArrayList<>();

        int result = gradeTypeService.processLineForCoefficients(nextLine, coefficients);

        assertEquals(1, result);
        assertTrue(coefficients.isEmpty());
    }

    @Test
    void processLineForNamesShouldAddNamesWhenIndexGreaterThanOrEqualToStartingCoefficients() {
        String[] nextLine = {"Not a name", "Name1", "Name2", "Name3"};
        List<String> names = new ArrayList<>();
        int startingCoefficients = 2;

        gradeTypeService.processLineForNames(nextLine, names, startingCoefficients);

        assertEquals(Arrays.asList("Name1", "Name2", "Name3"), names);
    }

    @Test
    void processLineForNamesShouldNotAddNamesWhenIndexLessThanStartingCoefficients() {
        String[] nextLine = {"Name1", "Name2", "Name3"};
        List<String> names = new ArrayList<>();
        int startingCoefficients = 4;

        gradeTypeService.processLineForNames(nextLine, names, startingCoefficients);

        assertTrue(names.isEmpty());
    }

    @Test
    void processLineForNamesShouldHandleEmptyLine() {
        String[] nextLine = {};
        List<String> names = new ArrayList<>();
        int startingCoefficients = 1;

        gradeTypeService.processLineForNames(nextLine, names, startingCoefficients);

        assertTrue(names.isEmpty());
    }

    @Test
    void findByNameShouldReturnGradeTypeWhenAuthorizedAndNameExists() {
        String token = "validToken";
        String name = "Type1";
        Integer projectId = 1;
        GradeType gradeType = new GradeType();
        gradeType.name(name);


        when(authService.checkAuth(token, "readGradeType")).thenReturn(true);
        when(gradeTypeRepository.findByNameAndProjectId(name, projectId)).thenReturn(gradeType);

        GradeType result = gradeTypeService.findByName(name, token, projectId);

        assertEquals(gradeType, result);
    }

    @Test
    void findByNameShouldThrowSecurityExceptionWhenUnauthorized() {
        String token = "validToken";
        String name = "Type1";

        when(authService.checkAuth(token, "readGradeType")).thenReturn(false);

        assertThrows(SecurityException.class, () -> gradeTypeService.findByName(name, token));
    }

    @Test
    void findByNameShouldReturnNullWhenNameDoesNotExist() {
        String token = "validToken";
        String name = "NonexistentType";
        Integer projectId = 1;

        when(authService.checkAuth(token, "readGradeType")).thenReturn(true);
        when(gradeTypeRepository.findByNameAndProjectId(name, projectId)).thenReturn(null);

        GradeType result = gradeTypeService.findByName(name, token, projectId);

        assertNull(result);
    }

}