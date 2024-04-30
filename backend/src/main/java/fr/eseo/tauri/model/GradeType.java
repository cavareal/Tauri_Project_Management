package fr.eseo.tauri.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "grade_types")
@Data
public class GradeType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty
    private Integer id;

    @JsonProperty
    private String name;

    @JsonProperty
    private Float factor;

    @JsonProperty
    private Boolean forGroup;

    @JsonProperty
    private Boolean imported;

    @JsonProperty
    private String scaleUrl;

}