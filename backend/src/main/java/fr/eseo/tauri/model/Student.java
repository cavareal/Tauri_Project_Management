package fr.eseo.tauri.model;

import fr.eseo.tauri.model.enumeration.Gendre;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


@Entity
@Table(name = "students")
@Getter
@Setter
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Gendre gendre;

    private String bachelor;

    private String role;

}

