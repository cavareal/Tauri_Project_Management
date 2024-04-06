package fr.eseo.tauri.service;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import fr.eseo.tauri.model.GradeType;
import fr.eseo.tauri.repository.GradeTypeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

@Service
public class GradeTypeService {

    private static final Logger LOGGER = LoggerFactory.getLogger(GradeTypeService.class);
    private final GradeTypeRepository gradeTypeRepository;

    @Autowired
    public GradeTypeService(GradeTypeRepository gradeTypeRepository) {
        this.gradeTypeRepository = gradeTypeRepository;
    }

    public void createGradeType(GradeType gradeType) {
        if (gradeType.name() == null || gradeType.name().trim().isEmpty()) {
            throw new IllegalArgumentException("Name cannot be null or empty");
        }
        gradeTypeRepository.save(gradeType);
    }

    public GradeType createGradeType(double coefficient, String rating, Boolean forGroup, Boolean imported) {
        GradeType gradeType = new GradeType();
        gradeType.factor(coefficient);
        gradeType.name(rating);
        gradeType.forGroup(forGroup);
        gradeType.imported(imported);
        return gradeTypeRepository.save(gradeType);
    }

    public List<GradeType> createGradeTypes(List<String> coefficients, List<String> ratings, Boolean forGroup, Boolean imported) {
        List<GradeType> gradeTypes = new ArrayList<>();
        gradeTypes.add(createGradeType(0, "B3/E3 average grades", forGroup, imported));
        for (int i = 0; i < coefficients.size(); i++) {
            gradeTypes.add(createGradeType(Double.parseDouble(coefficients.get(i)), ratings.get(i), forGroup, imported));
        }
        return gradeTypes;
    }

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
            LOGGER.error("Error occurred while extracting coefficient rating and value", e);
        }

        return createGradeTypes(coefficients, ratings, false, true);
    }

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
}
