package fr.eseo.tauri.unit.service;

import fr.eseo.tauri.exception.ResourceNotFoundException;
import fr.eseo.tauri.model.Notification;
import fr.eseo.tauri.model.User;
import fr.eseo.tauri.repository.NotificationRepository;
import fr.eseo.tauri.service.AuthService;
import fr.eseo.tauri.service.NotificationService;
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
class NotificationServiceTest {

    @Mock
    private AuthService authService;

    @Mock
    private NotificationRepository notificationRepository;

    @Mock
    private UserService userService;

    @InjectMocks
    private NotificationService notificationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getNotificationByIdShouldReturnNotificationWhenAuthorizedAndIdExists() {
        String token = "validToken";
        Integer id = 1;
        Notification notification = new Notification();
        notification.id(id);

        when(authService.checkAuth(token, "readNotification")).thenReturn(true);
        when(notificationRepository.findById(id)).thenReturn(Optional.of(notification));

        Notification result = notificationService.getNotificationById(token, id);

        assertEquals(notification, result);
    }

    @Test
    void getNotificationByIdShouldThrowSecurityExceptionWhenUnauthorized() {
        String token = "validToken";
        Integer id = 1;

        when(authService.checkAuth(token, "readNotification")).thenReturn(false);

        assertThrows(SecurityException.class, () -> notificationService.getNotificationById(token, id));
    }

    @Test
    void getNotificationByIdShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() {
        String token = "validToken";
        Integer id = 1;

        when(authService.checkAuth(token, "readNotification")).thenReturn(true);
        when(notificationRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> notificationService.getNotificationById(token, id));
    }

    @Test
    void getAllNotificationsShouldReturnNotificationsWhenAuthorized() {
        String token = "validToken";
        List<Notification> notifications = Arrays.asList(new Notification(), new Notification());

        when(authService.checkAuth(token, "readNotifications")).thenReturn(true);
        when(notificationRepository.findAll()).thenReturn(notifications);

        List<Notification> result = notificationService.getAllNotifications(token);

        assertEquals(notifications, result);
    }

    @Test
    void getAllNotificationsShouldThrowSecurityExceptionWhenUnauthorized() {
        String token = "validToken";

        when(authService.checkAuth(token, "readNotifications")).thenReturn(false);

        assertThrows(SecurityException.class, () -> notificationService.getAllNotifications(token));
    }

    @Test
    void getAllNotificationsShouldReturnEmptyListWhenNoNotifications() {
        String token = "validToken";

        when(authService.checkAuth(token, "readNotifications")).thenReturn(true);
        when(notificationRepository.findAll()).thenReturn(Collections.emptyList());

        List<Notification> result = notificationService.getAllNotifications(token);

        assertTrue(result.isEmpty());
    }

    @Test
    void createNotificationShouldSaveNotificationWhenAuthorized() {
        String token = "validToken";
        Notification notification = new Notification();
        notification.userFromId(1);
        notification.userToId(2);

        when(authService.checkAuth(token, "addNotification")).thenReturn(true);
        when(userService.getUserById(token, notification.userFromId())).thenReturn(new User());
        when(userService.getUserById(token, notification.userToId())).thenReturn(new User());

        notificationService.createNotification(token, notification);

        verify(notificationRepository, times(1)).save(notification);
    }

    @Test
    void createNotificationShouldThrowSecurityExceptionWhenUnauthorized() {
        String token = "validToken";
        Notification notification = new Notification();

        when(authService.checkAuth(token, "addNotification")).thenReturn(false);

        assertThrows(SecurityException.class, () -> notificationService.createNotification(token, notification));
    }


    @Test
    void updateNotificationShouldThrowSecurityExceptionWhenUnauthorized() {
        String token = "validToken";
        Integer id = 1;
        Notification updatedNotification = new Notification();

        when(authService.checkAuth(token, "updateNotification")).thenReturn(false);

        assertThrows(SecurityException.class, () -> notificationService.updateNotification(token, id, updatedNotification));
    }

    @Test
    void deleteNotificationByIdShouldThrowSecurityExceptionWhenUnauthorized() {
        String token = "validToken";
        Integer id = 1;

        when(authService.checkAuth(token, "deleteNotification")).thenReturn(false);

        assertThrows(SecurityException.class, () -> notificationService.deleteNotificationById(token, id));
    }

    @Test
    void deleteAllNotificationsShouldDeleteAllNotificationsWhenAuthorized() {
        String token = "validToken";

        when(authService.checkAuth(token, "deleteNotification")).thenReturn(true);

        notificationService.deleteAllNotifications(token);

        verify(notificationRepository, times(1)).deleteAll();
    }

    @Test
    void deleteAllNotificationsShouldThrowSecurityExceptionWhenUnauthorized() {
        String token = "validToken";

        when(authService.checkAuth(token, "deleteNotification")).thenReturn(false);

        assertThrows(SecurityException.class, () -> notificationService.deleteAllNotifications(token));
    }
    @Test
    void getNotificationsByUserShouldReturnNotificationsWhenAuthorizedAndNotificationsExist() {
        String token = "validToken";
        int userId = 1;
        List<Notification> notifications = Arrays.asList(new Notification(), new Notification());

        when(authService.checkAuth(token, "readNotification")).thenReturn(true);
        when(notificationRepository.findByUser(userId)).thenReturn(notifications);

        List<Notification> result = notificationService.getNotificationsByUser(token, userId);

        assertEquals(notifications, result);
    }

    @Test
    void getNotificationsByUserShouldThrowSecurityExceptionWhenUnauthorized() {
        String token = "validToken";
        Integer userId = 1;

        when(authService.checkAuth(token, "readNotification")).thenReturn(false);

        assertThrows(SecurityException.class, () -> notificationService.getNotificationsByUser(token, userId));
    }


    @Test
    void changeCheckedNotificationShouldThrowSecurityExceptionWhenUnauthorized() {
        String token = "validToken";
        Integer id = 1;

        when(authService.checkAuth(token, "updateNotification")).thenReturn(false);

        assertThrows(SecurityException.class, () -> notificationService.changeCheckedNotification(token, id));
    }


}
