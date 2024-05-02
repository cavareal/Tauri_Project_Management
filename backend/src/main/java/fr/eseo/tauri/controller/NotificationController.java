package fr.eseo.tauri.controller;

import fr.eseo.tauri.model.Notification;
import fr.eseo.tauri.model.User;
import fr.eseo.tauri.repository.NotificationRepository;
import fr.eseo.tauri.repository.UserRepository;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/notifications")
@Tag(name = "notifications")
public class NotificationController {

    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;

    @Autowired
    public NotificationController(NotificationRepository notificationRepository, UserRepository userRepository) {
        this.notificationRepository = notificationRepository;
        this.userRepository = userRepository;
    }

    @PostMapping("/")
    public Notification addNotification(@RequestBody Notification notification) {
        return notificationRepository.save(notification);
    }

    @GetMapping("/")
    public ResponseEntity<Iterable<Notification>> getAllNotifications() {

        return ResponseEntity.ok(notificationRepository.findAll());
    }

    @GetMapping("/isRead")
    public ResponseEntity<Iterable<Notification>> getAllNotificationsIfNotRead() {
        return ResponseEntity.ok(notificationRepository.findIfNotRead());
    }

    @GetMapping("/{id}")
    public Notification getNotificationById(@PathVariable Integer id) {
        return notificationRepository.findById(id).orElse(null);
    }

    @GetMapping("/users/{userId}/notifications")
    public ResponseEntity<Iterable<Notification>> getAllNotificationsUser(@PathVariable Integer userId) {
        Optional<User> actualUser = userRepository.findById(userId);
        return ResponseEntity.ok(notificationRepository.findByUser(userId));
    }

    @PutMapping("/{id}")
    public Notification updateNotification(@PathVariable Integer id, @RequestBody Notification notificationDetails) {
        Notification notification = notificationRepository.findById(id).orElse(null);
        if (notification != null) {
            notification.message(notificationDetails.message());
            notification.isRead(notificationDetails.isRead());
            notification.type(notificationDetails.type());
            // Si vous avez un champ User, vous pouvez également mettre à jour ici
            return notificationRepository.save(notification);
        }
        return null;
    }

    @DeleteMapping("/{id}")
    public String deleteNotification(@PathVariable Integer id) {
        notificationRepository.deleteById(id);
        return "Notification deleted";
    }
}
