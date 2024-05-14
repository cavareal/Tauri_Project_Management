package fr.eseo.tauri.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import fr.eseo.tauri.model.enumeration.ProjectPhase;
import fr.eseo.tauri.util.valid.Create;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Entity
@Table(name = "projects")
@Data
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty
    private Integer id;

    @NotNull(groups = { Create.class }, message = "The nbTeams field is required")
    @JsonProperty
    private Integer nbTeams;

    @NotNull(groups = { Create.class }, message = "The nbWomen field is required")
    @JsonProperty
    private Integer nbWomen;

    @JsonProperty
    private Integer nbSprint;

    @Enumerated(EnumType.STRING)
    @Column(name="phase")
    @JsonProperty
    private ProjectPhase phase = ProjectPhase.COMPOSING;

}