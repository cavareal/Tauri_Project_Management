package fr.eseo.tauri.repository;

import fr.eseo.tauri.model.enumeration.RoleType;
import org.springframework.data.jpa.repository.JpaRepository;
import fr.eseo.tauri.model.Permission;

import java.util.List;

public interface PermissionRepository extends JpaRepository<Permission, Integer> {
    List<Permission> findByRole(RoleType role);
}
