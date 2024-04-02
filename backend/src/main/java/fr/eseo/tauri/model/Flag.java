package fr.eseo.tauri.model;

import fr.eseo.tauri.model.enumeration.FlagType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Table(name = "flags")
@Getter
@Setter
public class Flag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name="type")
    private FlagType type;

    @ManyToOne
    @JoinColumn(name = "first_student_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Student firstStudent;

    @ManyToOne
    @JoinColumn(name = "second_student_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Student secondStudent;

    @ManyToOne
    @JoinColumn(name = "author_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User author;
}
