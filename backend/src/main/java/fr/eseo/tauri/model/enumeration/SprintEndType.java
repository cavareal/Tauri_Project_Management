package fr.eseo.tauri.model.enumeration;

import lombok.Getter;
@Getter
public enum SprintEndType {

        NORMAL_SPRINT("Sprint Normal"),
        FINAL_SPRINT("Sprint Final");

        private final String sprintEndType;

        SprintEndType(String sprintEndType) {
            this.sprintEndType = sprintEndType;
        }
}
