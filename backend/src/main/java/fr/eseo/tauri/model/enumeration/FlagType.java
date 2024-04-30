package fr.eseo.tauri.model.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum FlagType {

    REPORTING("Reporting"),
    VALIDATION("Validation");

    private final String type;

}
