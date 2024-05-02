package fr.eseo.tauri.service;

import fr.eseo.tauri.model.Notification;
import fr.eseo.tauri.model.User;
import fr.eseo.tauri.model.enumeration.NotificationType;
import fr.eseo.tauri.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;


    void createNotification(String message, String type, User userTo, User userFrom) {
        if (message == null || message.trim().isEmpty()) {
            throw new IllegalArgumentException("Message cannot be null or empty");
        }
        if (type == null || type.trim().isEmpty()) {
            throw new IllegalArgumentException("Type cannot be null or empty");
        }
        if (userTo == null) {
            throw new IllegalArgumentException("UserTo status cannot be null");
        }
        if (userFrom == null) {
            throw new IllegalArgumentException("UserFrom status cannot be null");
        }

        Notification notification = new Notification();
        notification.message(message);
        notification.isRead(false);
        notification.type(NotificationType.valueOf(type));
        notification.userTo(userTo);
        notification.userFrom(userFrom);
        notificationRepository.save(notification);
    }
}
