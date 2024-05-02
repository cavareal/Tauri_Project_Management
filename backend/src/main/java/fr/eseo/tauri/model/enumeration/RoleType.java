package fr.eseo.tauri.model.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum RoleType {

    SUPERVISING_STAFF("Personnel Encadrant"),
    OPTION_LEADER("Chef d'Option"),
    PROJECT_LEADER("Chef de Projet"),
    OPTION_STUDENT("Etudiant de l'option"),
    TEAM_MEMBER("Membre de l'équipe"),
    SYSTEM_ADMINISTRATOR("Administrateur Système"),
    TECHNICAL_COACH("Coach Technique"),
    JURY_MEMBER("Membre du Jury"),
    ESEO_ADMINISTRATION("Administration ESEO"),
    IDENTIFIED_USER("Utilisateur Identifié");

    private final String displayName;

}
