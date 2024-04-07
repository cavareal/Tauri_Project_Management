package fr.eseo.tauri.repository;

import fr.eseo.tauri.model.User;
import fr.eseo.tauri.model.enumeration.RoleType;
import org.springframework.data.jpa.repository.JpaRepository;
import fr.eseo.tauri.model.Role;

import java.util.List;

public interface RoleRepository extends JpaRepository<Role, Integer> {
    List<Role> findByUser(User user);

    List<Role> findByType(RoleType roleType);
}
