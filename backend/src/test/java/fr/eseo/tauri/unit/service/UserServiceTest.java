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

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getUserByIdReturnsUserWhenAuthorizedAndUserExists() {
        Integer id = 1;
        User expectedUser = new User();

        when(userRepository.findById(id)).thenReturn(Optional.of(expectedUser));

        User actualUser = userService.getUserById(id);

        assertEquals(expectedUser, actualUser);
    }

    @Test
    void getUserByIdThrowsResourceNotFoundExceptionWhenUserDoesNotExist() {
        Integer id = 1;

        when(userRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> userService.getUserById(id));
    }

    @Test
    void getAllUsersReturnsAllUsersWhenAuthorized() {
        List<User> expectedUsers = Arrays.asList(new User(), new User());

        when(userRepository.findAll()).thenReturn(expectedUsers);

        List<User> actualUsers = userService.getAllUsers();

        assertEquals(expectedUsers, actualUsers);
    }

    @Test
    void getAllUsersReturnsEmptyListWhenNoUsers() {
        List<User> expectedUsers = new ArrayList<>();

        when(userRepository.findAll()).thenReturn(expectedUsers);

        List<User> actualUsers = userService.getAllUsers();

        assertEquals(expectedUsers, actualUsers);
    }

    @Test
    void createUserSavesUserWhenAuthorized() throws Exception {
        User user = new User();

        userService.createUser(user);

        verify(userRepository, times(1)).save(user);
    }

//    @Test
//    void createUserThrowsSecurityExceptionWhenUnauthorized() {
//        String token = "validToken";
//        User user = new User();
//
//        when(authService.checkAuth(token, "createUser")).thenReturn(false);
//
//        assertThrows(SecurityException.class, () -> userService.createUser(user));
//    }

    @Test
    void deleteAllUsersDeletesAllUsersWhenAuthorized() {
        userService.deleteAllUsers();

        verify(userRepository, times(1)).deleteAll();
    }

}

