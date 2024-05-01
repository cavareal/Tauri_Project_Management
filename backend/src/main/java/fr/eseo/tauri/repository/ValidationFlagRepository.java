package fr.eseo.tauri.repository;

import fr.eseo.tauri.model.ValidationBonus;
import org.springframework.data.jpa.repository.JpaRepository;
import fr.eseo.tauri.model.ValidationFlag;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ValidationFlagRepository extends JpaRepository<ValidationFlag, Integer> {

    @Query("SELECT vf FROM ValidationFlag vf WHERE vf.flag.project.id = :projectId")
    List<ValidationBonus> findAllByProject(Integer projectId);

    @Transactional
    @Modifying
    @Query(value = "DELETE FROM validation_bonuses WHERE bonus_id IN (SELECT id FROM bonuses WHERE sprint_id IN (SELECT id FROM sprints WHERE project_id = :projectId))", nativeQuery = true)
    void deleteAllByProject(Integer projectId);
}
