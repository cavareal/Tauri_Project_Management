package fr.eseo.tauri.service;

import fr.eseo.tauri.exception.GlobalExceptionHandler;
import fr.eseo.tauri.model.Permission;
import fr.eseo.tauri.exception.ResourceNotFoundException;
import fr.eseo.tauri.repository.PermissionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PermissionService {

    private final AuthService authService;
    private final PermissionRepository permissionRepository;
    private final UserService userService;
    private final SprintService sprintService;

    public Permission getPermissionById(String token, Integer id) {
        if (!Boolean.TRUE.equals(authService.checkAuth(token, "readPermission"))) {
            throw new SecurityException(GlobalExceptionHandler.UNAUTHORIZED_ACTION);
        }
        return permissionRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("permission", id));
    }

    public List<Permission> getAllPermissions(String token) {
        if (!Boolean.TRUE.equals(authService.checkAuth(token, "readPermissions"))) {
            throw new SecurityException(GlobalExceptionHandler.UNAUTHORIZED_ACTION);
        }
        return permissionRepository.findAll();
    }

    public void createPermission(String token, Permission permission) {
        if (!Boolean.TRUE.equals(authService.checkAuth(token, "addPermission"))) {
            throw new SecurityException(GlobalExceptionHandler.UNAUTHORIZED_ACTION);
        }
        permissionRepository.save(permission);
    }

    public void updatePermission(String token, Integer id, Permission updatedPermission) {
        if (!Boolean.TRUE.equals(authService.checkAuth(token, "updatePermission"))) {
            throw new SecurityException(GlobalExceptionHandler.UNAUTHORIZED_ACTION);
        }

        Permission permission = getPermissionById(token, id);

        if (updatedPermission.type() != null) permission.type(updatedPermission.type());
        if (updatedPermission.role() != null) permission.role(updatedPermission.role());

        permissionRepository.save(permission);
    }

    public void deletePermission(String token, Integer id) {
        if (!Boolean.TRUE.equals(authService.checkAuth(token, "deletePermission"))) {
            throw new SecurityException(GlobalExceptionHandler.UNAUTHORIZED_ACTION);
        }
        getPermissionById(token, id);
        permissionRepository.deleteById(id);
    }

    public void deleteAllPermissions(String token) {
        if (!Boolean.TRUE.equals(authService.checkAuth(token, "deletePermission"))) {
            throw new SecurityException(GlobalExceptionHandler.UNAUTHORIZED_ACTION);
        }
        permissionRepository.deleteAll();
    }
}