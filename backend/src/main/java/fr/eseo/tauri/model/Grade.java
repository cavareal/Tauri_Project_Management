package fr.eseo.tauri.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.AssertTrue;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Table(name = "grades")
@Getter
@Setter
public class Grade {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty
    private Integer id;

    @JsonProperty
    private Float value;

    @JsonProperty
    private String comment;

    @ManyToOne
    @JoinColumn(name = "grade_type_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonProperty
    private GradeType gradeType;

    @ManyToOne
    @JoinColumn(name = "author_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonProperty
    private User author;

    @ManyToOne
    @JoinColumn(name = "student_id", nullable = true)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonProperty
    private Student student;

    @ManyToOne
    @JoinColumn(name = "team_id", nullable = true)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonProperty
    private Team team;

    @ManyToOne
    @JoinColumn(name = "sprint_id", nullable = true)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonProperty
    private Sprint sprint;

    /*@AssertTrue
    public boolean isStudentOrTeam() {

    }*/

}