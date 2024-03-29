package fr.eseo.tauri.repository;

import fr.eseo.tauri.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import fr.eseo.tauri.model.Team;

import java.util.List;

public interface TeamRepository extends JpaRepository<Team, Integer> {
    List<Team> findByleaderId(User leaderId);
}
