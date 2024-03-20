package fr.eseo.tauri.model.enumeration;

import lombok.Getter;

@Getter
public enum NotificationType {
    BONUS_MALUS("Bonus Malus");

    private final String type;

    NotificationType(String type) {
        this.type = type;
    }
}
