package fr.eseo.tauri.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import fr.eseo.tauri.model.Flag;

public interface FlagRepository extends JpaRepository<Flag, Integer> {
    // Vous pouvez ajouter des requêtes personnalisées ici si nécessaire
}
