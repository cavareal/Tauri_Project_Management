package fr.eseo.tauri.model;

import fr.eseo.tauri.model.enumeration.SprintEndType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "sprints")
@Getter
@Setter
public class Sprint {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private LocalDate startDate;

    private LocalDate endDate;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "ENUM('Sprint Normal', 'Sprint Final)")
    private SprintEndType endType;
}
