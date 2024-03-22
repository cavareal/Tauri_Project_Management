package fr.eseo.tauri.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import fr.eseo.tauri.model.GradeScale;

public interface GradeScaleRepository extends JpaRepository<GradeScale, Integer> {
    // Vous pouvez ajouter des requêtes personnalisées ici si nécessaire
}
