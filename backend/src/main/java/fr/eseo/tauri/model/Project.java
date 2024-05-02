package fr.eseo.tauri.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import fr.eseo.tauri.model.enumeration.ProjectPhase;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "projects")
@Data
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty
    private Integer id;

    @JsonProperty
    private Integer nbTeams;

    @JsonProperty
    private Integer nbWomen;

    // TODO: À supprimer une fois que la page Sprints aura été modifée en accord avec la maquette
    @JsonProperty
    private Integer nbSprint;

    @Enumerated(EnumType.STRING)
    @Column(name="phase")
    @JsonProperty
    private ProjectPhase phase = ProjectPhase.COMPOSING;

}