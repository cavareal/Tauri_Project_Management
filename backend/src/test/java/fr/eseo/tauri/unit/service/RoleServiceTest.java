package fr.eseo.tauri.unit.service;

import fr.eseo.tauri.exception.ResourceNotFoundException;
import fr.eseo.tauri.model.Permission;
import fr.eseo.tauri.model.Role;
import fr.eseo.tauri.model.User;
import fr.eseo.tauri.model.enumeration.PermissionType;
import fr.eseo.tauri.model.enumeration.RoleType;
import fr.eseo.tauri.repository.RoleRepository;
import fr.eseo.tauri.service.AuthService;
import fr.eseo.tauri.service.PermissionService;
import fr.eseo.tauri.service.RoleService;
import fr.eseo.tauri.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@Nested
class RoleServiceTest {

    @Mock
    private AuthService authService;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private UserService userService;

    @Mock
    private PermissionService permissionService;

    @InjectMocks
    private RoleService roleService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getRoleByIdShouldReturnRoleWhenAuthorizedAndIdExists() {
        String token = "validToken";
        Integer id = 1;
        Role role = new Role();
        role.id(id);

        when(authService.checkAuth(token, "readRole")).thenReturn(true);
        when(roleRepository.findById(id)).thenReturn(Optional.of(role));

        Role result = roleService.getRoleById(token, id);

        assertEquals(role, result);
    }

    @Test
    void getRoleByIdShouldThrowSecurityExceptionWhenUnauthorized() {
        String token = "validToken";
        Integer id = 1;

        when(authService.checkAuth(token, "readRole")).thenReturn(false);

        assertThrows(SecurityException.class, () -> roleService.getRoleById(token, id));
    }

    @Test
    void getRoleByIdShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() {
        String token = "validToken";
        Integer id = 1;

        when(authService.checkAuth(token, "readRole")).thenReturn(true);
        when(roleRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> roleService.getRoleById(token, id));
    }

/*    @Test
    void getAllRolesShouldReturnAllRolesWhenAuthorized() {
        String token = "validToken";
        List<Role> roles = Arrays.asList(new Role(), new Role());

        when(authService.checkAuth(token, "readRoles")).thenReturn(true);
        when(roleRepository.findAll()).thenReturn(roles);

        List<Role> result = roleService.getAllRoles(token);

        assertEquals(roles, result);
    }*/

    @Test
    void getAllRolesShouldThrowSecurityExceptionWhenUnauthorized() {
        String token = "validToken";

        when(authService.checkAuth(token, "readRoles")).thenReturn(false);

        assertThrows(SecurityException.class, () -> roleService.getAllRoles(token));
    }

/*    @Test
    void getAllRolesShouldHandleNoRoles() {
        String token = "validToken";
        List<Role> roles = Collections.emptyList();

        when(authService.checkAuth(token, "readRoles")).thenReturn(true);
        when(roleRepository.findAll()).thenReturn(roles);

        List<Role> result = roleService.getAllRoles(token);

        assertEquals(roles, result);
    }*/

    @Test
    void createRoleShouldSaveRoleWhenAuthorizedAndUserIdExists() {
        String token = "validToken";
        Role role = new Role();
        role.userId(1);
        User user = new User();
        user.id(1);

        when(authService.checkAuth(token, "addRole")).thenReturn(true);
        when(userService.getUserById(token, role.userId())).thenReturn(user);

        roleService.createRole(token, role);

        verify(roleRepository, times(1)).save(role);
    }

    @Test
    void createRoleShouldSaveRoleWhenAuthorizedAndUserIdIsNull() {
        String token = "validToken";
        Role role = new Role();

        when(authService.checkAuth(token, "addRole")).thenReturn(true);

        roleService.createRole(token, role);

        verify(roleRepository, times(1)).save(role);
    }

    @Test
    void createRoleShouldThrowSecurityExceptionWhenUnauthorized() {
        String token = "validToken";
        Role role = new Role();

        when(authService.checkAuth(token, "addRole")).thenReturn(false);

        assertThrows(SecurityException.class, () -> roleService.createRole(token, role));
    }

    @Test
    void deleteRoleByIdShouldDeleteRoleWhenAuthorizedAndIdExists() {
        String token = "validToken";
        Integer id = 1;
        Role role = new Role();
        role.id(id);

        when(authService.checkAuth(token, "deleteRole")).thenReturn(true);
        when(authService.checkAuth(token, "readRole")).thenReturn(true);
        when(roleRepository.findById(id)).thenReturn(Optional.of(role));

        roleService.deleteRoleById(token, id);

        verify(roleRepository, times(1)).deleteById(id);
    }

    @Test
    void deleteRoleByIdShouldThrowSecurityExceptionWhenUnauthorized() {
        String token = "validToken";
        Integer id = 1;

        when(authService.checkAuth(token, "deleteRole")).thenReturn(false);

        assertThrows(SecurityException.class, () -> roleService.deleteRoleById(token, id));
    }

    @Test
    void deleteRoleByIdShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() {
        String token = "validToken";
        Integer id = 1;

        when(authService.checkAuth(token, "deleteRole")).thenReturn(true);
        when(authService.checkAuth(token, "readRole")).thenReturn(true);
        when(roleRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> roleService.deleteRoleById(token, id));
    }

    @Test
    void deleteAllRolesShouldDeleteAllRolesWhenAuthorized() {
        String token = "validToken";

        when(authService.checkAuth(token, "deleteRole")).thenReturn(true);

        roleService.deleteAllRoles(token);

        verify(roleRepository, times(1)).deleteAll();
    }

    @Test
    void deleteAllRolesShouldThrowSecurityExceptionWhenUnauthorized() {
        String token = "validToken";

        when(authService.checkAuth(token, "deleteRole")).thenReturn(false);

        assertThrows(SecurityException.class, () -> roleService.deleteAllRoles(token));
    }

    @Test
    void getUsersByRoleTypeShouldThrowSecurityExceptionWhenUnauthorized() {
        String token = "validToken";
        RoleType roleType = RoleType.OPTION_LEADER;

        when(authService.checkAuth(token, "deleteRole")).thenReturn(false);

        assertThrows(SecurityException.class, () -> roleService.getUsersByRoleType(token, roleType));
    }

    @Test
    void hasPermissionShouldReturnFalseWhenPermissionDoesNotExist() {
        String token = "validToken";
        RoleType roleType = RoleType.OPTION_LEADER;
        PermissionType permissionType = PermissionType.ADD_GRADE_COMMENT;
        List<Permission> permissions = List.of(new Permission());

        when(authService.checkAuth(token, "deleteRole")).thenReturn(true);
        when(permissionService.getAllPermissionsByRole(roleType)).thenReturn(permissions);

        Boolean result = roleService.hasPermission(token, roleType, permissionType);

        assertFalse(result);
    }

}
