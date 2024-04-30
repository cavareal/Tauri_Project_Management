package fr.eseo.tauri.service;

import fr.eseo.tauri.exception.GlobalExceptionHandler;
import fr.eseo.tauri.model.Notification;
import fr.eseo.tauri.exception.ResourceNotFoundException;
import fr.eseo.tauri.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationService {

	private final AuthService authService;
	private final NotificationRepository notificationRepository;
	private final UserService userService;

	public Notification getNotificationById(String token, Integer id) {
		if (!Boolean.TRUE.equals(authService.checkAuth(token, "readNotification"))) {
			throw new SecurityException(GlobalExceptionHandler.UNAUTHORIZED_ACTION);
		}

		return notificationRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("notification", id));
	}

	public List<Notification> getAllNotifications(String token) {
		if (!Boolean.TRUE.equals(authService.checkAuth(token, "readNotifications"))) {
			throw new SecurityException(GlobalExceptionHandler.UNAUTHORIZED_ACTION);
		}

		return notificationRepository.findAll();
	}

	public void createNotification(String token, Notification notification) {
		if (!Boolean.TRUE.equals(authService.checkAuth(token, "addNotification"))) {
			throw new SecurityException(GlobalExceptionHandler.UNAUTHORIZED_ACTION);
		}

		notificationRepository.save(notification);
	}

	public void createManyNotifications(String token, List<Notification> notifications) {
		notifications.forEach(notification -> createNotification(token, notification));
	}

	public void updateNotification(String token, Integer id, Notification notification) {
		if (!Boolean.TRUE.equals(authService.checkAuth(token, "updateNotification"))) {
			throw new SecurityException(GlobalExceptionHandler.UNAUTHORIZED_ACTION);
		}

		var oldNotification = getNotificationById(token, id);

		if (notification.message() != null) oldNotification.message(notification.message());
		if (notification.isRead() != null) oldNotification.isRead(notification.isRead());
		if (notification.userToId() != null) oldNotification.userTo(userService.getUserById(notification.userToId()));
		if (notification.userFromId() != null) oldNotification.userFrom(userService.getUserById(notification.userFromId()));

		notificationRepository.save(oldNotification);
	}

	public void deleteNotificationById(String token, Integer id) {
		if (!Boolean.TRUE.equals(authService.checkAuth(token, "deleteNotification"))) {
			throw new SecurityException(GlobalExceptionHandler.UNAUTHORIZED_ACTION);
		}

		getNotificationById(token, id);
		notificationRepository.deleteById(id);

		if (notificationRepository.findById(id).isPresent()) {
			throw new DataAccessException("Error : Could not delete notification with id " + id) {};
		}
	}

	public void deleteAllNotifications(String token) {
		if (!Boolean.TRUE.equals(authService.checkAuth(token, "deleteNotification"))) {
			throw new SecurityException(GlobalExceptionHandler.UNAUTHORIZED_ACTION);
		}

		notificationRepository.deleteAll();

		if (!notificationRepository.findAll().isEmpty()) {
			throw new DataAccessException("Error : Could not delete all notifications") {};
		}
	}

}