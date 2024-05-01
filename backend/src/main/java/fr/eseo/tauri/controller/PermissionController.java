package fr.eseo.tauri.controller;

import fr.eseo.tauri.model.Permission;
import fr.eseo.tauri.service.PermissionService;
import fr.eseo.tauri.util.CustomLogger;
import fr.eseo.tauri.util.ResponseMessage;
import fr.eseo.tauri.util.valid.Create;
import fr.eseo.tauri.util.valid.Update;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/permissions")
@Tag(name = "permissions")
public class PermissionController {

    private final PermissionService permissionService;
    private final ResponseMessage responseMessage = new ResponseMessage("permission");

    @GetMapping("/{id}")
    public ResponseEntity<Permission> getPermissionById(@RequestHeader("Authorization") String token, @PathVariable Integer id) {
        Permission permission = permissionService.getPermissionById(token, id);
        return ResponseEntity.ok(permission);
    }

    @GetMapping
    public ResponseEntity<List<Permission>> getAllPermissionsByProject(@RequestHeader("Authorization") String token, @RequestParam("projectId") Integer projectId) {
        List<Permission> permissions = permissionService.getAllPermissions(token);
        return ResponseEntity.ok(permissions);
    }

    @PostMapping
    public ResponseEntity<String> createPermission(@RequestHeader("Authorization") String token, @Validated(Create.class) @RequestBody Permission permission) {
        permissionService.createPermission(token, permission);
        CustomLogger.info(responseMessage.create());
        return ResponseEntity.ok(responseMessage.create());
    }

    @PatchMapping("/{id}")
    public ResponseEntity<String> updatePermission(@RequestHeader("Authorization") String token, @PathVariable Integer id, @Validated(Update.class) @RequestBody Permission updatedPermission) {
        permissionService.updatePermission(token, id, updatedPermission);
        CustomLogger.info(responseMessage.update());
        return ResponseEntity.ok(responseMessage.update());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePermission(@RequestHeader("Authorization") String token, @PathVariable Integer id) {
        permissionService.deletePermission(token, id);
        CustomLogger.info(responseMessage.delete());
        return ResponseEntity.ok(responseMessage.delete());
    }

    @DeleteMapping
    public ResponseEntity<String> deleteAllPermissionsByProject(@RequestHeader("Authorization") String token, @RequestParam("projectId") Integer projectId) {
        permissionService.deleteAllPermissions(token);
        CustomLogger.info(responseMessage.deleteAllFromCurrentProject());
        return ResponseEntity.ok(responseMessage.deleteAllFromCurrentProject());
    }
}