package fr.eseo.tauri.service;

import fr.eseo.tauri.model.Notification;
import fr.eseo.tauri.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;

    /**
     * <b>HELPER  METHOD</b>
     * This method is used to create a Student object from the provided data.
     * The data includes the student's name, gender, and bachelor status.
     *
     * @param name the name of the student
     * @param gender the gender of the student
     * @param bachelor the bachelor status of the student
     * @return the created Student object
     * @throws IllegalArgumentException if the name or gender is null or empty, or if the bachelor status is null
     */
    void createNotification(String name, String gender, String bachelor) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Name cannot be null or empty");
        }
        if (gender == null || gender.trim().isEmpty()) {
            throw new IllegalArgumentException("Gender cannot be null or empty");
        }
        if (bachelor == null) {
            throw new IllegalArgumentException("Bachelor status cannot be null");
        }

        Notification notification = new Notification();
        notification.message("");
        notification.isRead(false);
        notification.type();
        notification.userTo();
        notification.userFrom();
        notificationRepository.save(notification);
    }
}
