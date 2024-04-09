package fr.eseo.tauri.controller;

import fr.eseo.tauri.model.Notification;
import fr.eseo.tauri.repository.NotificationRepository;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    private final NotificationRepository notificationRepository;

    public NotificationController(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    @PostMapping("/add")
    public Notification addNotification(@RequestBody Notification notification) {
        return notificationRepository.save(notification);
    }

    @GetMapping("/all")
    public Iterable<Notification> getAllNotifications() {
        return notificationRepository.findAll();
    }

    @GetMapping("/{id}")
    public Notification getNotificationById(@PathVariable Integer id) {
        return notificationRepository.findById(id).orElse(null);
    }

    @PutMapping("/update/{id}")
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

    @DeleteMapping("/delete/{id}")
    public String deleteNotification(@PathVariable Integer id) {
        notificationRepository.deleteById(id);
        return "Notification deleted";
    }
}
