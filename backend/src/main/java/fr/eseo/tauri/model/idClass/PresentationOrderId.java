package fr.eseo.tauri.model.idClass;

import com.fasterxml.jackson.annotation.JsonProperty;
import fr.eseo.tauri.model.Sprint;
import fr.eseo.tauri.model.Student;

import java.io.Serializable;
import java.util.Objects;

public class PresentationOrderId implements Serializable {

    @JsonProperty
    private Sprint sprintId;

    @JsonProperty
    private Student studentId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PresentationOrderId that = (PresentationOrderId) o;
        return Objects.equals(sprintId, that.sprintId) &&
                Objects.equals(studentId, that.studentId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sprintId, studentId);
    }
}
