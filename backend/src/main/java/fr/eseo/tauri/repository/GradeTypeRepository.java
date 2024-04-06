package fr.eseo.tauri.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import fr.eseo.tauri.model.GradeType;
import org.springframework.data.jpa.repository.Query;

public interface GradeTypeRepository extends JpaRepository<GradeType, Integer> {
    // Vous pouvez ajouter des requêtes personnalisées ici si nécessaire

	@Query("SELECT g FROM GradeType g WHERE g.imported")
	public GradeType findAllImported();

	@Query("SELECT g FROM GradeType g WHERE g.name = ?1")
	public GradeType findByName(String name);

}
