package fr.eseo.tauri.model.id_class;

import com.fasterxml.jackson.annotation.JsonProperty;
import fr.eseo.tauri.model.Sprint;
import fr.eseo.tauri.model.Student;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

public class PresentationOrderId implements Serializable {

    @JsonProperty
    private Sprint sprint;

    @JsonProperty
    private Student student;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PresentationOrderId that = (PresentationOrderId) o;
        return Objects.equals(sprint, that.sprint) &&
                Objects.equals(student, that.student);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sprint, student);
    }
}
