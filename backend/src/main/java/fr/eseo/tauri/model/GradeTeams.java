package fr.eseo.tauri.model;

import fr.eseo.tauri.model.idClass.GradeTeamsId;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Table(name = "grade_teams")
@IdClass(GradeTeamsId.class)
@Getter
@Setter

public class GradeTeams {

    @Id
    @OneToOne
    @JoinColumn(name = "grade_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Grade grade;

    @Id
    @OneToOne
    @JoinColumn(name = "team_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Team team;

}

// /!\ Make sure that, if a team is deleted, all the grades that were attributed to it are deleted as well, but not the other way around. /!\