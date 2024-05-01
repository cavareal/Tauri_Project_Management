package fr.eseo.tauri.repository;

import fr.eseo.tauri.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import fr.eseo.tauri.model.Flag;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface FlagRepository extends JpaRepository<Flag, Integer> {
    @Query("SELECT f FROM Flag f JOIN f.project p WHERE p.id = :projectId")
    List<Flag> findAllByProject(Integer projectId);

    @Transactional
    @Modifying
    @Query(value = "DELETE FROM flags WHERE project_id = :projectId", nativeQuery = true)
    void deleteAllByProject(Integer projectId);
}
