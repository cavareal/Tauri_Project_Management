package fr.eseo.tauri.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import fr.eseo.tauri.model.Flag;

import java.util.List;

public interface FlagRepository extends JpaRepository<Flag, Integer> {
    // Vous pouvez ajouter des requêtes personnalisées ici si nécessaire

    public List<Flag> findByAuthorIdAndDescription(Integer authorId, String description);
}
