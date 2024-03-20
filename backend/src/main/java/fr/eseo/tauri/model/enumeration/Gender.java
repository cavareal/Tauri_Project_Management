package fr.eseo.tauri.model.enumeration;

import lombok.Getter;

@Getter
public enum Gender {
    WOMEN("Femme"),
    MAN("Homme"),
    OTHER("Autre");

    private final String genderName;

    Gender(String genderName) {
        this.genderName = genderName;
    }

}
