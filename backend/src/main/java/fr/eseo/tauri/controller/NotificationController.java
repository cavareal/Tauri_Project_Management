package fr.eseo.tauri.controller;

import fr.eseo.tauri.model.Notification;
import fr.eseo.tauri.service.NotificationService;
import fr.eseo.tauri.util.CustomLogger;
import fr.eseo.tauri.util.valid.Create;
import fr.eseo.tauri.util.valid.Update;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/notifications")
@Tag(name = "notifications")
public class NotificationController {

    private final NotificationService notificationService;

    /**
     * Get a notification by its id
     * @param token the token of the user
     * @param id the id of the notification
     * @return the notification
     */
    @GetMapping("/{id}")
    public ResponseEntity<Notification> getNotificationById(@RequestHeader("Authorization") String token, @PathVariable Integer id) {
        var notification = notificationService.getNotificationById(token, id);
        return ResponseEntity.ok(notification);
    }

    /**
     * Get all notifications by project
     * @param token the token of the user
     * @param projectId the id of the project
     * @return the list of notifications
     */
    @GetMapping
    public ResponseEntity<List<Notification>> getAllNotifications(@RequestHeader("Authorization") String token , @RequestParam("projectId") Integer projectId) {
        var notifications = notificationService.getAllNotifications(token);
        return ResponseEntity.ok(notifications);
    }

    /**
     * Create a notification
     * @param token the token of the user
     * @param notification the notification to create
     * @return a message
     */
    @PostMapping
    public ResponseEntity<String> createNotification(@RequestHeader("Authorization") String token, @Validated(Create.class) @RequestBody Notification notification) {
        notificationService.createNotification(token, notification);
        CustomLogger.info("The notification has been added");
        return ResponseEntity.ok("The notification has been added");
    }

    /**
     * Update a notification
     * @param token the token of the user
     * @param id the id of the notification
     * @param updatedNotification the updated notification
     * @return a message
     */
    @PatchMapping("/{id}")
    public ResponseEntity<String> updateNotification(@RequestHeader("Authorization") String token, @PathVariable Integer id, @Validated(Update.class) @RequestBody Notification updatedNotification) {
        notificationService.updateNotification(token, id, updatedNotification);
        CustomLogger.info("The notification has been updated");
        return ResponseEntity.ok("The notification has been updated");
    }

    /**
     * Delete a notification by its id
     * @param token the token of the user
     * @param id the id of the notification
     * @return a message
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteNotificationById(@RequestHeader("Authorization") String token, @PathVariable Integer id) {
        notificationService.deleteNotificationById(token, id);
        CustomLogger.info("The notification has been deleted");
        return ResponseEntity.ok("The notification has been deleted");
    }

    /**
     * Delete all notifications
     * @param token
     * @return
     */
    @DeleteMapping
    public ResponseEntity<String> deleteAllNotifications(@RequestHeader("Authorization") String token) {
        notificationService.deleteAllNotifications(token);
        CustomLogger.info("All the notifications have been deleted");
        return ResponseEntity.ok("All the notifications have been deleted");
    }

}