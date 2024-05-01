package fr.eseo.tauri.repository;

import fr.eseo.tauri.model.Team;
import fr.eseo.tauri.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface TeamRepository extends JpaRepository<Team, Integer> {

    @Query("SELECT t FROM Team t WHERE t.project.id = :projectId")
    List<Team> findAllByProject(Integer projectId);

    @Transactional
    @Modifying
    @Query(value = "DELETE FROM teams WHERE project_id = :projectId", nativeQuery = true)
    void deleteAllByProject(Integer projectId);


    List<Team> findByLeader(User leader);

    @Query("SELECT t FROM Team t WHERE t.name = :name")
    Team findByName(String name);

    @Query("SELECT t.name FROM Team t")
    List<String> findAllTeamNames();

    @Query("SELECT s.team FROM Grade gr JOIN gr.student s JOIN gr.gradeType gt WHERE gt.name = 'AVERAGE' and s.team IS NOT NULL GROUP BY s.team ORDER BY AVG(gr.value) ASC")
    List<Team> findAllOrderByAvgGradeOrderByAsc();

    @Query("SELECT AVG(gr.value) FROM Grade gr JOIN gr.student s JOIN gr.gradeType gt WHERE s.team = ?1 AND gt.name = 'AVERAGE'")
    double findAvgGradeByTeam(Team team);

    @Query("SELECT s.team FROM Student s WHERE s.id = :studentId")
    Team findTeamByStudentId(int studentId);

}
