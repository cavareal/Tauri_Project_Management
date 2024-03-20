package fr.eseo.tauri.model.enumeration;

import lombok.Getter;

@Getter
public enum PermissionType {
    TEAM_CREATION("Team Creation"),
    TEAM_SUPPRESSION("Team Suppression"),
    TEAM_PUBLICATION("Team Publication"),
    RATE("Rate"),
    ATTRIBUTION_BONUS_MALUS("Attribution Bonus/Malus"),
    MANAGE_SPRINTS("Manage Sprints"),
    VIEW_OTHER_TEAM_MARK("View Other Team Mark"),
    VIEW_OWN_TEAM_MARK("View Own Team Mark"),
    VIEW_MARK_ALL_STUDENTS("View Mark for All Students"),
    VIEW_MARK("View Mark");

    private final String displayName;

    PermissionType(String displayName) {
        this.displayName = displayName;
    }

}
