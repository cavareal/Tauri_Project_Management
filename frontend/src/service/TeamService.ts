import { apiQuery } from "@/utils/api"
import { TeamSchema } from "@/type/Team"
import type { Team } from "@/type/Team"

export async function getTeams(): Promise<Team[]> {
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