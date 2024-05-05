package fr.eseo.tauri.model.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum GradeTypeName {

    AVERAGE,
    TECHNICAL_SOLUTION,
    SPRINT_CONFORMITY,
    PROJECT_MANAGEMENT,
    PRESENTATION_CONTENT,
    PRESENTATION_SUPPORT_MATERIAL,
    TEAM_PERFORMANCE,
    INDIVIDUAL_PERFORMANCE;

}