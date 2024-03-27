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
    @Column(columnDefinition = "ENUM('Reporting')")
    private FlagType type;

    @OneToOne
    @JoinColumn(name = "first_student_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Student firstStudentId;

    @OneToOne
    @JoinColumn(name = "second_student_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Student secondStudentId;

    @OneToOne
    @JoinColumn(name = "user_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User userId;
}
