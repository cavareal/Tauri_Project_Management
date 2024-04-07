package fr.eseo.tauri.repository;

import fr.eseo.tauri.model.Team;
import fr.eseo.tauri.model.enumeration.Gender;
import org.springframework.data.jpa.repository.JpaRepository;
import fr.eseo.tauri.model.Student;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface StudentRepository extends JpaRepository<Student, Integer> {
    List<Student> findByTeamId(Team teamId);

    List<Student> findByGender(Gender gender);

    List<Student> findByGenderOrderByBachelor(Gender gender);

    // TODO: change the average grade name to the correct one
    @Query("SELECT s FROM Grade gr JOIN gr.student s JOIN gr.gradeType gt WHERE gt.name = 'average' AND s.gender = ?1 ORDER BY gr.value DESC")
    List<Student> findByGenderOrderByImportedAvg(Gender gender);
}
