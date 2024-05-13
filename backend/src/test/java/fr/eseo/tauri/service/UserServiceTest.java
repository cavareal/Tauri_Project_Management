package fr.eseo.tauri.service;

import fr.eseo.tauri.exception.ResourceNotFoundException;
import fr.eseo.tauri.model.User;
import fr.eseo.tauri.repository.TeamRepository;
import fr.eseo.tauri.repository.UserRepository;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@Nested
class UserServiceTest {

    @InjectMocks
    UserService userService;

    @Mock
    UserRepository userRepository;

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

        userService.createUser(token, user);

        verify(userRepository, times(1)).save(user);
    }

    @Test
    void createUserThrowsSecurityExceptionWhenUnauthorized() {
        String token = "validToken";
        User user = new User();

        when(authService.checkAuth(token, "createUser")).thenReturn(false);

        assertThrows(SecurityException.class, () -> userService.createUser(token, user));
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

}

