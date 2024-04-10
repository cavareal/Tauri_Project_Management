package fr.eseo.tauri.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import fr.eseo.tauri.model.enumeration.ProjectPhase;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "projects")
@Getter
@Setter
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty
    private Integer id;

    @JsonProperty
    private Integer nbTeams;

    @JsonProperty
    private Integer ratioGender;        // Ratio/100

    @JsonProperty
    private Integer nbSprint;

    @Enumerated(EnumType.STRING)
    @Column(name="phase")
    @JsonProperty
    private ProjectPhase phase;

}





