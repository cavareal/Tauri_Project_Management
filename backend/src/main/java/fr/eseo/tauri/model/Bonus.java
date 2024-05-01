package fr.eseo.tauri.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import fr.eseo.tauri.util.valid.Create;
import fr.eseo.tauri.util.valid.Update;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Table(name = "bonuses")
@Data
public class Bonus {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty
    private Integer id;

    @NotNull(groups = { Create.class }, message = "The value field is required")
    @JsonProperty
    private Float value;

    @JsonProperty
    private String comment;

    @NotNull(groups = { Create.class }, message = "The limited field is required")
    @JsonProperty
    private Boolean limited;

    @ManyToOne
    @JoinColumn(name = "sprint_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonProperty
    private Sprint sprint;

    @ManyToOne
    @JoinColumn(name = "student_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonProperty
    private Student student;

    @ManyToOne
    @JoinColumn(name = "author_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonProperty
    private User author;

    @NotNull(groups = { Create.class }, message = "The sprintId field is required")
    @Transient
    @JsonDeserialize
    private Integer sprintId;

    @NotNull(groups = { Create.class }, message = "The studentId field is required")
    @Transient
    @JsonDeserialize
    private Integer studentId;

    @NotNull(groups = { Create.class, Update.class }, message = "The authorId field is required")
    @Transient
    @JsonDeserialize
    private Integer authorId;

}