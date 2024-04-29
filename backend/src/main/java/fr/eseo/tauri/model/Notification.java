package fr.eseo.tauri.model;


import com.fasterxml.jackson.annotation.JsonProperty;
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
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty
    private Integer id;

    @JsonProperty
    private String message;

    @JsonProperty
    private Boolean isRead;

    @Enumerated(EnumType.STRING)
    @Column(name="type")
    @JsonProperty
    private NotificationType type;

    @ManyToOne
    @JoinColumn(name = "user_to")
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonProperty
    private User userTo;

    @ManyToOne
    @JoinColumn(name = "user_from")
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonProperty
    private User userFrom;

}