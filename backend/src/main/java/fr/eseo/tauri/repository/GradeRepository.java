package fr.eseo.tauri.repository;

import fr.eseo.tauri.model.Grade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface GradeRepository extends JpaRepository<Grade, Integer> {
    // Vous pouvez ajouter des requêtes personnalisées ici si nécessaire

	@Modifying
	@Transactional
	@Query("UPDATE Grade g SET g.value = :value WHERE g.student.id = :studentId AND g.gradeType.imported = true AND g.gradeType.name = 'mean'")
	void updateImportedMeanByStudentId(@Param("value") float value, @Param("studentId") int studentId);

	@Query("SELECT g FROM Grade g WHERE g.team.id = :teamId")
	List<Grade> findAllByTeamId(Long teamId);


//	@Query ("SELECT gt.name AS grade_type, AVG(g.value) AS average_grade " +
//			"FROM Grade g " +
//			"JOIN GradeType gt ON g.gradeType " +
//			"WHERE gt.imported IS false " +
//			"GROUP BY gt.name")
//	public List<Object[]> findAverageGrades();

	@Query("SELECT g.gradeType.name AS gradeType, r.type AS roleType, AVG(g.value) AS averageValue " +
			"FROM Grade g " +
			"JOIN g.gradeType gt " +
			"JOIN g.author a " +
			"JOIN Role r ON a.id = r.user.id " +
			"WHERE g.team.id = :teamId AND gt.imported IS false " +
			"GROUP BY g.gradeType.name, r.type")
	public List<Object[]> findAverageGradesByGradeType(@Param("teamId") int teamId);

//	@Query("SELECT gt.name AS gradeType, r.type AS roleType, AVG(g.value) AS averageValue " +
//			"FROM Grade g " +
//			"JOIN g.author a " +
//			"JOIN a.role r " +
//			"JOIN g.gradeType gt " +
//			"WHERE gt.id = :gradeTypeId AND r IS NOT NULL " +
//			"GROUP BY gt.name, r.type")
//	public List<Object[]> findAverageGradesByGradeTypeByRole();
}
