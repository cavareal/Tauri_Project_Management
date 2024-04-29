package fr.eseo.tauri.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import fr.eseo.tauri.model.Project;
import org.springframework.data.jpa.repository.Query;

public interface ProjectRepository extends JpaRepository<Project, Integer> {
    @Query(value = "SELECT p FROM Project p ORDER BY p.id DESC LIMIT 1")
    Project findLast();
    // Vous pouvez ajouter des requêtes personnalisées ici si nécessaire



    
}
