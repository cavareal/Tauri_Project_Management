package fr.eseo.tauri.service;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import fr.eseo.tauri.model.GradeType;
import fr.eseo.tauri.repository.GradeTypeRepository;
import fr.eseo.tauri.util.CustomLogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

@Service
public class GradeTypeService {

    private final GradeService gradeService;
    private final GradeTypeRepository gradeTypeRepository;

    /**
     * Constructor for the GradeTypeService class.
     *
     * @param gradeTypeRepository the repository that provides CRUD operations for GradeType objects
     * @param gradeService the service that provides business logic for Grade objects
     */
    @Autowired
    public GradeTypeService(GradeTypeRepository gradeTypeRepository, GradeService gradeService) {
        this.gradeTypeRepository = gradeTypeRepository;
        this.gradeService = gradeService;
    }

    /**
     * This method is used to update the factor of a GradeType object and save it to the database.
     *
     * @param id the ID of the GradeType object to be updated
     * @param factor the new factor for the GradeType object
     *
     * @return the updated GradeType object, or null if no GradeType object with the provided ID exists
     */
    public GradeType updateFactor(int id, float factor) {
        var gradeType = gradeTypeRepository.findById(id).orElse(null);
        if (gradeType == null) return null;

        gradeType.factor(factor);
        gradeTypeRepository.save(gradeType);

        gradeService.updateImportedMean();
        CustomLogger.logInfo("Successfully updated factor for GradeType object with ID " + id);

        return gradeType;
    }

    /**
     * This method is used to create a new GradeType object and save it to the database.
     *
     * @param gradeType the GradeType object to be saved
     * @return the saved GradeType object
     * @throws IllegalArgumentException if the name of the GradeType object is null or empty
     */
    public GradeType createGradeType(GradeType gradeType) {
        if (gradeType.name() == null) {
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
            CustomLogger.logWarn("Coefficients or ratings are null or empty");
            return new ArrayList<>();
        }
        List<GradeType> gradeTypes = new ArrayList<>();
        gradeTypes.add(createGradeType(0, "AVERAGE", forGroup, imported));
        for (int i = 0; i < coefficients.size(); i++) {
            gradeTypes.add(createGradeType(Float.parseFloat(coefficients.get(i)), ratings.get(i), forGroup, imported));
        }
        CustomLogger.logInfo("Successfully created GradeType objects from the provided coefficients and ratings.");
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
           CustomLogger.logError("Error occurred while extracting coefficient rating and value", e);
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
                Integer.parseInt(trimmedPart); // Check if it's a coefficient
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
        gradeTypeRepository.deleteAllImported();
        CustomLogger.logInfo("Successfully deleted all imported GradeType objects.");
    }
}


