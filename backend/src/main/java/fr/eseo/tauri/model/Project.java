package fr.eseo.tauri.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;

@Entity
@Table(name = "projects")
@Getter
@Setter
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer nbTeams;

    private Integer ratioGendre;        // Ratio/100

    private Integer nbSprint;

    private String phase;

}





