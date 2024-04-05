package fr.eseo.tauri.repository;

import fr.eseo.tauri.model.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import fr.eseo.tauri.model.Student;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface StudentRepository extends JpaRepository<Student, Integer> {
    List<Student> findByTeamId(Team teamId);

    List<Student> findByTeamName(String teamName);

    Student findByName(String studentName);
    List<Student> findByTeam(Team team);

    @Query("SELECT s FROM Student s WHERE s.team = :team")
    List<Student> findStudentsByTeam(@Param("team") Team team);
}
