package fr.eseo.tauri.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import fr.eseo.tauri.model.enumeration.Gender;
import fr.eseo.tauri.model.enumeration.GradeTypeName;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "grade_types")
@Getter
@Setter
public class GradeType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty
    private Integer id;

    @Enumerated(EnumType.STRING)
    @Column(name="name")
    @JsonProperty
    private GradeTypeName name;

    @JsonProperty
    private Double factor;

    @JsonProperty
    private Boolean forGroup;

    @JsonProperty
    private Boolean imported;
}
