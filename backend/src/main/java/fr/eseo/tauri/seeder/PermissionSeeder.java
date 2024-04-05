package fr.eseo.tauri.seeder;

import fr.eseo.tauri.model.Permission;
import fr.eseo.tauri.model.enumeration.PermissionType;
import fr.eseo.tauri.model.enumeration.RoleType;
import fr.eseo.tauri.repository.PermissionRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class PermissionSeeder {

    private final Map<RoleType, List<PermissionType>> associationTable = Map.of(
            RoleType.IDENTIFIED_USER, List.of(PermissionType.LOGIN_OUT, PermissionType.VIEW_GRADE_SCALE, PermissionType.IMPORT_GENERATED_KEY),
            RoleType.SYSTEM_ADMINISTRATOR, List.of(PermissionType.IMPORT_GENERATED_KEY),
            RoleType.TECHNICAL_COACH, List.of(PermissionType.GRADE_SUPPORT_MATERIAL, PermissionType.GRADE_PRESENTATION_CONTENT, PermissionType.ADD_COMMENT_WG, PermissionType.ADD_ALL_TEAMS_COMMENT,  PermissionType.ADD_ALL_TEAMS_FEEDBACK),
            RoleType.SUPERVISING_STAFF, List.of(PermissionType.PREVIEW_TEAM, PermissionType.VALIDATION_TEAM_BEFORE_PREPUBLISH, PermissionType.FLAG_TEAM_WITHOUT_STUDENTS, PermissionType.FLAG_TEAM_WITH_STUDENTS, PermissionType.VIEW_TEAM_CHANGES, PermissionType.VIEW_TEAMS, PermissionType.VIEW_OWN_TEAM, PermissionType.VIEW_SPRINT_PROTOCOL, PermissionType.VIEW_OWN_TEAM_WITH_CRITERIA, PermissionType.VIEW_TEAMS_INFORMATIONS, PermissionType.GRADE_PRESENTATION_CONTENT, PermissionType.GRADE_SUPPORT_MATERIAL, PermissionType.GRADE_INDIVIDUAL_PERFORMANCE, PermissionType.ADD_GRADE_COMMENT, PermissionType.VIEW_ALL_ORAL_GRADES, PermissionType.GRADE_OWN_TEAM, PermissionType.VIEW_OWN_TEAM_COMMENT, PermissionType.GIVE_UNLIMITED_BONUS_MALUS, PermissionType.VALIDATION_LIMITED_BONUS_MALUS, PermissionType.VALIDATION_OWN_TEAM_GRADES, PermissionType.USE_KEY_OWN_TEAM, PermissionType.VERIFY_OWN_TEAM_KEY_USED, PermissionType.USE_KEY_ALL_TEAMS, PermissionType.VIEW_ALL_SPRINTS_GRADES, PermissionType.ADD_ALL_TEAMS_FEEDBACK, PermissionType.ADD_ALL_TEAMS_COMMENT, PermissionType.VIEW_FEEDBACK, PermissionType.VIEW_COMMENT),
            RoleType.OPTION_LEADER, List.of(PermissionType.IMPORT, PermissionType.MODIFICATION_STUDENT_LIST, PermissionType.MANAGE_SPRINT, PermissionType.VIEW_ALL_WRITING_GRADES, PermissionType.EXPORT_INDIVIDUAL_GRADES),
            RoleType.PROJECT_LEADER, List.of(PermissionType.IMPORT, PermissionType.MODIFICATION_STUDENT_LIST, PermissionType.EXPORT_STUDENT_LIST, PermissionType.TEAM_CREATION, PermissionType.TEAM_MANAGEMENT, PermissionType.PUBLISH_TEAMS, PermissionType.MANAGE_SPRINT, PermissionType.MANAGE_GRADE_SCALE, PermissionType.VIEW_ALL_WRITING_GRADES, PermissionType.VERIFY_ALL_KEYS_USED, PermissionType.EXPORT_INDIVIDUAL_GRADES),
            RoleType.OPTION_STUDENT, List.of(PermissionType.FLAG_TEAM_WITH_STUDENTS, PermissionType.VIEW_TEAM_CHANGES, PermissionType.VALIDATION_OWN_TEAM, PermissionType.VIEW_TEAMS, PermissionType.VIEW_OWN_TEAM, PermissionType.VIEW_SPRINT_PROTOCOL, PermissionType.VIEW_OWN_SPRINT_GRADE),
            RoleType.TEAM_MEMBER, List.of(PermissionType.GRADE_GLOBAL_PERFORMANCE, PermissionType.PUBLISH_RUNNING_ORDER, PermissionType.VIEW_OWN_GRADE_COMMENT, PermissionType.VIEW_OWN_TEAM_GRADE, PermissionType.VIEW_TEAM_GRADE, PermissionType.LIMITED_BONUS_MALUS, PermissionType.VIEW_OWN_GRADES_WG, PermissionType.USE_KEY_OWN_TEAM, PermissionType.VIEW_OWN_SPRINT_GRADE),
            RoleType.ESEO_ADMINISTRATION, List.of(),
            RoleType.JURY_MEMBER, List.of()
    );

    private final PermissionRepository permissionRepository;

    public PermissionSeeder(PermissionRepository permissionRepository) {
        this.permissionRepository = permissionRepository;
    }

    private void attributePermission(RoleType role, PermissionType permission) {
        var permissionEntity = new Permission();

        permissionEntity.role(role);
        permissionEntity.type(permission);

        permissionRepository.save(permissionEntity);
    }

    public void seed() {
        RoleType role;
        for (var entry : associationTable.entrySet()) {
            role = entry.getKey();
            var permissions = entry.getValue();

            for (var permission : permissions) {
                attributePermission(role, permission);
            }
        }
    }
}
