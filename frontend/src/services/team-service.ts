import { apiQuery } from "@/utils/api"
import { TeamSchema } from "@/types/team"
import type { Team } from "@/types/team"

export const getTeams = async(): Promise<Team[]> => {
	const response = await apiQuery({
		route: "teams",
		responseSchema: TeamSchema.array(),
		method: "GET"
	})

	if (response.status === "error") {
		throw new Error(response.error)
	}

	return response.data
}