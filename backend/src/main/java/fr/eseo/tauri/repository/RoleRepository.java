package fr.eseo.tauri.repository;

import fr.eseo.tauri.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import fr.eseo.tauri.model.Role;

import java.util.List;

public interface RoleRepository extends JpaRepository<Role, Integer> {
    List<Role> findByUser(User user);
}
