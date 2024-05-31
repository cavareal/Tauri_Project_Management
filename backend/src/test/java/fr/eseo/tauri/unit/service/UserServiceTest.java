package fr.eseo.tauri.unit.service;

import fr.eseo.tauri.exception.ResourceNotFoundException;
import fr.eseo.tauri.model.Permission;
import fr.eseo.tauri.model.User;
import fr.eseo.tauri.model.enumeration.PermissionType;
import fr.eseo.tauri.model.enumeration.RoleType;
import fr.eseo.tauri.repository.RoleRepository;
import fr.eseo.tauri.repository.UserRepository;
import fr.eseo.tauri.service.AuthService;
import fr.eseo.tauri.service.PermissionService;
import fr.eseo.tauri.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@Nested
class UserServiceTest {

    @InjectMocks
    UserService userService;

    @Mock
    UserRepository userRepository;

    @Mock
    RoleRepository roleRepository;

    @Mock
    PermissionService permissionService;

    @Mock
    AuthService authService;

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getUserByIdReturnsUserWhenAuthorizedAndUserExists() {
        String token = "validToken";
        Integer id = 1;
        User expectedUser = new User();

        when(authService.checkAuth(token, "readUser")).thenReturn(true);
        when(userRepository.findById(id)).thenReturn(Optional.of(expectedUser));

        User actualUser = userService.getUserById(token, id);

        assertEquals(expectedUser, actualUser);
    }

    @Test
    void getUserByIdThrowsSecurityExceptionWhenUnauthorized() {
        String token = "validToken";
        Integer id = 1;

        when(authService.checkAuth(token, "readUser")).thenReturn(false);

        assertThrows(SecurityException.class, () -> userService.getUserById(token, id));
    }

    @Test
    void getUserByIdThrowsResourceNotFoundExceptionWhenUserDoesNotExist() {
        String token = "validToken";
        Integer id = 1;

        when(authService.checkAuth(token, "readUser")).thenReturn(true);
        when(userRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> userService.getUserById(token, id));
    }

    @Test
    void getAllUsersReturnsAllUsersWhenAuthorized() {
        String token = "validToken";
        List<User> expectedUsers = Arrays.asList(new User(), new User());

        when(authService.checkAuth(token, "readUser")).thenReturn(true);
        when(userRepository.findAll()).thenReturn(expectedUsers);

        List<User> actualUsers = userService.getAllUsers(token);

        assertEquals(expectedUsers, actualUsers);
    }

    @Test
    void getAllUsersThrowsSecurityExceptionWhenUnauthorized() {
        String token = "validToken";

        when(authService.checkAuth(token, "readUser")).thenReturn(false);

        assertThrows(SecurityException.class, () -> userService.getAllUsers(token));
    }

    @Test
    void getAllUsersReturnsEmptyListWhenNoUsers() {
        String token = "validToken";
        List<User> expectedUsers = new ArrayList<>();

        when(authService.checkAuth(token, "readUser")).thenReturn(true);
        when(userRepository.findAll()).thenReturn(expectedUsers);

        List<User> actualUsers = userService.getAllUsers(token);

        assertEquals(expectedUsers, actualUsers);
    }

    @Test
    void createUserSavesUserWhenAuthorized() {
        String token = "validToken";
        User user = new User();

        when(authService.checkAuth(token, "createUser")).thenReturn(true);

        userService.createUser(user);

        verify(userRepository, times(1)).save(user);
    }

    @Test
    void createUserThrowsSecurityExceptionWhenUnauthorized() {
        String token = "validToken";
        User user = new User();

        when(authService.checkAuth(token, "createUser")).thenReturn(false);

        assertThrows(SecurityException.class, () -> userService.createUser(user));
    }

    @Test
    void updateUserThrowsSecurityExceptionWhenUnauthorized() {
        String token = "validToken";
        Integer id = 1;
        User updatedUser = new User();

        when(authService.checkAuth(token, "updateUser")).thenReturn(false);

        assertThrows(SecurityException.class, () -> userService.updateUser(token, id, updatedUser));
    }

    @Test
    void deleteUserByIdThrowsSecurityExceptionWhenUnauthorized() {
        String token = "validToken";
        Integer id = 1;

        when(authService.checkAuth(token, "deleteUser")).thenReturn(false);

        assertThrows(SecurityException.class, () -> userService.deleteUserById(token, id));
    }

    @Test
    void deleteAllUsersDeletesAllUsersWhenAuthorized() {
        String token = "validToken";

        when(authService.checkAuth(token, "deleteUser")).thenReturn(true);

        userService.deleteAllUsers(token);

        verify(userRepository, times(1)).deleteAll();
    }

    @Test
    void deleteAllUsersThrowsSecurityExceptionWhenUnauthorized() {
        String token = "validToken";

        when(authService.checkAuth(token, "deleteUser")).thenReturn(false);

        assertThrows(SecurityException.class, () -> userService.deleteAllUsers(token));
    }

    @Test
    void getRolesByUserIdThrowsSecurityExceptionWhenUnauthorized() {
        String token = "validToken";
        Integer id = 1;

        when(authService.checkAuth(token, "readRoleByUserId")).thenReturn(false);

        assertThrows(SecurityException.class, () -> userService.getRolesByUserId(token, id));
    }

    @Test
    void getTeamByMemberIdThrowsSecurityExceptionWhenUnauthorized() {
        String token = "validToken";
        Integer userId = 1;
        Integer projectId = 1;

        when(authService.checkAuth(token, "readTeamBySupervisor")).thenReturn(false);

        assertThrows(SecurityException.class, () -> userService.getTeamByMemberId(token, userId, projectId));
    }

    @Test
    void getPermissionsByUserThrowsSecurityExceptionWhenUnauthorized() {
        String token = "validToken";
        Integer id = 1;

        when(authService.checkAuth(token, "readPermissions")).thenReturn(false);

        assertThrows(SecurityException.class, () -> userService.getPermissionsByUser(token, id));
    }

    @Test
    void hasPermissionThrowsSecurityExceptionWhenUnauthorized() {
        String token = "validToken";
        Integer id = 1;
        PermissionType permission = PermissionType.ADD_ALL_TEAMS_FEEDBACK;

        when(authService.checkAuth(token, "readPermissions")).thenReturn(false);

        assertThrows(SecurityException.class, () -> userService.hasPermission(token, id, permission));
    }


    @Test
    void getPermissionsByUserShouldThrowSecurityExceptionWhenUnauthorized() {
        String token = "validToken";
        Integer id = 1;

        when(authService.checkAuth(token, "readPermissions")).thenReturn(false);

        assertThrows(SecurityException.class, () -> userService.getPermissionsByUser(token, id));
    }


}

