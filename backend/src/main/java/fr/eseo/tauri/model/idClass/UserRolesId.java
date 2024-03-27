package fr.eseo.tauri.model.idClass;

import fr.eseo.tauri.model.Role;
import fr.eseo.tauri.model.User;

import java.io.Serializable;
import java.util.Objects;

public class UserRolesId implements Serializable {
    private User userId;
    private Role roleId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserRolesId that = (UserRolesId) o;
        return Objects.equals(userId, that.userId) &&
                Objects.equals(roleId, that.roleId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, roleId);
    }
}
