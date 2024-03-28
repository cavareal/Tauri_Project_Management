package fr.eseo.tauri.model;

import fr.eseo.tauri.model.enumeration.Gender;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;


@Entity
@Table(name = "students")
@Getter
@Setter
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Enumerated(EnumType.STRING)
    @Column(name="gender")
    private Gender gender;

    private String bachelor;

    private String teamRole; //Enumeration avec PO / SA / etc etc + laisser la possibilitié d'y rajouter des trucs à la main par les étudiants ? Pour setup des trucs plus tard comme des canaux de discution entre SA / PO et PL

    @OneToOne
    @JoinColumn(name = "team_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Team teamId;

    @OneToOne
    @JoinColumn(name = "user_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User userId;

    @OneToOne
    @JoinColumn(name = "project_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Project projectId;

}

