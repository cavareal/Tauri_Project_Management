import { z } from "zod"

export const RoleTypeSchema = z.enum([
	"SUPERVISING_STAFF",
	"OPTION_LEADER",
	"PROJECT_LEADER",
	"OPTION_STUDENT",
	"TEAM_MEMBER",
	"SYSTEM_ADMINISTRATOR",
	"TECHNICAL_COACH",
	"JURY_MEMBER",
	"ESEO_ADMINISTRATION",
	"IDENTIFIED_USER"
])

export type RoleType = z.infer<typeof RoleTypeSchema>