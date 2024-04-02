package fr.eseo.tauri.model;

import fr.eseo.tauri.model.idClass.PresentationOrderId;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;


@Entity
@Table(name = "presentation_orders")
@IdClass(PresentationOrderId.class)
@Getter
@Setter
public class PresentationOrder {

    @Id
    @ManyToOne
    @JoinColumn(name = "sprint_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Sprint sprint;

    @Id
    @ManyToOne
    @JoinColumn(name = "student_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Student student;

    private Integer value;

}
