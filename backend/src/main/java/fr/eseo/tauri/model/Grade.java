package fr.eseo.tauri.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import fr.eseo.tauri.util.valid.Create;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Table(name = "grades")
@Data
public class Grade {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty
    private Integer id;

    @NotNull(groups = { Create.class }, message = "The value field is required")
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
    @JoinColumn(name = "student_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonProperty
    private Student student;

    @ManyToOne
    @JoinColumn(name = "team_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonProperty
    private Team team;

    @ManyToOne
    @JoinColumn(name = "sprint_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonProperty
    private Sprint sprint;

    @NotNull(groups = { Create.class }, message = "The gradeTypeId field is required")
    @Transient
    @JsonDeserialize
    private Integer gradeTypeId;

    @NotNull(groups = { Create.class }, message = "The authorId field is required")
    @Transient
    @JsonDeserialize
    private Integer authorId;

//    @NotNull(groups = { Create.class }, message = "The studentId field is required")
    @Transient
    @JsonDeserialize
    private Integer studentId;

//    @NotNull(groups = { Create.class }, message = "The teamId field is required")
    @Transient
    @JsonDeserialize
    private Integer teamId;

    @NotNull(groups = { Create.class }, message = "The sprintId field is required")
    @Transient
    @JsonDeserialize
    private Integer sprintId;

}