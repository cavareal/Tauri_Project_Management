package fr.eseo.tauri.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import fr.eseo.tauri.model.ValidationBonus;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ValidationBonusRepository extends JpaRepository<ValidationBonus, Integer> {

    @Query("SELECT vb FROM ValidationBonus vb WHERE vb.bonus.sprint.project.id = :projectId")
    List<ValidationBonus> findAllByProject(@Param("projectId") Integer projectId);

    @Transactional
    @Modifying
    @Query(value = "DELETE FROM validation_bonuses WHERE bonus_id IN (SELECT id FROM bonuses WHERE sprint_id IN (SELECT id FROM sprints WHERE project_id = :projectId))", nativeQuery = true)
    void deleteAllByProject(@Param("projectId") Integer projectId);

}
