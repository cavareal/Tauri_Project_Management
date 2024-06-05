package fr.eseo.tauri.service;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import fr.eseo.tauri.exception.EmptyResourceException;
import fr.eseo.tauri.model.GradeType;
import fr.eseo.tauri.model.enumeration.GradeTypeName;
import fr.eseo.tauri.repository.GradeTypeRepository;
import fr.eseo.tauri.util.CustomLogger;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import fr.eseo.tauri.exception.GlobalExceptionHandler;
import fr.eseo.tauri.exception.ResourceNotFoundException;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GradeTypeService {

    private final AuthService authService;
    private final GradeTypeRepository gradeTypeRepository;
    private final GradeService gradeService;
    private final ProjectService projectService;
    
    private static final String READ_PERMISSION = "readGradeType";
    private static final String ADD_PERMISSION = "addGradeType";
    private static final String UPDATE_PERMISSION = "updateGradeType";
    private static final String DELETE_PERMISSION = "deleteGradeType";

    public GradeType getGradeTypeById(String token, Integer id) {
        if (!Boolean.TRUE.equals(authService.checkAuth(token, READ_PERMISSION))) {
            throw new SecurityException(GlobalExceptionHandler.UNAUTHORIZED_ACTION);
        }
        return gradeTypeRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("gradeType", id));
    }

    public List<GradeType> getAllImportedGradeTypes(String token, Integer projectId) {
        if (!Boolean.TRUE.equals(authService.checkAuth(token, READ_PERMISSION))) {
            throw new SecurityException(GlobalExceptionHandler.UNAUTHORIZED_ACTION);
        }
        return gradeTypeRepository.findAllImported(projectId);
    }

    public List<GradeType> getAllUnimportedGradeTypes(String token, Integer projectId) {
        if (!Boolean.TRUE.equals(authService.checkAuth(token, READ_PERMISSION))) {
            throw new SecurityException(GlobalExceptionHandler.UNAUTHORIZED_ACTION);
        }
        return gradeTypeRepository.findAllUnimported(projectId);
    }

    public void createGradeType(String token, GradeType gradeType) {
        if (!Boolean.TRUE.equals(authService.checkAuth(token, ADD_PERMISSION))) {
            throw new SecurityException(GlobalExceptionHandler.UNAUTHORIZED_ACTION);
        }
        gradeTypeRepository.save(gradeType);
    }

    public void updateGradeType(String token, Integer id, GradeType updatedGradeType, Integer projectId) {
        if (!Boolean.TRUE.equals(authService.checkAuth(token, UPDATE_PERMISSION))) {
            throw new SecurityException(GlobalExceptionHandler.UNAUTHORIZED_ACTION);
        }

        GradeType gradeType = getGradeTypeById(token, id);
        CustomLogger.info("Updating GradeType with id " + gradeType);

        if (updatedGradeType.name() != null) gradeType.name(updatedGradeType.name());
        if (updatedGradeType.factor() != null) {
            gradeType.factor(updatedGradeType.factor());
            gradeService.updateImportedMean(projectId);
        }
        if (updatedGradeType.forGroup() != null) gradeType.forGroup(updatedGradeType.forGroup());
        if (updatedGradeType.imported() != null) gradeType.imported(updatedGradeType.imported());
        if (updatedGradeType.scalePDFBlob() != null) gradeType.scalePDFBlob(updatedGradeType.scalePDFBlob());
        if (updatedGradeType.project() != null) gradeType.project(updatedGradeType.project());

        gradeTypeRepository.save(gradeType);
    }

    public void deleteGradeTypeById(String token, Integer id) {
        if (!Boolean.TRUE.equals(authService.checkAuth(token, DELETE_PERMISSION))) {
            throw new SecurityException(GlobalExceptionHandler.UNAUTHORIZED_ACTION);
        }
        getGradeTypeById(token, id);
        gradeTypeRepository.deleteById(id);
    }

    public void deleteAllImportedGradeTypes(String token) {
        if (!Boolean.TRUE.equals(authService.checkAuth(token, DELETE_PERMISSION))) {
            throw new SecurityException(GlobalExceptionHandler.UNAUTHORIZED_ACTION);
        }
        gradeTypeRepository.deleteAllImported();
    }

    public void deleteAllUnimportedGradeTypes(String token) {
        if (!Boolean.TRUE.equals(authService.checkAuth(token, DELETE_PERMISSION))) {
            throw new SecurityException(GlobalExceptionHandler.UNAUTHORIZED_ACTION);
        }
        gradeTypeRepository.deleteAllUnimported();
    }

    /**
     * This method is used to create a list of GradeType objects from the provided coefficients and names.
     *
     * @param coefficients the list of coefficients for the GradeType objects
     * @param names      the list of names for the GradeType objects
     * @return a list of GradeType objects created from the provided coefficients and names
     */
    public List<GradeType> generateImportedGradeTypes(String token, List<String> coefficients, List<String> names) {

        if (!Boolean.TRUE.equals(authService.checkAuth(token, ADD_PERMISSION))) {
            throw new SecurityException(GlobalExceptionHandler.UNAUTHORIZED_ACTION);
        }

        if (coefficients == null || coefficients.isEmpty()) {
            CustomLogger.warn("The list of coefficients is null or empty");
            throw new EmptyResourceException("list of coefficients");
        }

        if(names == null || names.isEmpty()){
            CustomLogger.warn("The list of names is null or empty");
            throw new EmptyResourceException("list of names");
        }

        List<GradeType> importedGradeTypes = new ArrayList<>();

        importedGradeTypes.add(createImportedGradeType(GradeTypeName.AVERAGE.displayName(), (float) 0));

        for (int i = 0; i < coefficients.size(); i++) {
            importedGradeTypes.add(createImportedGradeType(names.get(i), Float.parseFloat(coefficients.get(i))));
        }

        CustomLogger.info("Successfully created GradeType objects from the provided coefficients and names.");
        return importedGradeTypes;
    }

    public GradeType createImportedGradeType(String name, Float factor) {
        GradeType gradeType = new GradeType();
        gradeType.name(name);
        gradeType.factor(factor);
        gradeType.forGroup(false);
        gradeType.imported(true);
        gradeType.project(projectService.getActualProject());
        return(gradeTypeRepository.save(gradeType));
    }

    /**
     * This method is used to create a list of GradeType objects from a CSV file.
     *
     * @param inputStream the InputStream from which the CSV file is read
     * @return a list of GradeType objects created from the coefficients and names in the CSV file
     */
    public List<GradeType> createGradeTypesFromCSV(String token, InputStream inputStream) throws CsvValidationException, IOException {
        List<String> coefficients = new ArrayList<>();
        List<String> names = new ArrayList<>();
        boolean coefficientsStarted = false;
        int startingCoefficients = 1;
        int lineBrowsed = 0;

        CSVReader reader = new CSVReader(new InputStreamReader(inputStream));
        String[] nextLine;
        while ((nextLine = reader.readNext()) != null) {
            lineBrowsed++;
            if (!coefficientsStarted) {
                startingCoefficients = processLineForCoefficients(nextLine, coefficients);
                coefficientsStarted = true;
            } else if (lineBrowsed == 2) {
                processLineForNames(nextLine, names, startingCoefficients);
            }
        }

        return generateImportedGradeTypes(token, coefficients, names);
    }

    /**
     * <b>HELPER METHOD</b>
     * This method is used to process a line from a CSV file and extract the coefficients.
     *
     * @param nextLine     the line from the CSV file as an array of strings
     * @param coefficients the list of coefficients to which the extracted coefficients are added
     * @return the starting index of the coefficients in the line
     */
    public int processLineForCoefficients(String[] nextLine, List<String> coefficients) {
        int startingCoefficients = 1;
        for (String part : nextLine) {
            String trimmedPart = part.trim();
            try {
                Float.parseFloat(trimmedPart); // Check if it's a coefficient
                coefficients.add(trimmedPart);
            } catch (NumberFormatException ignored) {
                startingCoefficients++;
            }
        }
        return startingCoefficients;
    }

    /**
     * <b>HELPER METHOD</b>
     * This method is used to process a line from a CSV file and extract the names.
     *
     * @param nextLine             the line from the CSV file as an array of strings
     * @param names              the list of names to which the extracted names are added
     * @param startingCoefficients the starting index of the coefficients in the line
     */
    public void processLineForNames(String[] nextLine, List<String> names, int startingCoefficients) {
        int index = 0;
        for (String part : nextLine) {
            index++;
            if (index >= startingCoefficients) {
                String trimmedPart = part.trim();
                names.add(trimmedPart);
            }
        }
    }


    public GradeType findByName(String name, String token, Integer projectId) {
        if (!Boolean.TRUE.equals(authService.checkAuth(token, READ_PERMISSION))) {
            throw new SecurityException(GlobalExceptionHandler.UNAUTHORIZED_ACTION);
        }
        return gradeTypeRepository.findByNameAndProjectId(name, projectId);
    }
    
}


