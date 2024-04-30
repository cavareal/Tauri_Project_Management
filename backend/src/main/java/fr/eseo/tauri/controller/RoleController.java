package fr.eseo.tauri.controller;

import fr.eseo.tauri.model.Role;
import fr.eseo.tauri.model.User;
import fr.eseo.tauri.model.enumeration.RoleType;
import fr.eseo.tauri.repository.RoleRepository;
import fr.eseo.tauri.service.RoleService;
import fr.eseo.tauri.util.CustomLogger;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/roles")
@Tag(name = "roles")
public class RoleController {

    private final RoleService roleService;
    private final RoleRepository roleRepository;

    @GetMapping
    public ResponseEntity<List<Role>> getAllRoles(@RequestHeader("Authorization") String token) {
        List<Role> roles = roleService.getAllRoles(token);
        return ResponseEntity.ok(roles);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Role> getRoleById(@RequestHeader("Authorization") String token, @PathVariable Integer id) {
        Role role = roleService.getRoleById(token, id);
        return ResponseEntity.ok(role);
    }

    @PostMapping
    public ResponseEntity<String> addRoles(@RequestHeader("Authorization") String token, @RequestBody List<Role> roles) {
        roleService.addRoles(token, roles);
        CustomLogger.logInfo("The role(s) have been added");
        return ResponseEntity.ok("The role(s) have been added");
    }

    @PatchMapping("/{id}")
    public ResponseEntity<String> updateRole(@RequestHeader("Authorization") String token, @PathVariable Integer id, @RequestBody Map<String, Object> request) {
        roleService.updateRole(token, id, request);
        CustomLogger.logInfo("The role has been updated");
        return ResponseEntity.ok("The role has been updated");
    }

    @DeleteMapping
    public ResponseEntity<String> deleteAllRoles(@RequestHeader("Authorization") String token) {
        roleService.deleteAllRoles(token);
        CustomLogger.logInfo("All the roles have been deleted");
        return ResponseEntity.ok("All the roles have been deleted");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteRole(@RequestHeader("Authorization") String token, @PathVariable Integer id) {
        roleService.deleteRole(token, id);
        CustomLogger.logInfo("The role has been deleted");
        return ResponseEntity.ok("The role has been deleted");
    }


    @GetMapping("/type/{type}")
    public User getFirstUserByRoleType(@PathVariable RoleType type) {;
        return roleRepository.findFirstByType(type).user();
    }
}
