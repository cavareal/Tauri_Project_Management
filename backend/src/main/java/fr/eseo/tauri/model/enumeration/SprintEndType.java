package fr.eseo.tauri.model.enumeration;

import lombok.Getter;
@Getter
public enum SprintEndType {

        CLASSIC_SPRINT("Sprint Classique"),
        FINAL_SPRINT("Sprint Final");

        private final String sprintEndType;

        SprintEndType(String sprintEndType) {
            this.sprintEndType = sprintEndType;
        }
}
