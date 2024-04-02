package fr.eseo.tauri.model;

import com.fasterxml.jackson.annotation.JsonProperty;
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

    @JsonProperty
    private String name;

    @JsonProperty
    private Integer factor;

    @JsonProperty
    private Boolean forGroup;

    @JsonProperty
    private Boolean imported;
}
