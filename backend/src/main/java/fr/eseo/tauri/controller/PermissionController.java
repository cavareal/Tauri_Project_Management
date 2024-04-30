package fr.eseo.tauri.controller;

import fr.eseo.tauri.model.Permission;
import fr.eseo.tauri.service.PermissionService;
import fr.eseo.tauri.util.CustomLogger;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/permissions")
@Tag(name = "permissions")
public class PermissionController {

    private final PermissionService permissionService;

    @GetMapping
    public ResponseEntity<List<Permission>> getAllPermissions(@RequestHeader("Authorization") String token) {
        List<Permission> permissions = permissionService.getAllPermissions(token);
        return ResponseEntity.ok(permissions);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Permission> getPermissionById(@RequestHeader("Authorization") String token, @PathVariable Integer id) {
        Permission permission = permissionService.getPermissionById(token, id);
        return ResponseEntity.ok(permission);
    }

    @PostMapping
    public ResponseEntity<String> addPermissions(@RequestHeader("Authorization") String token, @RequestBody List<Permission> permissions) {
        permissionService.addPermissions(token, permissions);
        CustomLogger.logInfo("The permission(s) have been added");
        return ResponseEntity.ok("The permission(s) have been added");
    }

    @PatchMapping("/{id}")
    public ResponseEntity<String> updatePermission(@RequestHeader("Authorization") String token, @PathVariable Integer id, @RequestBody Map<String, Object> request) {
        permissionService.updatePermission(token, id, request);
        CustomLogger.logInfo("The permission has been updated");
        return ResponseEntity.ok("The permission has been updated");
    }

    @DeleteMapping
    public ResponseEntity<String> deleteAllPermissions(@RequestHeader("Authorization") String token) {
        permissionService.deleteAllPermissions(token);
        CustomLogger.logInfo("All the permissions have been deleted");
        return ResponseEntity.ok("All the permissions have been deleted");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePermission(@RequestHeader("Authorization") String token, @PathVariable Integer id) {
        permissionService.deletePermission(token, id);
        CustomLogger.logInfo("The permission has been deleted");
        return ResponseEntity.ok("The permission has been deleted");
    }
}