package fr.eseo.tauri.model;

import fr.eseo.tauri.model.idClass.ValidationFlagId;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Table(name = "validation_flags")
@IdClass(ValidationFlagId.class)
@Getter
@Setter
public class ValidationFlag {

    private Boolean confirmed;

    @Id
    @ManyToOne
    @JoinColumn(name = "author_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User author;

    @Id
    @ManyToOne
    @JoinColumn(name = "flag_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Flag flag;
}
