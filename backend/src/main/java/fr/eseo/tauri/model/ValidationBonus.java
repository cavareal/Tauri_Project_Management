package fr.eseo.tauri.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import fr.eseo.tauri.model.id_class.ValidationBonusId;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Table(name = "validation_bonuses")
@IdClass(ValidationBonusId.class)
@Getter
@Setter
public class ValidationBonus {

    private Boolean confirmed;

    @Id
    @ManyToOne
    @JoinColumn(name = "user_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonProperty
    private User user;

    @Id
    @ManyToOne
    @JoinColumn(name = "bonus_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonProperty
    private Bonus bonus;

}