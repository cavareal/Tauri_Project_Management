package fr.eseo.tauri.repository;

import fr.eseo.tauri.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import fr.eseo.tauri.model.Team;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TeamRepository extends JpaRepository<Team, Integer> {
    List<Team> findByLeader(User leader);

    @Query("SELECT t.name FROM Team t")
    List<String> findAllTeamNames();

}
