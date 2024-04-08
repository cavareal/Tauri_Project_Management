package fr.eseo.tauri.service;

import fr.eseo.tauri.model.GradeType;
import fr.eseo.tauri.model.enumeration.GradeTypeName;
import fr.eseo.tauri.repository.GradeTypeRepository;
import fr.eseo.tauri.util.CustomLogger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GradeTypeServiceTest {

    @Mock
    private GradeTypeRepository gradeTypeRepository;

    private GradeTypeService gradeTypeService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        gradeTypeService = new GradeTypeService(gradeTypeRepository, null);
    }

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
        String csvContent = "\"\",\"\",\"\",\"\",\"\",\"2\",\"3\",\"3\",\"2\",\"2\",\"1\",\"1\"\n" +
                "\"\",\"\",\"sexe\n" +
                "M / F\",\"\",\"\",\"PADL\",\"PDLO\",\"PWND\",\"IRS\",\"STAGE S7\",\"S5\",\"S6\"\n" +
                "\"1\",\"ABADIE Cyril\",\"M\",\"B\",\"  12.52 \",\"\",\"\",\"\",\"\",\"\",\"\",\"\"\n" +
                "\"2\",\"ALARD Sébastien\",\"M\",\"\",\"  13.72 \",\"  15.38 \",\"  10.97 \",\"  11.50 \",\"  18.57 \",\"  14.50 \",\"  13.19 \",\"  14.51 \"";
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
        CustomLogger.logInfo("size" + gradeTypes);
        assertTrue(gradeTypes.isEmpty());
        verify(gradeTypeRepository, never()).save(any(GradeType.class));
    }

    @Test
    void createGradeTypesFromCSV_withSummaryData_ignoresSummaryData() {
        String csvContent = "\"\",\"\",\"\",\"\",\"\",\"2\",\"3\",\"3\",\"2\",\"2\",\"1\",\"1\"\n" +
                "\"\",\"\",\"sexe\n" +
                "M / F\",\"\",\"\",\"PADL\",\"PDLO\",\"PWND\",\"IRS\",\"STAGE S7\",\"S5\",\"S6\"\n" +
                "\"1\",\"ABADIE Cyril\",\"M\",\"B\",\"  12.52 \",\"\",\"\",\"\",\"\",\"\",\"\",\"\"\n" +
                "\"2\",\"ALARD Sébastien\",\"M\",\"\",\"  13.72 \",\"  15.38 \",\"  10.97 \",\"  11.50 \",\"  18.57 \",\"  14.50 \",\"  13.19 \",\"  14.51 \"\n" +
                "\"\",\"Nombre F\",\"6\",\"\",\"\",\"\",\"\",\"\",\"\",\"\",\"\",\"\"\n" +
                "\"\",\"Nombre H\",\"42\",\"\",\"\",\"\",\"\",\"\",\"\",\"\",\"\",\"\"\n" +
                "\"\",\"Nombre B\",\"\",\"6\",\"\",\"\",\"\",\"\",\"\",\"\",\"\",\"\"";
        InputStream inputStream = new ByteArrayInputStream(csvContent.getBytes());

        List<GradeType> gradeTypes = gradeTypeService.createGradeTypesFromCSV(inputStream);

        assertEquals(7+1, gradeTypes.size()); // 7 columns + 1 for the average that is added by the service
        verify(gradeTypeRepository, times(7+1)).save(any(GradeType.class));
    }

}