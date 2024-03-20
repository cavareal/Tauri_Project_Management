package fr.eseo.tauri.model;

import fr.eseo.tauri.model.enumeration.Gender;
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

    private Gender gender;

    private String bachelor;

    private String role;

}

