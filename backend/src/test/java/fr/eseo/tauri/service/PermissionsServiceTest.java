package fr.eseo.tauri.service;

import fr.eseo.tauri.exception.ResourceNotFoundException;
import fr.eseo.tauri.model.Permission;
import fr.eseo.tauri.model.enumeration.RoleType;
import fr.eseo.tauri.repository.PermissionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@Nested
class PermissionsServiceTest {

    @Mock
    private AuthService authService;

    @Mock
    private PermissionRepository permissionRepository;

    @InjectMocks
    private PermissionService permissionService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getPermissionByIdShouldReturnPermissionWhenAuthorizedAndIdExists() {
        String token = "validToken";
        Integer id = 1;
        Permission permission = new Permission();
        permission.id(id);

        when(authService.checkAuth(token, "readPermission")).thenReturn(true);
        when(permissionRepository.findById(id)).thenReturn(Optional.of(permission));

        Permission result = permissionService.getPermissionById(token, id);

        assertEquals(permission, result);
    }

    @Test
    void getPermissionByIdShouldThrowSecurityExceptionWhenUnauthorized() {
        String token = "validToken";
        Integer id = 1;

        when(authService.checkAuth(token, "readPermission")).thenReturn(false);

        assertThrows(SecurityException.class, () -> permissionService.getPermissionById(token, id));
    }

    @Test
    void getPermissionByIdShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() {
        String token = "validToken";
        Integer id = 1;

        when(authService.checkAuth(token, "readPermission")).thenReturn(true);
        when(permissionRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> permissionService.getPermissionById(token, id));
    }

    @Test
    void getAllPermissionsShouldReturnAllPermissionsWhenAuthorized() {
        String token = "validToken";
        List<Permission> permissions = Arrays.asList(new Permission(), new Permission());

        when(authService.checkAuth(token, "readPermissions")).thenReturn(true);
        when(permissionRepository.findAll()).thenReturn(permissions);

        List<Permission> result = permissionService.getAllPermissions(token);

        assertEquals(permissions, result);
    }

    @Test
    void getAllPermissionsShouldThrowSecurityExceptionWhenUnauthorized() {
        String token = "validToken";

        when(authService.checkAuth(token, "readPermissions")).thenReturn(false);

        assertThrows(SecurityException.class, () -> permissionService.getAllPermissions(token));
    }

    @Test
    void getAllPermissionsByRoleShouldReturnPermissionsWhenAuthorizedAndRoleExists() {
        String token = "validToken";
        RoleType roleType = RoleType.SUPERVISING_STAFF;
        List<Permission> permissions = Arrays.asList(new Permission(), new Permission());

        when(authService.checkAuth(token, "readPermissions")).thenReturn(true);
        when(permissionRepository.findByRole(roleType)).thenReturn(permissions);

        List<Permission> result = permissionService.getAllPermissionsByRole(token, roleType);

        assertEquals(permissions, result);
    }

    @Test
    void getAllPermissionsByRoleShouldThrowSecurityExceptionWhenUnauthorized() {
        String token = "validToken";
        RoleType roleType = RoleType.SUPERVISING_STAFF;

        when(authService.checkAuth(token, "readPermissions")).thenReturn(false);

        assertThrows(SecurityException.class, () -> permissionService.getAllPermissionsByRole(token, roleType));
    }

    @Test
    void getAllPermissionsByRoleShouldReturnEmptyListWhenNoPermissionsForRole() {
        String token = "validToken";
        RoleType roleType = RoleType.SUPERVISING_STAFF;

        when(authService.checkAuth(token, "readPermissions")).thenReturn(true);
        when(permissionRepository.findByRole(roleType)).thenReturn(new ArrayList<>());

        List<Permission> result = permissionService.getAllPermissionsByRole(token, roleType);

        assertTrue(result.isEmpty());
    }

    @Test
    void createPermissionShouldSavePermissionWhenAuthorized() {
        String token = "validToken";
        Permission permission = new Permission();

        when(authService.checkAuth(token, "addPermission")).thenReturn(true);

        permissionService.createPermission(token, permission);

        verify(permissionRepository, times(1)).save(permission);
    }

    @Test
    void createPermissionShouldThrowSecurityExceptionWhenUnauthorized() {
        String token = "validToken";
        Permission permission = new Permission();

        when(authService.checkAuth(token, "addPermission")).thenReturn(false);

        assertThrows(SecurityException.class, () -> permissionService.createPermission(token, permission));
    }

    @Test
    void deletePermissionShouldDeletePermissionWhenAuthorizedAndIdExists() {
        String token = "validToken";
        Integer id = 1;
        Permission permission = new Permission();
        permission.id(id);

        when(authService.checkAuth(token, "deletePermission")).thenReturn(true);
        when(authService.checkAuth(token, "readPermission")).thenReturn(true);
        when(permissionRepository.findById(id)).thenReturn(Optional.of(permission));

        permissionService.deletePermission(token, id);

        verify(permissionRepository, times(1)).deleteById(id);
    }

    @Test
    void deletePermissionShouldThrowSecurityExceptionWhenUnauthorized() {
        String token = "validToken";
        Integer id = 1;

        when(authService.checkAuth(token, "deletePermission")).thenReturn(false);

        assertThrows(SecurityException.class, () -> permissionService.deletePermission(token, id));
    }

    @Test
    void deletePermissionShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() {
        String token = "validToken";
        Integer id = 1;

        when(authService.checkAuth(token, "deletePermission")).thenReturn(true);
        when(authService.checkAuth(token, "readPermission")).thenReturn(true);
        when(permissionRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> permissionService.deletePermission(token, id));
    }

    @Test
    void deleteAllPermissionsShouldDeleteAllPermissionsWhenAuthorized() {
        String token = "validToken";

        when(authService.checkAuth(token, "deletePermission")).thenReturn(true);
        when(authService.checkAuth(token, "readPermission")).thenReturn(true);

        permissionService.deleteAllPermissions(token);

        verify(permissionRepository, times(1)).deleteAll();
    }

    @Test
    void deleteAllPermissionsShouldThrowSecurityExceptionWhenUnauthorized() {
        String token = "validToken";

        when(authService.checkAuth(token, "deletePermission")).thenReturn(false);
        when(authService.checkAuth(token, "readPermission")).thenReturn(true);

        assertThrows(SecurityException.class, () -> permissionService.deleteAllPermissions(token));
    }
}
