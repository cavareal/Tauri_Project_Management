
/**
 * Get the value of a cookie by its name
 * @param name the name of the cookie to get
 * @returns the value of the cookie if it exists, null otherwise
 */
export default function getCookie(name: string): string | null {
	const cookies = document.cookie
	const index = cookies.search(name)

	if (index === -1) return null

	let end = cookies.indexOf(";", index)
	end = end === -1 ? cookies.length : end
	return cookies.slice(index + name.length + 1, end)
}

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