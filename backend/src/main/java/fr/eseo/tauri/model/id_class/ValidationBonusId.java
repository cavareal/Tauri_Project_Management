package fr.eseo.tauri.model.id_class;

import com.fasterxml.jackson.annotation.JsonProperty;
import fr.eseo.tauri.model.Bonus;
import fr.eseo.tauri.model.User;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@EqualsAndHashCode
public class ValidationBonusId implements Serializable {

    @JsonProperty
    private User user;

    @JsonProperty
    private Bonus bonus;

}
