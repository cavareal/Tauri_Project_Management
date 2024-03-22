package fr.eseo.tauri.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import fr.eseo.tauri.model.Team;

public interface TeamRepository extends JpaRepository<Team, Integer> {
    // Vous pouvez ajouter des requêtes personnalisées ici si nécessaire
}
