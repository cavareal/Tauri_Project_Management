package fr.eseo.tauri.model.enumeration;

import lombok.Getter;

@Getter
public enum RoleType {
    SS("Supervising Staff");

    private final String displayName;

    RoleType(String displayName) {
        this.displayName = displayName;
    }
}
