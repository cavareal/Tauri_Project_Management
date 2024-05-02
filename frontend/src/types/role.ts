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

export const formatRole = (role: string) => {
	switch (role) {
	case "SUPERVISING_STAFF":
		return "Professeur référent"
	case "OPTION_LEADER":
		return "Leader de l'option"
	case "PROJECT_LEADER":
		return "Leader du projet"
	case "OPTION_STUDENT":
		return "Étudiant"
	case "TEAM_MEMBER":
		return "Membre d'une équipe"
	case "SYSTEM_ADMINISTRATOR":
		return "Administrateur système"
	case "TECHNICAL_COACH":
		return "Coach technique"
	default:
		return role
	}
}