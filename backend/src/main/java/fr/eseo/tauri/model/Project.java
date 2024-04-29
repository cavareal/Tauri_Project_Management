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
    private Integer nbWomen;

    @JsonProperty
    private Integer nbSprint; //A supprimer une fois que la page Sprints aura été modifée en accord avec la maquette

    @Enumerated(EnumType.STRING)
    @Column(name="phase", columnDefinition = "varchar(32) default 'COMPOSING'")
    @JsonProperty
    private ProjectPhase phase = ProjectPhase.COMPOSING;

}