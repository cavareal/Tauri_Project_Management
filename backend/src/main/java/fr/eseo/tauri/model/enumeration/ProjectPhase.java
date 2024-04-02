package fr.eseo.tauri.model.enumeration;

import lombok.Getter;

@Getter
public enum ProjectPhase {

    COMPOSING("Composition"),
    PREPUBLISHED("Prépublié"),
    PUBLISHED("Publié"),
    FINISHED("Terminé");

    private final String displayName;

    ProjectPhase(String displayName) {
        this.displayName = displayName;
    }
}
