import { z } from "zod"

export const PermissionTypeSchema = z.enum([
	"LOGIN_OUT",
	"LDAP",
	"IMPORT_GENERATED_KEY",
	"IMPORT",
	"MODIFICATION_STUDENT_LIST",
	"EDIT_IMPORTED_GRADE_TYPES",
	"EXPORT_STUDENT_LIST",
	"TEAM_CREATION",
	"PREVIEW_TEAM",
	"VALIDATION_TEAM_BEFORE_PREPUBLISH",
	"FLAG_TEAM_WITHOUT_STUDENTS",
	"FLAG_TEAM_WITH_STUDENTS",
	"TEAM_MANAGEMENT",
	"VIEW_TEAM_CHANGES",
	"VALIDATION_OWN_TEAM",
	"VIEW_TEAMS",
	"VIEW_OWN_TEAM",
	"PUBLISH_TEAMS",
	"MANAGE_SPRINT",
	"MANAGE_GRADE_SCALE",
	"VIEW_SPRINT_PROTOCOL",
	"VIEW_OWN_TEAM_WITH_CRITERIA",
	"VIEW_TEAMS_INFORMATIONS",
	"GRADE_GLOBAL_PERFORMANCE",
	"PUBLISH_RUNNING_ORDER",
	"GRADE_SUPPORT_MATERIAL",
	"GRADE_PRESENTATION_CONTENT",
	"GRADE_INDIVIDUAL_PERFORMANCE",
	"GRADE_TECHNICAL_SOLUTION",
	"GRADE_SPRINT_CONFORMITY",
	"GRADE_CONFIRMATION",
	"GRADE_PROJECT_MANAGEMENT",
	"COMMENT_SUPPORT_MATERIAL",
	"COMMENT_PRESENTATION_CONTENT",
	"COMMENT_INDIVIDUAL_PERFORMANCE",
	"COMMENT_TECHNICAL_SOLUTION",
	"COMMENT_SPRINT_CONFORMITY",
	"COMMENT_PROJECT_MANAGEMENT",
	"VIEW_OWN_GRADE_COMMENT",
	"VIEW_OWN_TEAM_GRADE",
	"VIEW_TEAM_GRADE",
	"VIEW_ALL_ORAL_GRADES",
	"VIEW_GRADE_SCALE",
	"ADD_COMMENT_WG",
	"VIEW_OWN_TEAM_COMMENT",
	"GIVE_UNLIMITED_BONUS_MALUS",
	"LIMITED_BONUS_MALUS",
	"VALIDATION_LIMITED_BONUS_MALUS",
	"VALIDATION_OWN_TEAM_GRADES",
	"VIEW_OWN_GRADES_WG",
	"VIEW_ALL_WRITING_GRADES",
	"USE_KEY_OWN_TEAM",
	"VERIFY_OWN_TEAM_KEY_USED",
	"VERIFY_ALL_KEYS_USED",
	"USE_KEY_ALL_TEAMS",
	"VIEW_OWN_SPRINT_GRADE",
	"VIEW_ALL_SPRINTS_GRADES",
	"EXPORT_INDIVIDUAL_GRADES",
	"ADD_ALL_TEAMS_FEEDBACK",
	"ADD_ALL_TEAMS_COMMENT",
	"VIEW_FEEDBACK",
	"VIEW_COMMENT",
	"STUDENTS_PAGE",
	"TEAMS_PAGE",
	"MY_TEAM_PAGE",
	"SPRINTS_PAGE",
	"GRADES_PAGE",
	"RATING_PAGE",
	"GRADE_SCALES_PAGE"
])

export type PermissionType = z.infer<typeof PermissionTypeSchema>