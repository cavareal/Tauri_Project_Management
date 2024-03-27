package fr.eseo.tauri.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Table(name = "grade_teams")
@Getter
@Setter

public class GradeTeams {

    @Id
    @OneToOne
    @JoinColumn(name = "grade_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Grade gradeId;

    @Id
    @OneToOne
    @JoinColumn(name = "team_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Team teamId;

}
