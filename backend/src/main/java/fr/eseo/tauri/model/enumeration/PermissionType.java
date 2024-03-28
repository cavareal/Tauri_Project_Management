package fr.eseo.tauri.model.enumeration;

import lombok.Getter;

@Getter
public enum PermissionType {
    LOGIN_OUT("Login/Logout"),
    LDAP("LDAP"),
    IMPORT_GENERATED_KEY("Import Generated Key"),
    IMPORT("Import"),
    MODIFICATION_STUDENT_LIST("Modification Student List"),
    EXPORT_STUDENT_LIST("Export Student List"),
    TEAM_CREATION("Team Creation"),
    PREVIEW_TEAM("Preview Team"),
    FLAG_TEAM("Flag Team"),
    TEAM_MANAGEMENT("Team Management"),
    VALIDATION_TEAM("Validation Team"),
    VIEW_TEAMS("View Teams"),
    VIEW_OWN_TEAM("View Own Team"),
    PUBLISH_TEAMS("Publish Teams"),
    MANAGE_SPRINT("Manage Sprint"),
    MANAGE_GRADE_SCALE("Manage Grade Scale"),
    VIEW_SPRINT_PROTOCOL("View Sprint Protocol"),
    VIEW_OWN_TEAM_WITH_CRITERIA("View Own Team with Criteria"),
    VIEW_TEAMS_INFORMATIONS("View Teams Informations"),
    GRADE_GLOBAL_PERFORMANCE("Grade Global Performance"),
    PUBLISH_RUNNING_ORDER("Publish Running Order"),
    GRADE_SUPPORT_MATERIAL("Grade Support Material"),
    GRADE_PRESENTATION_CONTENT("Grade Presentation Content"),
    GRADE_INDIVIDUAL_PERFORMANCE("Grade Individual Performance"),
    ADD_GRADE_COMMENT("Add Grade Comment"),
    VIEW_OWN_GRADE_COMMENT("View Own Grade Comment"),
    VIEW_OWN_TEAM_GRADE("View Own Team Grade"),
    VIEW_TEAM_GRADE("View Team Grade"),
    VIEW_ALL_TEAM_STUDENT_GRADES("View All Team Student Grades"),
    VIEW_GRADE_SCALE("View Grade Scale"),
    GRADE_OWN_TEAM("Grade Own Team"),
    ADD_COMMENT_WG("Add Comment Wg"),
    VIEW_OWN_TEAM_COMMENT("View Own Team Comment"),
    GIVE_UNLIMITED_BONUS_MALUS("Give Unlimited Bonus Malus"),
    LIMITED_BONUS_MALUS("Limited Bonus Malus"),
    VALIDATION_LIMITED_BONUS_MALUS("Validation Limited Bonus Malus"),
    VALIDATION_OWN_TEAM_GRADES("Validation Own Team Grades"),
    VIEW_OWN_GRADES_WG("View Own Grades Wg"),
    USE_KEY_OWN_TEAM("Use Key Own Team"),
    VERIFY_OWN_TEAM_KEY_USED("Verify Own Team Key Used"),
    VERIFY_ALL_KEYS_USED("Verify All Keys Used"),
    USE_KEY_ALL_TEAMS("Use Key All Teams"),
    VIEW_OWN_SPRINT_GRADE("View Own Sprint Grade"),
    VIEW_ALL_SPRINT_GRADES("View All Sprint Grades"),
    EXPORT_INDIVIDUAL_GRADES("Export Individual Grades"),
    ADD_ALL_TEAMS_FEEDBACK("Add All Teams Feedback"),
    ADD_ALL_TEAMS_COMMENT("Add All Teams Comment"),
    VIEW_FEEDBACK("View Feedback"),
    VIEW_COMMENT("View Comment");

    private final String displayName;

    PermissionType(String displayName) {
        this.displayName = displayName;
    }

}
