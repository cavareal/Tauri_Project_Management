package fr.eseo.tauri.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import fr.eseo.tauri.model.Sprint;

public interface SprintRepository extends JpaRepository<Sprint, Integer> {
    // Vous pouvez ajouter des requêtes personnalisées ici si nécessaire
}
