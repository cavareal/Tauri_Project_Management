package fr.eseo.tauri.repository;

import fr.eseo.tauri.model.Grade;
import fr.eseo.tauri.model.GradeType;
import fr.eseo.tauri.model.Student;
import fr.eseo.tauri.model.Team;
import fr.eseo.tauri.model.enumeration.GradeTypeName;
import fr.eseo.tauri.model.enumeration.RoleType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface GradeRepository extends JpaRepository<Grade, Integer> {

	@Query("SELECT g FROM Grade g WHERE g.gradeType.imported = true  AND g.student.project.id = :projectId")
	List<Grade> findAllImportedByProject(Integer projectId);

	@Query("SELECT g FROM Grade g WHERE g.gradeType.imported = false  AND g.student.project.id = :projectId")
	List<Grade> findAllUnimportedByProject(Integer projectId);

	@Transactional
	@Modifying
	@Query(value = "DELETE FROM grades WHERE sprint_id IN (SELECT id FROM sprints WHERE project_id = :projectId)", nativeQuery = true)
	void deleteAllByProject(Integer projectId);

	@Query("SELECT g FROM Grade g WHERE g.team.id = :teamId")
	List<Grade> findAllByTeamId(Long teamId);

	@Query("SELECT AVG(gr.value) FROM Grade gr, GradeType gt, User u, Role r " +
			"WHERE gr.gradeType.id = gt.id AND u.id = gr.author.id AND r.user.id = u.id " +
			"AND gt.name = :gradeTypeName " +
			"AND gr.team = :team " +
			"AND r.type = :roleType")
	Double findAverageGradesByGradeType(Team team, String gradeTypeName, RoleType roleType);

	@Query("SELECT AVG(g.value) FROM Grade g JOIN Role r ON g.author.id = r.user.id WHERE g.gradeType = :gradeType AND g.team = :team AND r.type = :roleType")
	Double findAverageTeamGradeByGradeTypeAndRoleType(Team team, GradeType gradeType, RoleType roleType);

	@Query("SELECT AVG(g.value) FROM Grade g JOIN Role r ON g.author.id = r.user.id WHERE g.gradeType = :gradeType AND g.student = :student AND r.type = :roleType")
	Double findAverageStudentGradeByGradeTypeAndRoleType(Student student, GradeType gradeType, RoleType roleType);

	@Modifying
	@Transactional
	@Query("UPDATE Grade g SET g.value = :value WHERE g.student.id = :studentId AND g.gradeType.imported AND g.gradeType.name = :gradeTypeName")
	void updateImportedMeanByStudentId(Float value, Integer studentId, String gradeTypeName);

	@Modifying
	@Transactional
	default void updateImportedMeanByStudentId(Float value, Integer studentId) {
		updateImportedMeanByStudentId(value, studentId, GradeTypeName.AVERAGE.displayName());
	}

	@Query("SELECT g FROM Grade g WHERE g.student.id = :studentId AND g.gradeType.imported = true AND g.gradeType.imported=true")
	public List<Grade> findImportedGradesByStudentId(int studentId);


	@Query("SELECT g.value FROM Grade g WHERE g.student = :student AND g.gradeType = :gradeType")
	public Float findValueByStudentAndGradeType(Student student, GradeType gradeType);
}
