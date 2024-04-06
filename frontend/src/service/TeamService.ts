import { apiQuery } from "@/utils/api"
import { TeamSchema } from "@/type/Team"
import type { Team } from "@/type/Team"
import type { Criteria } from "@/type/Criteria"
import { CriteriaSchema } from "@/type/Criteria"

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

export async function getCriteria(teamId: number): Promise<Criteria> {
	const response = await apiQuery({
		route: `teams/${teamId}/criteria`,
		responseSchema: CriteriaSchema,
		method: "GET"
	})

	if (response.status === "error") {
		throw new Error(response.error)
	}
	return response.data
}