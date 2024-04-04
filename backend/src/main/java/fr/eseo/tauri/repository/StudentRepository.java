package fr.eseo.tauri.repository;

import fr.eseo.tauri.model.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import fr.eseo.tauri.model.Student;

import java.util.List;

public interface StudentRepository extends JpaRepository<Student, Integer> {
    List<Student> findByTeamId(Team teamId);

    List<Student> findByTeamName(String teamName);
}
