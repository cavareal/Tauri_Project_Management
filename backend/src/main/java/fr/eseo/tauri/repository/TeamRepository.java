package fr.eseo.tauri.repository;

import fr.eseo.tauri.model.Student;
import fr.eseo.tauri.model.Team;
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

    @Query("SELECT COUNT(s) FROM Student s WHERE s.team.id = :teamId AND s.gender = 'WOMAN'")
    Integer countWomenInTeam(Integer teamId);

    @Query("SELECT COUNT(s) FROM Student s WHERE s.team.id = :teamId AND s.bachelor = true")
    Integer countBachelorInTeam(Integer teamId);

    @Query("SELECT s FROM Student s WHERE s.team.id = :teamId")
    List<Student> findByTeamId(Integer teamId);

    @Query("SELECT t FROM Team t WHERE t.leader.id = :leaderId AND t.project.id = :projectId")
    Team findByLeaderId(Integer leaderId, Integer projectId);

    @Query("SELECT t FROM Team t WHERE t.leader.id = :leaderId")
    List<Team> findByLeaderId(Integer leaderId);

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
