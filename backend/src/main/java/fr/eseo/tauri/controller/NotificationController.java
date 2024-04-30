package fr.eseo.tauri.controller;

import fr.eseo.tauri.model.Notification;
import fr.eseo.tauri.service.NotificationService;
import fr.eseo.tauri.util.CustomLogger;
import fr.eseo.tauri.validator.notification.CreateNotificationValidator;
import fr.eseo.tauri.validator.notification.UpdateNotificationValidator;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/notifications")
@Tag(name = "notifications")
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping("/{id}")
    public ResponseEntity<Notification> getNotificationById(@RequestHeader("Authorization") String token, @PathVariable Integer id) {
        var notification = notificationService.getNotificationById(token, id);
        return ResponseEntity.ok(notification);
    }

    @GetMapping
    public ResponseEntity<List<Notification>> getAllNotifications(@RequestHeader("Authorization") String token) {
        var notifications = notificationService.getAllNotifications(token);
        return ResponseEntity.ok(notifications);
    }

    @PostMapping
    public ResponseEntity<String> createNotification(@RequestHeader("Authorization") String token, @Valid @RequestBody CreateNotificationValidator notificationDetails) {
        notificationService.createNotification(token, notificationDetails);
        CustomLogger.info("The notification has been added");
        return ResponseEntity.ok("The notification has been added");
    }

    @PostMapping("/batch")
    public ResponseEntity<String> createManyNotifications(@RequestHeader("Authorization") String token, @Valid @RequestBody List<CreateNotificationValidator> notificationsDetails) {
        notificationService.createManyNotifications(token, notificationsDetails);
        CustomLogger.info("The notification(s) have been added");
        return ResponseEntity.ok("The notification(s) have been added");
    }

    @PatchMapping("/{id}")
    public ResponseEntity<String> updateNotification(@RequestHeader("Authorization") String token, @PathVariable Integer id, @Valid @RequestBody UpdateNotificationValidator request) {
        notificationService.updateNotification(token, id, request);
        CustomLogger.info("The notification has been updated");
        return ResponseEntity.ok("The notification has been updated");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteNotificationById(@RequestHeader("Authorization") String token, @PathVariable Integer id) {
        notificationService.deleteNotificationById(token, id);
        CustomLogger.info("The notification has been deleted");
        return ResponseEntity.ok("The notification has been deleted");
    }

    @DeleteMapping
    public ResponseEntity<String> deleteAllNotifications(@RequestHeader("Authorization") String token) {
        notificationService.deleteAllNotifications(token);
        CustomLogger.info("All the notifications have been deleted");
        return ResponseEntity.ok("All the notifications have been deleted");
    }

}