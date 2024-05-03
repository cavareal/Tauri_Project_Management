package fr.eseo.tauri.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import fr.eseo.tauri.model.GradeType;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface GradeTypeRepository extends JpaRepository<GradeType, Integer> {

	@Query("SELECT g FROM GradeType g WHERE g.forGroup")
	List<GradeType> findAllForGroup();

	@Query("SELECT gt FROM GradeType gt WHERE gt.imported = true")
	List<GradeType> findAllImported();

	@Query("SELECT gt FROM GradeType gt WHERE gt.imported = false")
	List<GradeType> findAllUnimported();

	@Query("SELECT gt FROM GradeType gt WHERE gt.name = :name")
	GradeType findByName(String name);

	@Modifying
	@Transactional
	@Query("DELETE FROM GradeType gt WHERE gt.imported = true")
	void deleteAllImported();

	@Modifying
	@Transactional
	@Query("DELETE FROM GradeType gt WHERE gt.imported = false")
	void deleteAllUnimported();

}
