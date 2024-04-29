package fr.eseo.tauri.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import fr.eseo.tauri.model.enumeration.FlagType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.io.Serializable;

@Entity
@Table(name = "flags")
@Getter
@Setter
public class Flag implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty
    private Integer id;

    @JsonProperty
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name="type")
    @JsonProperty
    private FlagType type;

    @ManyToOne
    @JoinColumn(name = "first_student_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonProperty
    private Student firstStudent;

    @ManyToOne
    @JoinColumn(name = "second_student_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonProperty
    private Student secondStudent;

    @ManyToOne
    @JoinColumn(name = "author_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonProperty
    private User author;

}
