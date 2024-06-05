package fr.eseo.tauri.unit.service;

import com.opencsv.exceptions.CsvValidationException;
import fr.eseo.tauri.exception.EmptyResourceException;
import fr.eseo.tauri.exception.ResourceNotFoundException;
import fr.eseo.tauri.model.GradeType;
import fr.eseo.tauri.model.Project;
import fr.eseo.tauri.repository.GradeTypeRepository;
import fr.eseo.tauri.service.AuthService;
import fr.eseo.tauri.service.GradeTypeService;
import fr.eseo.tauri.service.ProjectService;
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
    private ProjectService projectService;

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

        when(gradeTypeRepository.findById(anyInt())).thenReturn(Optional.of(gradeType));

        GradeType result = gradeTypeService.getGradeTypeById(1);

        assertEquals(gradeType, result);
    }

    @Test
    void getGradeTypeByIdShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() {
        when(gradeTypeRepository.findById(anyInt())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> gradeTypeService.getGradeTypeById(1));
    }

    @Test
    void getAllImportedGradeTypesShouldReturnGradeTypesWhenAuthorized() {
        List<GradeType> gradeTypes = new ArrayList<>();
        gradeTypes.add(new GradeType());

        when(gradeTypeRepository.findAllImported(1)).thenReturn(gradeTypes);

        List<GradeType> result = gradeTypeService.getAllImportedGradeTypes(1);

        assertEquals(gradeTypes, result);
    }

    @Test
    void getAllImportedGradeTypesShouldReturnEmptyListWhenNoGradeTypes() {
        when(gradeTypeRepository.findAllImported(1)).thenReturn(new ArrayList<>());

        List<GradeType> result = gradeTypeService.getAllImportedGradeTypes(1);

        assertTrue(result.isEmpty());
    }

    @Test
    void getAllUnimportedGradeTypesShouldReturnGradeTypesWhenAuthorized() {
        List<GradeType> gradeTypes = new ArrayList<>();
        gradeTypes.add(new GradeType());

        when(gradeTypeRepository.findAllUnimported(1)).thenReturn(gradeTypes);

        List<GradeType> result = gradeTypeService.getAllUnimportedGradeTypes(1);

        assertEquals(gradeTypes, result);
    }

    @Test
    void getAllUnimportedGradeTypesShouldReturnEmptyListWhenNoGradeTypes() {
        when(gradeTypeRepository.findAllUnimported(1)).thenReturn(new ArrayList<>());

        List<GradeType> result = gradeTypeService.getAllUnimportedGradeTypes(1);

        assertTrue(result.isEmpty());
    }

    @Test
    void createGradeTypeShouldSaveGradeTypeWhenAuthorized() {
        GradeType gradeType = new GradeType();

        gradeTypeService.createGradeType(gradeType);

        verify(gradeTypeRepository, times(1)).save(gradeType);
    }

    @Test
    void updateGradeTypeShouldUpdateWhenAuthorizedAndIdExists() {
        GradeType updatedGradeType = new GradeType();
        updatedGradeType.name("UpdatedName");
        GradeType existingGradeType = new GradeType();
        existingGradeType.id(1);

        when(gradeTypeRepository.findById(anyInt())).thenReturn(Optional.of(existingGradeType));

        gradeTypeService.updateGradeType(1, updatedGradeType, 1);

        assertEquals("UpdatedName", existingGradeType.name());
        verify(gradeTypeRepository, times(1)).save(existingGradeType);
    }

    @Test
    void updateGradeTypeShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() {
        GradeType updatedGradeType = new GradeType();

        when(gradeTypeRepository.findById(anyInt())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> gradeTypeService.updateGradeType(1, updatedGradeType, 1));
    }

    @Test
    void deleteGradeTypeByIdShouldDeleteWhenAuthorizedAndIdExists() {
        GradeType gradeType = new GradeType();
        gradeType.id(1);

        when(gradeTypeRepository.findById(anyInt())).thenReturn(Optional.of(gradeType));

        gradeTypeService.deleteGradeTypeById(1);

        verify(gradeTypeRepository, times(1)).deleteById(1);
    }

    @Test
    void deleteGradeTypeByIdShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() {
        when(gradeTypeRepository.findById(anyInt())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> gradeTypeService.deleteGradeTypeById(1));
    }

    @Test
    void deleteAllImportedGradeTypesShouldDeleteWhenAuthorized() {
        gradeTypeService.deleteAllImportedGradeTypes();

        verify(gradeTypeRepository, times(1)).deleteAllImported();
    }

    @Test
    void deleteAllUnimportedGradeTypesShouldDeleteWhenAuthorized() {
        gradeTypeService.deleteAllUnimportedGradeTypes();

        verify(gradeTypeRepository, times(1)).deleteAllUnimported();
    }

    @Test
    void generateImportedGradeTypesShouldCreateGradeTypesWhenAuthorized() {
        List<String> coefficients = Arrays.asList("1.0", "2.0", "3.0");
        List<String> names = Arrays.asList("Type1", "Type2", "Type3");

        List<GradeType> result = gradeTypeService.generateImportedGradeTypes(coefficients, names);

        assertEquals(4, result.size()); // 3 types + 1 average
    }

    @Test
    void generateImportedGradeTypesShouldThrowEmptyResourceExceptionWhenCoefficientsEmpty() {
        List<String> coefficients = new ArrayList<>();
        List<String> names = Arrays.asList("Type1", "Type2", "Type3");

        assertThrows(EmptyResourceException.class, () -> gradeTypeService.generateImportedGradeTypes(coefficients, names));
    }

    @Test
    void generateImportedGradeTypesShouldThrowEmptyResourceExceptionWhenNamesEmpty() {
        List<String> coefficients = Arrays.asList("1.0", "2.0", "3.0");
        List<String> names = new ArrayList<>();

        assertThrows(EmptyResourceException.class, () -> gradeTypeService.generateImportedGradeTypes(coefficients, names));
    }

    @Test
    void createImportedGradeTypeShouldReturnGradeTypeWhenAuthorized() {
        String name = "Type1";
        Float factor = 1.0f;
        Project project = new Project();

        when(projectService.getActualProject()).thenReturn(project);
        when(gradeTypeRepository.save(any(GradeType.class))).thenAnswer(i -> i.getArguments()[0]);

        GradeType result = gradeTypeService.createImportedGradeType(name, factor);

        assertEquals(name, result.name());
        assertEquals(factor, result.factor());
        assertFalse(result.forGroup());
        assertTrue(result.imported());
        assertEquals(project, result.project());
    }

    @Test
    void createGradeTypesFromCSVShouldReturnGradeTypesWhenAuthorized() throws IOException, CsvValidationException {
        InputStream inputStream = new ByteArrayInputStream("1.0,2.0,3.0\nType1,Type2,Type3".getBytes());

        List<GradeType> result = gradeTypeService.createGradeTypesFromCSV(inputStream);

        assertEquals(4, result.size()); // 3 types + 1 average
    }

    @Test
    void createGradeTypesFromCSVShouldThrowEmptyResourceExceptionWhenCoefficientsEmpty() {
        InputStream inputStream = new ByteArrayInputStream("\nType1,Type2,Type3".getBytes());

        assertThrows(EmptyResourceException.class, () -> gradeTypeService.createGradeTypesFromCSV(inputStream));
    }

    @Test
    void createGradeTypesFromCSVShouldThrowEmptyResourceExceptionWhenNamesEmpty() {
        InputStream inputStream = new ByteArrayInputStream("1.0,2.0,3.0\n".getBytes());

        assertThrows(EmptyResourceException.class, () -> gradeTypeService.createGradeTypesFromCSV(inputStream));
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
        String name = "Type1";
        Integer projectId = 1;
        GradeType gradeType = new GradeType();
        gradeType.name(name);

<<<<<<< HEAD

        when(authService.checkAuth(token, "readGradeType")).thenReturn(true);
        when(gradeTypeRepository.findByNameAndProjectId(name, projectId)).thenReturn(gradeType);

        GradeType result = gradeTypeService.findByName(name, token, projectId);
=======
        when(gradeTypeRepository.findByName(name)).thenReturn(gradeType);

        GradeType result = gradeTypeService.findByName(name);
>>>>>>> d97ed030b709c75ced5edca355ac9d4f19f13d61

        assertEquals(gradeType, result);
    }

    @Test
    void findByNameShouldReturnNullWhenNameDoesNotExist() {
        String name = "NonexistentType";
        Integer projectId = 1;
        String token = "iughi";

<<<<<<< HEAD
        when(authService.checkAuth(token, "readGradeType")).thenReturn(true);
        when(gradeTypeRepository.findByNameAndProjectId(name, projectId)).thenReturn(null);

        GradeType result = gradeTypeService.findByName(name, token, projectId);
=======
        when(gradeTypeRepository.findByName(name)).thenReturn(null);

        GradeType result = gradeTypeService.findByName(name);
>>>>>>> d97ed030b709c75ced5edca355ac9d4f19f13d61

        assertNull(result);
    }

}