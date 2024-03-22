package fr.eseo.tauri.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import fr.eseo.tauri.model.ValidationBonus;

public interface ValidationBonusRepository extends JpaRepository<ValidationBonus, Integer> {
    // Vous pouvez ajouter des requêtes personnalisées ici si nécessaire
}
