package fr.eseo.tauri.model.idClass;

import fr.eseo.tauri.model.Grade;
import fr.eseo.tauri.model.Team;

import java.io.Serializable;
import java.util.Objects;

public class GradeTeamsId implements Serializable {
    private Grade gradeId;
    private Team teamId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GradeTeamsId that = (GradeTeamsId) o;
        return Objects.equals(gradeId, that.gradeId) &&
                Objects.equals(teamId, that.teamId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(gradeId, teamId);
    }
}
