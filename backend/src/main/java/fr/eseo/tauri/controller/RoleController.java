package fr.eseo.tauri.controller;

import fr.eseo.tauri.model.Role;
import fr.eseo.tauri.model.User;
import fr.eseo.tauri.model.enumeration.PermissionType;
import fr.eseo.tauri.model.enumeration.RoleType;
import fr.eseo.tauri.service.RoleService;
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
@RequestMapping("/api/roles")
@Tag(name = "roles")
public class RoleController {

    private final RoleService roleService;
    private final ResponseMessage responseMessage = new ResponseMessage("role");

    @GetMapping("/{id}")
    public ResponseEntity<Role> getRoleById(@RequestHeader("Authorization") String token, @PathVariable Integer id) {
        Role role = roleService.getRoleById(token, id);
        return ResponseEntity.ok(role);
    }

    @GetMapping
    public ResponseEntity<List<Role>> getAllRoles(@RequestHeader("Authorization") String token) {
        List<Role> roles = roleService.getAllRoles(token);
        return ResponseEntity.ok(roles);
    }

    @PostMapping
    public ResponseEntity<String> createRole(@RequestHeader("Authorization") String token, @Validated(Create.class) @RequestBody Role role) {
        roleService.createRole(token, role);
        CustomLogger.info(responseMessage.create());
        return ResponseEntity.ok(responseMessage.create());
    }

    @PostMapping(path="/{email}")
    public ResponseEntity<String> createRoles(@PathVariable String email, @Validated(Create.class) @RequestBody RoleType[] roles) {
        roleService.createRoles(email, roles);
        CustomLogger.info(responseMessage.create());
        return ResponseEntity.ok(responseMessage.create());
    }

    @PatchMapping("/{id}")
    public ResponseEntity<String> updateRole(@RequestHeader("Authorization") String token, @PathVariable Integer id, @Validated(Update.class) @RequestBody Role updatedRole) {
        roleService.updateRole(token, id, updatedRole);
        CustomLogger.info(responseMessage.update());
        return ResponseEntity.ok(responseMessage.update());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteRoleById(@RequestHeader("Authorization") String token, @PathVariable Integer id) {
        roleService.deleteRoleById(token, id);
        CustomLogger.info(responseMessage.delete());
        return ResponseEntity.ok(responseMessage.delete());
    }

    @GetMapping("/{roleType}/users")
    public ResponseEntity<List<User>> getUsersByRole(@RequestHeader("Authorization") String token, @PathVariable RoleType roleType) {
        var users = roleService.getUsersByRoleType(token, roleType);
        CustomLogger.info("Retrieved users by role type: " + users);
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{roleType}/permissions/{permissionType}")
    public ResponseEntity<Boolean> hasPermission(@RequestHeader("Authorization") String token, @PathVariable RoleType roleType, @PathVariable PermissionType permissionType) {
        var hasPermission = roleService.hasPermission(token, roleType, permissionType);
        return ResponseEntity.ok(hasPermission);
    }

}