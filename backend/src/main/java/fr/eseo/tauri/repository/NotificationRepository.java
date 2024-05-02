package fr.eseo.tauri.repository;

import fr.eseo.tauri.model.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface NotificationRepository extends JpaRepository<Notification, Integer> {

    @Query("SELECT n FROM Notification n WHERE n.userTo= :user")
    Iterable<Notification> findByUser(@Param("user") int id);

    @Query("SELECT n FROM Notification n WHERE n.isRead = false")
    Iterable<Notification> findIfNotRead();
}
