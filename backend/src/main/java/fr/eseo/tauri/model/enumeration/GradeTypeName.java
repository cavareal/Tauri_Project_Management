package fr.eseo.tauri.model.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum GradeTypeName {

    AVERAGE("Moyenne"),
    TECHNICAL_SOLUTION("Solution Technique"),
    SPRINT_CONFORMITY("Conformité du sprint"),
    PROJECT_MANAGEMENT("Gestion de projet"),
    PRESENTATION_CONTENT("Contenu de la présentation"),
    PRESENTATION_SUPPORT_MATERIAL("Support de présentation"),
    TEAM_PERFORMANCE("Performance de l'équipe"),
    INDIVIDUAL_PERFORMANCE("Performance individuelle");

    private final String displayName;

}