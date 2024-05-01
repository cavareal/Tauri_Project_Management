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

	@Query("SELECT g FROM GradeType g WHERE g.imported")
	List<GradeType> findAllImported();

	@Query("SELECT g FROM GradeType g WHERE g.name = :name")
	GradeType findByName(String name);

	@Modifying
	@Transactional
	@Query("DELETE FROM GradeType g WHERE g.imported")
	void deleteAllImported();

	@Query("SELECT g FROM GradeType g WHERE g.forGroup AND NOT g.imported")
	List<GradeType> findAllNotImportedTeamGrades();

}
