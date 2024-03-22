package fr.eseo.tauri.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import fr.eseo.tauri.model.ValidationFlag;

public interface ValidationFlagRepository extends JpaRepository<ValidationFlag, Integer> {
    // Vous pouvez ajouter des requêtes personnalisées ici si nécessaire
}
