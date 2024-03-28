package fr.eseo.tauri.model;

import fr.eseo.tauri.model.idClass.ValidationBonusId;
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
    private User userId;

    @Id
    @ManyToOne
    @JoinColumn(name = "bonus_id")
    @OnDelete(action = OnDeleteAction.CASCADE)

    private Bonus bonusId;
}