package fr.eseo.tauri.model;


import fr.eseo.tauri.model.enumeration.NotificationType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "notifications")
@Getter
@Setter
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String message;

    private boolean read;

    private NotificationType type;
}
