package fr.eseo.tauri.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Table(name = "scale_types")
@IdClass(ScaleTypesId.class)
@Getter
@Setter

public class ScaleTypes {

    @Id
    @OneToOne
    @JoinColumn(name = "grade_scale_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private GradeScale gradeScaleId;

    @Id
    @OneToOne
    @JoinColumn(name = "grade_type_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private GradeType gradeTypeId;

}
