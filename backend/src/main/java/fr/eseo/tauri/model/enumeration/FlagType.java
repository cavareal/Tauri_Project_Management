package fr.eseo.tauri.model.enumeration;

import lombok.Getter;

@Getter
public enum FlagType {
    REPORTING("Reporting"),
    VALIDATION("Validation");

    private final String type;

    FlagType(String type) {
        this.type = type;
    }
}
