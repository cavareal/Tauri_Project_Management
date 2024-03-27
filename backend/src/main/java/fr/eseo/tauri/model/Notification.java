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
    private Integer id;

    private String message;

    private Boolean isRead;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "ENUM('Bonus Malus')")
    private NotificationType type;

}
