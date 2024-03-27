package fr.eseo.tauri.model;

import java.io.Serializable;
import java.util.Objects;

public class ValidationFlagId implements Serializable {
    private User authorId;
    private Flag flagId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ValidationFlagId that = (ValidationFlagId) o;
        return Objects.equals(authorId, that.authorId) &&
                Objects.equals(flagId, that.flagId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(authorId, flagId);
    }
}
