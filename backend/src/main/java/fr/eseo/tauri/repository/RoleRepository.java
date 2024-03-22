package fr.eseo.tauri.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import fr.eseo.tauri.model.Role;

public interface RoleRepository extends JpaRepository<Role, Integer> {
    // Vous pouvez ajouter des requêtes personnalisées ici si nécessaire
}
