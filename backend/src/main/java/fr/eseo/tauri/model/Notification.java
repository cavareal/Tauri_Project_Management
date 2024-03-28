package fr.eseo.tauri.model;


import fr.eseo.tauri.model.enumeration.NotificationType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Table(name = "notifications")
@Getter
@Setter
public class    Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String message;

    private Boolean isRead;

    @Enumerated(EnumType.STRING)
    @Column(name="type")
    private NotificationType type;

    @ManyToOne
    @JoinColumn(name = "user_to")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User userTo;

    @ManyToOne
    @JoinColumn(name = "user_from")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User userFrom;
}
