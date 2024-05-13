package fr.eseo.tauri.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import fr.eseo.tauri.model.id_class.ValidationBonusId;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Table(name = "validation_bonuses")
@IdClass(ValidationBonusId.class)
@Data
public class ValidationBonus {

    @JsonProperty
    private Boolean confirmed = false;

    @Id
    @ManyToOne
    @JoinColumn(name = "author_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonProperty
    private User author;

    @Id
    @ManyToOne
    @JoinColumn(name = "bonus_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonProperty
    private Bonus bonus;

}