package fr.eseo.tauri.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Table(name = "validation_bonuses")
@Getter
@Setter
public class ValidationBonus {

    private Boolean confirmed;

    @Id
    @OneToOne()
    @JoinColumn(name = "user_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User userId;

    @Id
    @OneToOne()
    @JoinColumn(name = "bonus_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Bonus bonusId;
}
