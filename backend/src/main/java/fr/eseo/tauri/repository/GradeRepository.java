package fr.eseo.tauri.repository;

import fr.eseo.tauri.model.Grade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface GradeRepository extends JpaRepository<Grade, Integer> {
    // Vous pouvez ajouter des requêtes personnalisées ici si nécessaire

	@Modifying
	@Transactional
	@Query("UPDATE Grade g SET g.value = :value WHERE g.student.id = :studentId AND g.gradeType.imported = true AND g.gradeType.name = 'mean'")
	void updateImportedMeanByStudentId(@Param("value") float value, @Param("studentId") int studentId);

}
