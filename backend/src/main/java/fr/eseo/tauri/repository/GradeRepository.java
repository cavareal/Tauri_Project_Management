package fr.eseo.tauri.repository;

import fr.eseo.tauri.model.Grade;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GradeRepository extends JpaRepository<Grade, Integer> {
    // Vous pouvez ajouter des requêtes personnalisées ici si nécessaire
}
