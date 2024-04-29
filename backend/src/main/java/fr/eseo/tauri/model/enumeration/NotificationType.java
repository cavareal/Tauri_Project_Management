package fr.eseo.tauri.model.enumeration;

import lombok.Getter;

@Getter
public enum NotificationType {
    BONUS_MALUS("Bonus Malus"),
    CREATE_TEAMS("generation of teams");

    private final String type;

    NotificationType(String type) {
        this.type = type;
    }
}
