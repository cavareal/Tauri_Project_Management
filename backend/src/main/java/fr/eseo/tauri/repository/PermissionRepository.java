package fr.eseo.tauri.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import fr.eseo.tauri.model.Permission;

public interface PermissionRepository extends JpaRepository<Permission, Integer> {
    // Vous pouvez ajouter des requêtes personnalisées ici si nécessaire
}
