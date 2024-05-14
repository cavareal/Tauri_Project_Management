package fr.eseo.tauri.repository;

import fr.eseo.tauri.model.Bonus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface BonusRepository extends JpaRepository<Bonus, Integer> {

    @Query("SELECT b FROM Bonus b WHERE b.sprint.project.id = :projectId")
    List<Bonus> findAllByProject(Integer projectId);

    @Transactional
    @Modifying
    @Query(value = "DELETE FROM bonuses WHERE sprint_id IN (SELECT id FROM sprints WHERE project_id = :projectId)", nativeQuery = true)
    void deleteAllByProject(Integer projectId);

    @Query("SELECT b FROM Bonus b WHERE b.student.id = :studentId ORDER BY b.limited ASC")
    List<Bonus> findAllStudentBonuses(Integer studentId);

    @Query("SELECT b FROM Bonus b WHERE b.student.id = :studentId AND b.limited = :limited")
    Bonus findStudentBonus(Integer studentId, Boolean limited);

}
