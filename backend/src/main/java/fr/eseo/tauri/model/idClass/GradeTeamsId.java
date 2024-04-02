package fr.eseo.tauri.model.idClass;

import com.fasterxml.jackson.annotation.JsonProperty;
import fr.eseo.tauri.model.Grade;
import fr.eseo.tauri.model.Team;

import java.io.Serializable;
import java.util.Objects;

public class GradeTeamsId implements Serializable {

    @JsonProperty
    private Grade grade;

    @JsonProperty
    private Team team;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GradeTeamsId that = (GradeTeamsId) o;
        return Objects.equals(grade, that.grade) &&
                Objects.equals(team, that.team);
    }

    @Override
    public int hashCode() {
        return Objects.hash(grade, team);
    }
}
