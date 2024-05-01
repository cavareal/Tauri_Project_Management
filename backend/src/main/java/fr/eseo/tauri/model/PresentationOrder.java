package fr.eseo.tauri.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import fr.eseo.tauri.model.id_class.PresentationOrderId;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NonNull;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Table(name = "presentation_orders")
@IdClass(PresentationOrderId.class)
@Data
public class PresentationOrder {

    @Id
    @ManyToOne
    @JoinColumn(name = "sprint_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    @NonNull
    @JsonProperty
    private Sprint sprint;

    @Id
    @ManyToOne
    @JoinColumn(name = "student_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    @NonNull
    @JsonProperty
    private Student student;

    @JsonProperty
    private Integer value;

}