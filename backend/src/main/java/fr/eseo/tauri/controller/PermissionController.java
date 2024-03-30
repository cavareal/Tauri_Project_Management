package fr.eseo.tauri.controller;

import fr.eseo.tauri.model.Permission;
import fr.eseo.tauri.repository.PermissionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/permissions")
public class PermissionController {

    private final PermissionRepository permissionRepository;

    @Autowired
    public PermissionController(PermissionRepository permissionRepository) {
        this.permissionRepository = permissionRepository;
    }

    @PostMapping("/add")
    public Permission addPermission(@RequestBody Permission permission) {
        return permissionRepository.save(permission);
    }

    @GetMapping("/all")
    public Iterable<Permission> getAllPermissions() {
        return permissionRepository.findAll();
    }

    @GetMapping("/{id}")
    public Permission getPermissionById(@PathVariable Integer id) {
        return permissionRepository.findById(id).orElse(null);
    }

    @PutMapping("/update/{id}")
    public Permission updatePermission(@PathVariable Integer id, @RequestBody Permission permissionDetails) {
        Permission permission = permissionRepository.findById(id).orElse(null);
        if (permission != null) {
            permission.type(permissionDetails.type());
            // Si vous avez un champ Role, vous pouvez également mettre à jour ici
            return permissionRepository.save(permission);
        }
        return null;
    }

    @DeleteMapping("/delete/{id}")
    public String deletePermission(@PathVariable Integer id) {
        permissionRepository.deleteById(id);
        return "Permission deleted";
    }
}
