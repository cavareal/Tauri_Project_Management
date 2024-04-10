package fr.eseo.tauri.model.enumeration;

import lombok.Getter;
@Getter
public enum SprintEndType {

        NORMAL_SPRINT("Sprint Normal"),
        FINAL_SPRINT("Sprint Final");

        private final String endType;

        SprintEndType(String sprintEndType) {
            this.endType = sprintEndType;
        }
}
