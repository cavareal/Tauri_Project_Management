package fr.eseo.tauri.repository;

import fr.eseo.tauri.model.Student;
import fr.eseo.tauri.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import fr.eseo.tauri.model.Team;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TeamRepository extends JpaRepository<Team, Integer> {
    List<Team> findByLeader(User leader);

    @Query("SELECT t FROM Team t WHERE t.name = :name")
    Team findByName(@Param("name") String name);
    @Query("SELECT t.name FROM Team t")
    List<String> findAllTeamNames();

    // find team with studentId
    @Query("SELECT s.team FROM Student s WHERE s.id = :studentId")
    Team findTeamByStudentId(int studentId);
}
