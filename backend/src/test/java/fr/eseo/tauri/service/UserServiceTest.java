package fr.eseo.tauri.service;

import fr.eseo.tauri.model.*;
import fr.eseo.tauri.model.enumeration.PermissionType;
import fr.eseo.tauri.model.enumeration.RoleType;
import fr.eseo.tauri.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@Nested
class UserServiceTest {

    @InjectMocks
    UserService userService;

    @Mock
    UserRepository userRepository;

    @Mock
    TeamRepository teamRepository;

    @Mock
    RoleRepository roleRepository;

    @Mock
    PermissionRepository permissionRepository;

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
    }

    /*@Test
     void testDeleteUser() {
        // Arrange
        User user = new User();
        Team team = new Team();
        team.leader(user);
        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        when(teamRepository.findByLeader(user)).thenReturn(List.of(team));

        // Act
        userService.deleteUserById(1);

        // Assert
        verify(userRepository, times(1)).findById(1);
        verify(teamRepository, times(1)).findByLeader(user);
        assertNull(team.leader());
        verify(userRepository, times(1)).deleteById(1);
        assertFalse(userRepository.existsById(1));
    }

    @Test
    void testGetPermissions() {
        // Arrange
        User user = new User();
        //userRepository.save(user);
        Role role = new Role();
        role.type(RoleType.SUPERVISING_STAFF);
        role.user(user);
        //roleRepository.save(role);
        Permission permission1 = new Permission();
        permission1.type(PermissionType.IMPORT);
        permission1.role(RoleType.SUPERVISING_STAFF);
        Permission permission2 = new Permission();
        permission2.type(PermissionType.VIEW_COMMENT);
        permission2.role(RoleType.SUPERVISING_STAFF);
        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        when(roleRepository.findByUser(user)).thenReturn(List.of(role));
        when(permissionRepository.findByRole(RoleType.SUPERVISING_STAFF)).thenReturn(Arrays.asList(permission1, permission2));

        // Act
        List<PermissionType> permissions = userService.getPermissions(1);

        // Assert
        verify(userRepository, times(1)).findById(1);
        verify(roleRepository, times(1)).findByUser(user);
        assertEquals(2, permissions.size());
        assertTrue(permissions.contains(PermissionType.IMPORT));
        assertTrue(permissions.contains(PermissionType.VIEW_COMMENT));
    }

    @Test
     void testHasPermission() {
        // Arrange
        User user = new User();
        Role role = new Role();
        role.type(RoleType.SUPERVISING_STAFF);
        role.user(user);
        Permission permission = new Permission();
        permission.type(PermissionType.IMPORT);
        permission.role(RoleType.SUPERVISING_STAFF);
        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        when(roleRepository.findByUser(user)).thenReturn(List.of(role));
        when(permissionRepository.findByRole(RoleType.SUPERVISING_STAFF)).thenReturn(List.of(permission));

        // Act
        Boolean hasPermission1 = userService.hasPermission(1, PermissionType.IMPORT);
        //Boolean hasPermission2 = userService.hasPermission(1, PermissionType.VIEW_COMMENT);

        // Assert
        assertTrue(hasPermission1);
        //assertFalse(hasPermission2);
    }*/
}

