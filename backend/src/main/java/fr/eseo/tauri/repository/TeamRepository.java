package fr.eseo.tauri.repository;

import fr.eseo.tauri.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import fr.eseo.tauri.model.Team;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TeamRepository extends JpaRepository<Team, Integer> {
    List<Team> findByleaderId(User leaderId);

    @Query("SELECT AVG(gr.value) FROM Grade gr JOIN gr.student s JOIN gr.gradeType gt WHERE s.team = ?1 AND gt.name = 'average'")
    double findAvgGradeByTeamId(Team team);
}
