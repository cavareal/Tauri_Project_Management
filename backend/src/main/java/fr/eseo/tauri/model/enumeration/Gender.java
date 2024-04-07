package fr.eseo.tauri.model.enumeration;

import lombok.Getter;

@Getter
public enum Gender {
    WOMAN("Femme"),
    MAN("Homme");

    private final String genderName;

    Gender(String genderName) {
        this.genderName = genderName;
    }

}
