package fr.eseo.tauri.model.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ProjectPhase {

    COMPOSING("Composition"),
    PREPUBLISHED("Prépublié"),
    PUBLISHED("Publié"),
    FINISHED("Terminé");

    private final String displayName;

}
