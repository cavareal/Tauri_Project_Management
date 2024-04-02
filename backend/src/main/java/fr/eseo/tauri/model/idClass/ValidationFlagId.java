package fr.eseo.tauri.model.idClass;

import com.fasterxml.jackson.annotation.JsonProperty;
import fr.eseo.tauri.model.Flag;
import fr.eseo.tauri.model.User;

import java.io.Serializable;
import java.util.Objects;

public class ValidationFlagId implements Serializable {

    @JsonProperty
    private User authorId;

    @JsonProperty
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
