package fr.eseo.tauri.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import fr.eseo.tauri.util.valid.Create;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Entity
@Table(name = "grade_types")
@Data
public class GradeType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty
    private Integer id;

    @NotNull(groups = { Create.class }, message = "The name field is required")
    @JsonProperty
    private String name;

    @JsonProperty
    private Float factor;

    @NotNull(groups = { Create.class }, message = "The  field is required")
    @JsonProperty
    private Boolean forGroup;

    @NotNull(groups = { Create.class }, message = "The imported field is required")
    @JsonProperty
    private Boolean imported;

    @JsonProperty
    private String scaleUrl;

}