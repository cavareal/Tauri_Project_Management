package fr.eseo.tauri.model.enumeration;

import lombok.Getter;

@Getter
public enum Gender {
    FEMME("Femme"),
    HOMME("Homme"),
    AUTRE("Autre");

    private final String genderName;

    Gender(String genderName) {
        this.genderName = genderName;
    }

}
