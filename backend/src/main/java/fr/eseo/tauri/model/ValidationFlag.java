package fr.eseo.tauri.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Table(name = "validation_flags")
@Getter
@Setter
public class ValidationFlag {

    private Boolean confirmed;

    @Id
    @OneToOne()
    @JoinColumn(name = "author_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User authorId;

    @Id
    @OneToOne()
    @JoinColumn(name = "flag_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Flag flagId;
}
