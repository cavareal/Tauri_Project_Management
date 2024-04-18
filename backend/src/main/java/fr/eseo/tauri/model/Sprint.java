package fr.eseo.tauri.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import fr.eseo.tauri.model.enumeration.SprintEndType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDate;

@Entity
@Table(name = "sprints")
@Getter
@Setter
public class Sprint {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty
    private Integer id;

    @JsonProperty
    private LocalDate startDate;

    @JsonProperty
    private LocalDate endDate;

    @Enumerated(EnumType.STRING)
    @Column(name="end_type")
    @JsonProperty
    private SprintEndType endType;

    @ManyToOne
    @JoinColumn(name = "project_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonProperty
    private Project project;
}
