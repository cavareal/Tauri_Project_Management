package fr.eseo.tauri.service;

import fr.eseo.tauri.exception.GlobalExceptionHandler;
import fr.eseo.tauri.model.Permission;
import fr.eseo.tauri.model.enumeration.PermissionType;
import fr.eseo.tauri.model.enumeration.RoleType;
import fr.eseo.tauri.exception.ResourceNotFoundException;
import fr.eseo.tauri.repository.PermissionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class PermissionService {

    private final AuthService authService;
    private final PermissionRepository permissionRepository;

    public List<Permission> getAllPermissions(String token) {
        if (!Boolean.TRUE.equals(authService.checkAuth(token, "readPermissions"))) {
            throw new SecurityException(GlobalExceptionHandler.UNAUTHORIZED_ACTION);
        }
        return permissionRepository.findAll();
    }

    public Permission getPermissionById(String token, Integer id) {
        if (!Boolean.TRUE.equals(authService.checkAuth(token, "readPermission"))) {
            throw new SecurityException(GlobalExceptionHandler.UNAUTHORIZED_ACTION);
        }
        return permissionRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("permission", id));
    }

    public void addPermissions(String token, List<Permission> permissions) {
        if (!Boolean.TRUE.equals(authService.checkAuth(token, "addPermission"))) {
            throw new SecurityException(GlobalExceptionHandler.UNAUTHORIZED_ACTION);
        }
        int permissionsNumber = permissionRepository.findAll().size();
        for(Permission permission : permissions) {
            permissionRepository.save(permission);
            if(permissionRepository.findAll().size() == permissionsNumber){
                throw new DataAccessException("Error : Could not add permission") {};
            } else {
                permissionsNumber++;
            }
        }
    }

    public void updatePermission(String token, Integer id, Map<String, Object> permissionDetails) {
        if (Boolean.TRUE.equals(authService.checkAuth(token, "updatePermission"))) {
            Permission permission = getPermissionById(token, id);

            for (Map.Entry<String, Object> entry : permissionDetails.entrySet()) {
                String key = entry.getKey();
                Object value = entry.getValue();

                if (value == null) {
                    continue;
                }

                switch (key) {
                    case "type":
                        permission.type(PermissionType.valueOf((String) value));
                        break;
                    case "role":
                        permission.role(RoleType.valueOf((String) value));
                        break;
                    default:
                        throw new IllegalArgumentException("Invalid key: " + key);
                }
            }

            permissionRepository.save(permission);

        } else {
            throw new SecurityException(GlobalExceptionHandler.UNAUTHORIZED_ACTION);
        }
    }

    public void deleteAllPermissions(String token) {
        if (!Boolean.TRUE.equals(authService.checkAuth(token, "deletePermission"))) {
            throw new SecurityException(GlobalExceptionHandler.UNAUTHORIZED_ACTION);
        }
        permissionRepository.deleteAll();
        if(!permissionRepository.findAll().isEmpty()){
            throw new DataAccessException("Error : Could not delete all permissions") {};
        }
    }

    public void deletePermission(String token, Integer id) {
        if (!Boolean.TRUE.equals(authService.checkAuth(token, "deletePermission"))) {
            throw new SecurityException(GlobalExceptionHandler.UNAUTHORIZED_ACTION);
        }
        getPermissionById(token, id);
        int permissionsNumber = permissionRepository.findAll().size();
        permissionRepository.deleteById(id);
        if(permissionRepository.findAll().size() == permissionsNumber){
            throw new DataAccessException("Error : Could not delete permission with id : " + id) {};
        }
    }
}