package fr.eseo.tauri.repository;

import fr.eseo.tauri.model.Team;
import fr.eseo.tauri.model.enumeration.Gender;
import org.springframework.data.jpa.repository.JpaRepository;
import fr.eseo.tauri.model.Student;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface StudentRepository extends JpaRepository<Student, Integer> {
    @Query("SELECT s FROM Student s JOIN s.project p WHERE p.id = :projectId")
    List<Student> findAllByProject(Integer projectId);

    @Transactional
    @Modifying
    @Query(value = "DELETE FROM students WHERE project_id = :projectId", nativeQuery = true)
    void deleteAllByProject(Integer projectId);

    List<Student> findByTeamName(String teamName);

    Student findByName(String studentName);
    List<Student> findByTeam(Team team);

    @Query("SELECT s FROM Student s WHERE s.team = :team")
    List<Student> findStudentsByTeam(@Param("team") Team team);

    List<Student> findByGender(Gender gender);

    List<Student> findByGenderOrderByBachelor(Gender gender);

    // TODO: change the average grade name to the correct one
    @Query("SELECT s FROM Grade gr JOIN gr.student s JOIN gr.gradeType gt WHERE gt.name = 'AVERAGE' AND s.gender = ?1 ORDER BY s.bachelor, gr.value DESC")
    List<Student> findByGenderOrderByBachelorAndImportedAvgDesc(Gender gender);
}
