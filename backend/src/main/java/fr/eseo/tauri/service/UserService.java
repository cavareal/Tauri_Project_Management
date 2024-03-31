package fr.eseo.tauri.service;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import fr.eseo.tauri.model.Team;
import fr.eseo.tauri.repository.ProjectRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import fr.eseo.tauri.repository.TeamRepository;
import fr.eseo.tauri.model.User;
import fr.eseo.tauri.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final TeamRepository teamRepository;

    /**
     * Constructor for UserService.
     * @param teamRepository the team repository
     * @param userRepository the user repository
     */

    @Autowired
    public UserService(UserRepository userRepository, TeamRepository teamRepository) {
        this.userRepository = userRepository;
        this.teamRepository = teamRepository;
    }

    /**
     * Change team's leader to null when their leader is deleted.
     * @param id the user's id
     */

    public void deleteUser(Integer id) {
        Optional<User> user = userRepository.findById(Long.valueOf(id));
        if (user.isPresent()) {
            List<Team> teams = teamRepository.findByleaderId(user.get());
            for (Team team : teams) {
                team.leaderId(null);
                teamRepository.save(team);
            }
            userRepository.deleteById(Long.valueOf(id));
        }
    }

    public void createUser(User user) {
        userRepository.save(user);
    }

    /*@PostConstruct //Test function for the deleteUser function
    public void initDataIfTableIsEmpty() {

        if (userRepository.count() != 0) {
            this.deleteUser(1);
        }

    }*/

    /**
     * Extracts names, genders, and bachelor status from a CSV file.
     *
     * @param filePath the path to the CSV file
     * @return a list containing three lists: names, genders, and bachelor status
     *         <p>
     *         The returned list contains three inner lists:
     *         <ul>
     *         <li>names: List of names extracted from the CSV file.</li>
     *         <li>genders: List of genders extracted from the CSV file.</li>
     *         <li>bachelor status: List of bachelor status extracted from the CSV file.</li>
     *         </ul>
     *         </p>
     */
    public static List<List<String>> extractNamesGenderAndBachelor(String filePath) {
        List<List<String>> result = new ArrayList<>();
        List<String> names = new ArrayList<>();
        List<String> genders = new ArrayList<>();
        List<String> bachelors = new ArrayList<>();

        boolean namesStarted = false;

        try (CSVReader reader = new CSVReader(new FileReader(filePath))) {
            String[] nextLine;
            while ((nextLine = reader.readNext()) != null) {
                if (!namesStarted && hasNonEmptyValue(nextLine, 1)) {
                    namesStarted = true;
                }

                if (namesStarted && !names.isEmpty() && isEmpty(nextLine, 1)) {
                    break;
                }

                if (namesStarted && !isEmpty(nextLine, 1)) {
                    names.add(nextLine[1]); // Assuming complete name is in the second column
                    genders.add(nextLine[2]);
                    bachelors.add(nextLine.length > 3 ? nextLine[3] : ""); // Add bachelor status or empty string
                }
            }
        } catch (IOException | CsvValidationException e) {
            e.printStackTrace();
        }

        result.add(names);
        result.add(genders);
        result.add(bachelors);
        return result;
    }

    /**
     * Checks if the specified index in the given line contains a non-empty value.
     *
     * @param line  the array representing a line from the CSV file
     * @param index the index to check
     * @return {@code true} if the index contains a non-empty value, {@code false} otherwise
     */
    private static boolean hasNonEmptyValue(String[] line, int index) {
        return line.length > index && !line[index].trim().isEmpty();
    }

    /**
     * Checks if the specified index in the given line contains an empty value.
     *
     * @param line  the array representing a line from the CSV file
     * @param index the index to check
     * @return {@code true} if the index contains an empty value, {@code false} otherwise
     */
    private static boolean isEmpty(String[] line, int index) {
        return line.length > index && line[index].trim().isEmpty();
    }

    public static void main(String[] args) {
        String filePath = "C:\\Users\\coren\\Downloads\\Equipes-–-Exemple_1_.csv";
        List<List<String>> extractedData = extractNamesGenderAndBachelor(filePath);

        List<String> names = extractedData.get(0);
        List<String> genders = extractedData.get(1);
        List<String> bachelors = extractedData.get(2);

        for (int i = 0; i < names.size(); i++) {
            System.out.println(names.get(i) + " " + genders.get(i) + " " + bachelors.get(i));
        }
    }
}
