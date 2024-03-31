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
     *
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
     *
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

}
