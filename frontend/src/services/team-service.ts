import { apiQuery } from "@/utils/api"
import { TeamSchema } from "@/types/team"
import type { Team } from "@/types/team"
import { z } from "zod"
import type { Criteria } from "@/types/criteria"
import { CriteriaSchema } from "@/types/criteria"

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

export const getTeamById = async(id: number): Promise<Team> => {
	const response = await apiQuery({
		route: `teams/${id}`,
		responseSchema: TeamSchema,
		method: "GET"
	})

	if (response.status === "error") {
		throw new Error(response.error)
	}

	return response.data
}

export const setTeamName = async(id: number, value: string): Promise<void> => {
	const response = await apiQuery({
		route: `teams/update-name-team/${id}?newName=${value}`,
		responseSchema: z.string(),
		method: "PUT",
		textResponse: true
	})

	if (response.status === "error") {
		throw new Error(response.error)
	}
}

export const setTeamLeader = async(id: number, value: string): Promise<void> => {
	const response = await apiQuery({
		route: `teams/update-leader-team/${id}?idLeader=${value}`,
		responseSchema: z.string(),
		method: "PUT",
		textResponse: true
	})

	if (response.status === "error") {
		throw new Error(response.error)
	}
}

export const generateTeams = async(nbTeams: string, ratioGender: string): Promise<void> => {
	const response = await apiQuery({
		route: "teams/create-teams",
		responseSchema: z.string(),
		method: "POST",
		body: { nbTeams, ratioGender },
		textResponse: true
	})

	if (response.status === "error") {
		throw new Error(response.error)
	}
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