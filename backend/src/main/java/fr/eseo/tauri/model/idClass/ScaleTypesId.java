package fr.eseo.tauri.model.idClass;

import com.fasterxml.jackson.annotation.JsonProperty;
import fr.eseo.tauri.model.GradeScale;
import fr.eseo.tauri.model.GradeType;

import java.io.Serializable;
import java.util.Objects;

public class ScaleTypesId implements Serializable {

    @JsonProperty
    private GradeScale gradeScaleId;

    @JsonProperty
    private GradeType gradeTypeId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ScaleTypesId that = (ScaleTypesId) o;
        return Objects.equals(gradeScaleId, that.gradeScaleId) &&
                Objects.equals(gradeTypeId, that.gradeTypeId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(gradeScaleId, gradeTypeId);
    }
}
