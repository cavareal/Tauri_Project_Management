package fr.eseo.tauri.repository;

import fr.eseo.tauri.model.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import fr.eseo.tauri.model.Student;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface StudentRepository extends JpaRepository<Student, Integer> {
    List<Student> findByTeamId(Team teamId);

    @Query("SELECT s FROM Student s WHERE s.teamId = :team")
    List<Student> findStudentsByTeam(@Param("team") Team team);
}
