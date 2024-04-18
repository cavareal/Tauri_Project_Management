package fr.eseo.tauri.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import fr.eseo.tauri.model.enumeration.Gender;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.io.Serializable;


@Entity
@Table(name = "students")
@OnDelete(action = OnDeleteAction.CASCADE)
@PrimaryKeyJoinColumn(name="user_id")
@Getter
@Setter
public class Student extends User implements Serializable {

    @Enumerated(EnumType.STRING)
    @Column(name="gender")
    @JsonProperty
    private Gender gender;

    @JsonProperty
    private Boolean bachelor;

    @JsonProperty
    private String teamRole; //Enumeration avec PO / SA / etc etc + laisser la possibilitié d'y rajouter des trucs à la main par les étudiants ? Pour setup des trucs plus tard comme des canaux de discution entre SA / PO et PL

    @ManyToOne
    @JoinColumn(name = "team_id")
    @OnDelete(action = OnDeleteAction.NO_ACTION)
	@JsonProperty
    private Team team;

    @ManyToOne
    @JoinColumn(name = "project_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
	@JsonProperty
    private Project project;

}

