package fr.eseo.tauri.model;

import java.io.Serializable;
import java.util.Objects;

public class PresentationOrderId implements Serializable {
    private Sprint sprintId;
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
