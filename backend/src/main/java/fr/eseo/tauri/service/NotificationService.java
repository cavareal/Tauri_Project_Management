package fr.eseo.tauri.service;

import fr.eseo.tauri.controller.GlobalExceptionHandler;
import fr.eseo.tauri.model.Notification;
import fr.eseo.tauri.model.enumeration.NotificationType;
import fr.eseo.tauri.model.exception.ResourceNotFoundException;
import fr.eseo.tauri.repository.NotificationRepository;
import fr.eseo.tauri.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final AuthService authService;
    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;

    public List<Notification> getAllNotifications(String token) {
        if (!Boolean.TRUE.equals(authService.checkAuth(token, "readNotifications"))) {
            throw new SecurityException(GlobalExceptionHandler.UNAUTHORIZED_ACTION);
        }
        return notificationRepository.findAll();
    }

    public Notification getNotificationById(String token, Integer id) {
        if (!Boolean.TRUE.equals(authService.checkAuth(token, "readNotification"))) {
            throw new SecurityException(GlobalExceptionHandler.UNAUTHORIZED_ACTION);
        }
        return notificationRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("notification", id));
    }

    public void addNotifications(String token, List<Notification> notifications) {
        if (!Boolean.TRUE.equals(authService.checkAuth(token, "addNotification"))) {
            throw new SecurityException(GlobalExceptionHandler.UNAUTHORIZED_ACTION);
        }
        int notificationsNumber = notificationRepository.findAll().size();
        for(Notification notification : notifications) {
            notificationRepository.save(notification);
            if(notificationRepository.findAll().size() == notificationsNumber){
                throw new DataAccessException("Error : Could not add notification sent by " + notification.userFrom().name()) {};
            } else {
                notificationsNumber++;
            }
        }
    }

    public void updateNotification(String token, Integer id, Map<String, Object> notificationDetails) {
        if (Boolean.TRUE.equals(authService.checkAuth(token, "updateNotification"))) {
            Notification notification = getNotificationById(token, id);

            for (Map.Entry<String, Object> entry : notificationDetails.entrySet()) {
                String key = entry.getKey();
                Object value = entry.getValue();

                if (value == null) {
                    continue;
                }

                switch (key) {
                    case "message":
                        notification.message((String) value);
                        break;
                    case "isRead":
                        notification.isRead((Boolean) value);
                        break;
                    case "type":
                        notification.type(NotificationType.valueOf((String) value));
                        break;
                    case "userTo":
                        Map<String, Object> userToMap = (Map<String, Object>) value;
                        notification.userTo(userRepository.findById((Integer) userToMap.get("id")).orElseThrow(() -> new ResourceNotFoundException("user", (Integer) userToMap.get("id"))));
                        break;
                    case "userFrom":
                        Map<String, Object> userFromMap = (Map<String, Object>) value;
                        notification.userFrom(userRepository.findById((Integer) userFromMap.get("id")).orElseThrow(() -> new ResourceNotFoundException("user", (Integer) userFromMap.get("id"))));
                        break;
                    default:
                        throw new IllegalArgumentException("Invalid key: " + key);
                }
            }
            notificationRepository.save(notification);

        } else {
            throw new SecurityException(GlobalExceptionHandler.UNAUTHORIZED_ACTION);
        }
    }

    public void deleteAllNotifications(String token) {
        if (!Boolean.TRUE.equals(authService.checkAuth(token, "deleteNotification"))) {
            throw new SecurityException(GlobalExceptionHandler.UNAUTHORIZED_ACTION);
        }
        notificationRepository.deleteAll();
        if(!notificationRepository.findAll().isEmpty()){
            throw new DataAccessException("Error : Could not delete all notifications") {};
        }
    }

    public void deleteNotification(String token, Integer id) {
        if (!Boolean.TRUE.equals(authService.checkAuth(token, "deleteNotification"))) {
            throw new SecurityException(GlobalExceptionHandler.UNAUTHORIZED_ACTION);
        }
        getNotificationById(token, id);
        int notificationsNumber = notificationRepository.findAll().size();
        notificationRepository.deleteById(id);
        if(notificationRepository.findAll().size() == notificationsNumber){
            throw new DataAccessException("Error : Could not delete notification with id : " + id) {};
        }
    }
}