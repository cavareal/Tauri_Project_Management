package fr.eseo.tauri.model.idClass;

import com.fasterxml.jackson.annotation.JsonProperty;
import fr.eseo.tauri.model.Role;
import fr.eseo.tauri.model.User;

import java.io.Serializable;
import java.util.Objects;

public class UserRolesId implements Serializable {

    @JsonProperty
    private User user;

    @JsonProperty
    private Role role;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserRolesId that = (UserRolesId) o;
        return Objects.equals(user, that.user) &&
                Objects.equals(role, that.role);
    }

    @Override
    public int hashCode() {
        return Objects.hash(user, role);
    }
}
