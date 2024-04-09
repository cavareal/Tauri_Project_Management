package fr.eseo.tauri.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import fr.eseo.tauri.model.GradeType;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface GradeTypeRepository extends JpaRepository<GradeType, Integer> {

	@Query("SELECT g FROM GradeType g WHERE g.imported")
	public List<GradeType> findAllImported();

	@Query("SELECT g FROM GradeType g WHERE g.name = ?1")
	public GradeType findByName(String name);

	@Query("SELECT g FROM GradeType g WHERE g.imported = false AND g.forGroup = true")
	public List<GradeType> findAllNotImportedTeamGrades();

}
