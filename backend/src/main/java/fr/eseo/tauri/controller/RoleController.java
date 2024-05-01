package fr.eseo.tauri.controller;

import fr.eseo.tauri.model.Role;
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

    @DeleteMapping
    public ResponseEntity<String> deleteAllRoles(@RequestHeader("Authorization") String token) {
        roleService.deleteAllRoles(token);
        CustomLogger.info(responseMessage.deleteAll());
        return ResponseEntity.ok(responseMessage.deleteAll());
    }
}