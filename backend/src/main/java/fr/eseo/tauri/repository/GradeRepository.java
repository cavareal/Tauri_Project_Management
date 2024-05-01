package fr.eseo.tauri.repository;

import fr.eseo.tauri.model.Grade;
import fr.eseo.tauri.model.Team;
import fr.eseo.tauri.model.enumeration.RoleType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface GradeRepository extends JpaRepository<Grade, Integer> {

	@Query("SELECT g FROM Grade g JOIN g.sprint s JOIN s.project p WHERE p.id = :projectId")
	List<Grade> findAllByProject(Integer projectId);

	@Transactional
	@Modifying
	@Query(value = "DELETE FROM grades WHERE sprint_id IN (SELECT id FROM sprints WHERE project_id = :projectId)", nativeQuery = true)
	void deleteAllByProject(Integer projectId);

	@Modifying
	@Transactional
	@Query("UPDATE Grade g SET g.value = :value WHERE g.student.id = :studentId AND g.gradeType.imported = true AND (lower(g.gradeType.name) = 'mean' OR lower(g.gradeType.name) = 'average')")
	void updateImportedMeanByStudentId(@Param("value") float value, @Param("studentId") int studentId);

	@Query("SELECT g FROM Grade g WHERE g.team.id = :teamId")
	List<Grade> findAllByTeamId(Long teamId);

	@Query("SELECT AVG(gr.value) FROM Grade gr, GradeType gt, User u, Role r " +
			"WHERE gr.gradeType.id = gt.id AND u.id = gr.author.id AND r.user.id = u.id " +
			"AND gt.name = :gradeTypeName " +
			"AND gr.team = :team " +
			"AND r.type = :roleType")
	public Double findAverageGradesByGradeType(@Param("team") Team team, @Param("gradeTypeName") String gradeTypeName, @Param("roleType") RoleType roleType);

}
