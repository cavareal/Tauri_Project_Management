package fr.eseo.tauri.model.enumeration;

import lombok.Getter;

@Getter
public enum Gendre {
    FEMME("Femme"),
    HOMME("Homme"),
    AUTRE("Autre");

    private final String genreName;

    Gendre(String genreName) {
        this.genreName = genreName;
    }

}
