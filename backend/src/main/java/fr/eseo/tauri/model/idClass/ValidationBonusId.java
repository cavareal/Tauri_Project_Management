package fr.eseo.tauri.model.idClass;

import com.fasterxml.jackson.annotation.JsonProperty;
import fr.eseo.tauri.model.Bonus;
import fr.eseo.tauri.model.User;

import java.io.Serializable;
import java.util.Objects;

public class ValidationBonusId implements Serializable {

    @JsonProperty
    private User userId;

    @JsonProperty
    private Bonus bonusId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ValidationBonusId that = (ValidationBonusId) o;
        return Objects.equals(userId, that.userId) &&
                Objects.equals(bonusId, that.bonusId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, bonusId);
    }
}
