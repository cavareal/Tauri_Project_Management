package fr.eseo.tauri.service;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import fr.eseo.tauri.model.GradeType;
import fr.eseo.tauri.repository.GradeTypeRepository;
import fr.eseo.tauri.util.CustomLogger;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
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

    public GradeType getGradeTypeById(String token, Integer id) {
        if (!Boolean.TRUE.equals(authService.checkAuth(token, "readGradeType"))) {
            throw new SecurityException(GlobalExceptionHandler.UNAUTHORIZED_ACTION);
        }
        return gradeTypeRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("gradeType", id));
    }

    public List<GradeType> getAllImportedGradeTypes(String token) {
        if (!Boolean.TRUE.equals(authService.checkAuth(token, "readGradeTypes"))) {
            throw new SecurityException(GlobalExceptionHandler.UNAUTHORIZED_ACTION);
        }
        return gradeTypeRepository.findAllImported();
    }

    public List<GradeType> getAllUnimportedGradeTypes(String token) {
        if (!Boolean.TRUE.equals(authService.checkAuth(token, "readGradeTypes"))) {
            throw new SecurityException(GlobalExceptionHandler.UNAUTHORIZED_ACTION);
        }
        return gradeTypeRepository.findAllUnimported();
    }

    public void createGradeType(String token, GradeType gradeType) {
        if (!Boolean.TRUE.equals(authService.checkAuth(token, "addGradeType"))) {
            throw new SecurityException(GlobalExceptionHandler.UNAUTHORIZED_ACTION);
        }
        gradeTypeRepository.save(gradeType);
    }

    public void updateGradeType(String token, Integer id, GradeType updatedGradeType) {
        if (!Boolean.TRUE.equals(authService.checkAuth(token, "updateGradeType"))) {
            throw new SecurityException(GlobalExceptionHandler.UNAUTHORIZED_ACTION);
        }

        GradeType gradeType = getGradeTypeById(token, id);

        if (updatedGradeType.name() != null) gradeType.name(updatedGradeType.name());
        if (updatedGradeType.factor() != null) gradeType.factor(updatedGradeType.factor());
        if (updatedGradeType.forGroup() != null) gradeType.forGroup(updatedGradeType.forGroup());
        if (updatedGradeType.imported() != null) gradeType.imported(updatedGradeType.imported());
        if (updatedGradeType.scaleUrl() != null) gradeType.scaleUrl(updatedGradeType.scaleUrl());

        gradeTypeRepository.save(gradeType);
        gradeService.updateImportedMean();
    }

    public void deleteGradeTypeById(String token, Integer id) {
        if (!Boolean.TRUE.equals(authService.checkAuth(token, "deleteGradeType"))) {
            throw new SecurityException(GlobalExceptionHandler.UNAUTHORIZED_ACTION);
        }
        getGradeTypeById(token, id);
        gradeTypeRepository.deleteById(id);
    }

    public void deleteAllGradeTypes(String token) {
        if (!Boolean.TRUE.equals(authService.checkAuth(token, "deleteGradeType"))) {
            throw new SecurityException(GlobalExceptionHandler.UNAUTHORIZED_ACTION);
        }
        gradeTypeRepository.deleteAll();
    }

    /**
     * This method is used to create a new GradeType object and save it to the database.
     *
     * @param gradeType the GradeType object to be saved
     * @return the saved GradeType object
     * @throws IllegalArgumentException if the name of the GradeType object is null or empty
     */
    public GradeType createGradeType(GradeType gradeType) {
        if (StringUtils.isBlank(gradeType.name())) {
            throw new IllegalArgumentException("Name cannot be null or empty");
        }
        gradeTypeRepository.save(gradeType);
        return gradeType;
    }

    /**
     * This method is used to create a new GradeType object and save it to the database.
     *
     * @param coefficient the coefficient for the GradeType object
     * @param rating      the rating for the GradeType object
     * @param forGroup    the forGroup value for the GradeType object
     * @param imported    the imported value for the GradeType object
     * @return the saved GradeType object
     */
    public GradeType createGradeType(float coefficient, String rating, Boolean forGroup, Boolean imported) {
        GradeType gradeType = new GradeType();
        gradeType.factor(coefficient);
        gradeType.name(rating);
        gradeType.forGroup(forGroup);
        gradeType.imported(imported);
        return createGradeType(gradeType);
    }

    /**
     * This method is used to create a list of GradeType objects from the provided coefficients and ratings.
     *
     * @param coefficients the list of coefficients for the GradeType objects
     * @param ratings      the list of ratings for the GradeType objects
     * @param forGroup     the forGroup value for the GradeType objects
     * @param imported     the imported value for the GradeType objects
     * @return a list of GradeType objects created from the provided coefficients and ratings
     */
    public List<GradeType> createGradeTypes(List<String> coefficients, List<String> ratings, Boolean forGroup, Boolean imported) {
        if (coefficients == null || ratings == null || coefficients.isEmpty() || ratings.isEmpty()) {
            CustomLogger.warn("Coefficients or ratings are null or empty");
            return new ArrayList<>();
        }
        List<GradeType> gradeTypes = new ArrayList<>();
        gradeTypes.add(createGradeType(0, "AVERAGE", forGroup, imported));
        for (int i = 0; i < coefficients.size(); i++) {
            gradeTypes.add(createGradeType(Float.parseFloat(coefficients.get(i)), ratings.get(i), forGroup, imported));
        }
        CustomLogger.info("Successfully created GradeType objects from the provided coefficients and ratings.");
        return gradeTypes;
    }

    /**
     * This method is used to create a list of GradeType objects from a CSV file.
     *
     * @param inputStream the InputStream from which the CSV file is read
     * @return a list of GradeType objects created from the coefficients and ratings in the CSV file
     */
    public List<GradeType> createGradeTypesFromCSV(InputStream inputStream) {
        List<String> coefficients = new ArrayList<>();
        List<String> ratings = new ArrayList<>();
        boolean coefficientsStarted = false;
        int startingCoefficients = 1;
        int lineBrowsed = 0;

        try (CSVReader reader = new CSVReader(new InputStreamReader(inputStream))) {
            String[] nextLine;
            while ((nextLine = reader.readNext()) != null) {
                lineBrowsed++;
                if (!coefficientsStarted) {
                    startingCoefficients = processLineForCoefficients(nextLine, coefficients);
                    coefficientsStarted = true;
                } else if (lineBrowsed == 2) {
                    processLineForRatings(nextLine, ratings, startingCoefficients);
                }
            }
        } catch (IOException | CsvValidationException e) {
           CustomLogger.error("Error occurred while extracting coefficient rating and value", e);
        }
        return createGradeTypes(coefficients, ratings, false, true);
    }

    /**
     * <b>HELPER METHOD</b>
     * This method is used to process a line from a CSV file and extract the coefficients.
     *
     * @param nextLine     the line from the CSV file as an array of strings
     * @param coefficients the list of coefficients to which the extracted coefficients are added
     * @return the starting index of the coefficients in the line
     */
    private int processLineForCoefficients(String[] nextLine, List<String> coefficients) {
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
     * This method is used to process a line from a CSV file and extract the ratings.
     *
     * @param nextLine             the line from the CSV file as an array of strings
     * @param ratings              the list of ratings to which the extracted ratings are added
     * @param startingCoefficients the starting index of the coefficients in the line
     */
    private void processLineForRatings(String[] nextLine, List<String> ratings, int startingCoefficients) {
        int index = 0;
        for (String part : nextLine) {
            index++;
            if (index >= startingCoefficients) {
                String trimmedPart = part.trim();
                ratings.add(trimmedPart);
            }
        }
    }

    /**
     * This method is used to delete all imported GradeType objects from the database.
     */
    public void deleteAllImportedGradeTypes() {
        try {
            gradeTypeRepository.deleteAllImported();
        } catch (Exception e) {
            CustomLogger.error("Error occurred while deleting all imported GradeType objects", e);
        }
    }

    public List<GradeType> getImportedGradeTypes() {
        return gradeTypeRepository.findAllImported();
    }
}


