package fr.eseo.tauri.model;

import java.io.Serializable;
import java.util.Objects;

public class ValidationBonusId implements Serializable {
    private User userId;
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
