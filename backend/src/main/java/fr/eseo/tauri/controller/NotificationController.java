package fr.eseo.tauri.controller;

import fr.eseo.tauri.model.Notification;
import fr.eseo.tauri.repository.NotificationRepository;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/notifications")
@Tag(name = "notifications")
public class NotificationController {

    private final NotificationRepository notificationRepository;

    @PostMapping("/")
    public Notification addNotification(@RequestBody Notification notification) {
        return notificationRepository.save(notification);
    }

    @GetMapping("/")
    public Iterable<Notification> getAllNotifications() {
        return notificationRepository.findAll();
    }

    @GetMapping("/{id}")
    public Notification getNotificationById(@PathVariable Integer id) {
        return notificationRepository.findById(id).orElse(null);
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
