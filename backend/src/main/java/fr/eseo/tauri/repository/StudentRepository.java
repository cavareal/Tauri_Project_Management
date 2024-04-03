package fr.eseo.tauri.repository;

import fr.eseo.tauri.model.Team;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;
import fr.eseo.tauri.model.Student;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface StudentRepository extends JpaRepository<Student, Integer> {
    List<Student> findByTeamId(Team teamId);

    @Query("SELECT s FROM Grade gr JOIN gr.student s JOIN gr.gradeType gt WHERE gt.name = 'average' ORDER BY gr.value DESC")
    List<Student> findAllOrderByImportedAvg();
}
