package fr.eseo.tauri.controller;

import fr.eseo.tauri.model.Permission;
import fr.eseo.tauri.repository.PermissionRepository;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/permissions")
@Tag(name = "permissions")
public class PermissionController {

    private final PermissionRepository permissionRepository;

    @PostMapping("/")
    public Permission addPermission(@RequestBody Permission permission) {
        return permissionRepository.save(permission);
    }

    @GetMapping("/")
    public Iterable<Permission> getAllPermissions() {
        return permissionRepository.findAll();
    }

    @GetMapping("/{id}")
    public Permission getPermissionById(@PathVariable Integer id) {
        return permissionRepository.findById(id).orElse(null);
    }

    @PutMapping("/{id}")
    public Permission updatePermission(@PathVariable Integer id, @RequestBody Permission permissionDetails) {
        Permission permission = permissionRepository.findById(id).orElse(null);
        if (permission != null) {
            permission.type (permissionDetails.type());
            // Si vous avez un champ Role, vous pouvez également mettre à jour ici
            return permissionRepository.save(permission);
        }
        return null;
    }

    @DeleteMapping("/{id}")
    public String deletePermission(@PathVariable Integer id) {
        permissionRepository.deleteById(id);
        return "Permission deleted";
    }
}
