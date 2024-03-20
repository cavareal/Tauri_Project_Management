package fr.eseo.tauri.model.enumeration;

import lombok.Getter;

@Getter
public enum FlagType {
    REPORTING("reporting");

    private final String type;

    FlagType(String type) {
        this.type = type;
    }
}
