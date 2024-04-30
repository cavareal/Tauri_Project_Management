package fr.eseo.tauri.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import fr.eseo.tauri.model.id_class.ValidationFlagId;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Table(name = "validation_flags")
@IdClass(ValidationFlagId.class)
@Data
public class ValidationFlag {

    private Boolean confirmed;

    @Id
    @ManyToOne
    @JoinColumn(name = "author_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonProperty
    private User author;

    @Id
    @ManyToOne
    @JoinColumn(name = "flag_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonProperty
    private Flag flag;

}