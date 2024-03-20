package fr.eseo.tauri.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
@Entity
@Table(name = "bonuses")
@Getter
@Setter

public class Bonus {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String value;

    private String comment;

    private boolean limited;

    private boolean confirmed;
}
