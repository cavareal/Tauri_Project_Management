package fr.eseo.tauri.controller;

import fr.eseo.tauri.model.Notification;
import fr.eseo.tauri.service.NotificationService;
import fr.eseo.tauri.util.CustomLogger;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/notifications")
@Tag(name = "notifications")
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping
    public ResponseEntity<List<Notification>> getAllNotifications(@RequestHeader("Authorization") String token) {
        List<Notification> notifications = notificationService.getAllNotifications(token);
        return ResponseEntity.ok(notifications);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Notification> getNotificationById(@RequestHeader("Authorization") String token, @PathVariable Integer id) {
        Notification notification = notificationService.getNotificationById(token, id);
        return ResponseEntity.ok(notification);
    }

    @PostMapping
    public ResponseEntity<String> addNotifications(@RequestHeader("Authorization") String token, @RequestBody List<Notification> notifications) {
        notificationService.addNotifications(token, notifications);
        CustomLogger.logInfo("The notification(s) have been added");
        return ResponseEntity.ok("The notification(s) have been added");
    }

    @PatchMapping("/{id}")
    public ResponseEntity<String> updateNotification(@RequestHeader("Authorization") String token, @PathVariable Integer id, @RequestBody Map<String, Object> request) {
        notificationService.updateNotification(token, id, request);
        CustomLogger.logInfo("The notification has been updated");
        return ResponseEntity.ok("The notification has been updated");
    }

    @DeleteMapping
    public ResponseEntity<String> deleteAllNotifications(@RequestHeader("Authorization") String token) {
        notificationService.deleteAllNotifications(token);
        CustomLogger.logInfo("All the notifications have been deleted");
        return ResponseEntity.ok("All the notifications have been deleted");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteNotification(@RequestHeader("Authorization") String token, @PathVariable Integer id) {
        notificationService.deleteNotification(token, id);
        CustomLogger.logInfo("The notification has been deleted");
        return ResponseEntity.ok("The notification has been deleted");
    }
}