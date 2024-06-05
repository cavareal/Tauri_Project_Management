package fr.eseo.tauri.unit.service;

import fr.eseo.tauri.exception.ResourceNotFoundException;
import fr.eseo.tauri.model.Permission;
import fr.eseo.tauri.model.Role;
import fr.eseo.tauri.model.User;
import fr.eseo.tauri.model.enumeration.PermissionType;
import fr.eseo.tauri.model.enumeration.RoleType;
import fr.eseo.tauri.repository.RoleRepository;
import fr.eseo.tauri.service.PermissionService;
import fr.eseo.tauri.service.RoleService;
import fr.eseo.tauri.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@Nested
class RoleServiceTest {

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
        Integer id = 1;
        Role role = new Role();
        role.id(id);

        when(roleRepository.findById(id)).thenReturn(Optional.of(role));

        Role result = roleService.getRoleById(id);

        assertEquals(role, result);
    }

    @Test
    void getRoleByIdShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() {
        Integer id = 1;

        when(roleRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> roleService.getRoleById(id));
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
        Role role = new Role();
        role.userId(1);
        User user = new User();
        user.id(1);

        when(userService.getUserById(role.userId())).thenReturn(user);

        roleService.createRole(role);

        verify(roleRepository, times(1)).save(role);
    }

    @Test
    void createRoleShouldSaveRoleWhenAuthorizedAndUserIdIsNull() {
        Role role = new Role();

        roleService.createRole(role);

        verify(roleRepository, times(1)).save(role);
    }

    @Test
    void deleteRoleByIdShouldDeleteRoleWhenAuthorizedAndIdExists() {
        Integer id = 1;
        Role role = new Role();
        role.id(id);

        when(roleRepository.findById(id)).thenReturn(Optional.of(role));

        roleService.deleteRoleById(id);

        verify(roleRepository, times(1)).deleteById(id);
    }

    @Test
    void deleteRoleByIdShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() {
        Integer id = 1;

        when(roleRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> roleService.deleteRoleById(id));
    }

    @Test
    void deleteAllRolesShouldDeleteAllRolesWhenAuthorized() {
        roleService.deleteAllRoles();

        verify(roleRepository, times(1)).deleteAll();
    }

    @Test
    void hasPermissionShouldReturnFalseWhenPermissionDoesNotExist() {
        RoleType roleType = RoleType.OPTION_LEADER;
        PermissionType permissionType = PermissionType.ADD_GRADE_COMMENT;
        List<Permission> permissions = List.of(new Permission());

        when(permissionService.getAllPermissionsByRole(roleType)).thenReturn(permissions);

        Boolean result = roleService.hasPermission(roleType, permissionType);

        assertFalse(result);
    }

}
