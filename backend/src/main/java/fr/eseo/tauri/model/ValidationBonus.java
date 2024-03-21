package fr.eseo.tauri.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "validation_bonuses")
@Getter
@Setter

public class ValidationBonus {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id; //Change it later, this tables primary key is the combination of the two foreign keys

    private Boolean confirmed;
}
