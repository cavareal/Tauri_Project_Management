package fr.eseo.tauri.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Table(name = "scale_types")
@Getter
@Setter

public class ScaleTypes {

    @Id
    @OneToOne
    @JoinColumn(name = "scale_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private GradeScale scaleId;

    @Id
    @OneToOne
    @JoinColumn(name = "type_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private GradeType typeId;

}
