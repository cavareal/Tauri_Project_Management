package fr.eseo.tauri.model.idClass;

import com.fasterxml.jackson.annotation.JsonProperty;
import fr.eseo.tauri.model.Flag;
import fr.eseo.tauri.model.User;

import java.io.Serializable;
import java.util.Objects;

public class ValidationFlagId implements Serializable {

    @JsonProperty
    private User author;

    @JsonProperty
    private Flag flag;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ValidationFlagId that = (ValidationFlagId) o;
        return Objects.equals(author, that.author) &&
                Objects.equals(flag, that.flag);
    }

    @Override
    public int hashCode() {
        return Objects.hash(author, flag);
    }
}
