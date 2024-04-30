import { apiQuery } from "@/utils/api"
import { TeamSchema } from "@/types/team"
import type { Team } from "@/types/team"
import { z } from "zod"
import type { Criteria } from "@/types/criteria"
import { CriteriaSchema } from "@/types/criteria"
import type { User } from "@/types/user"

export const getTeams = async(idProject: string | null): Promise<Team[]> => {
	console.log("idProject", idProject)
	const response = await apiQuery({
		route: `teams?idProject=${idProject}`,
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

export const generateTeams = async(idProject: string | null, nbTeams: string, womenPerTeam: string): Promise<void> => {
	const response = await apiQuery({
		route: `teams?idProject=${idProject}`,
		responseSchema: z.string(),
		method: "POST",
		body: { nbTeams, womenPerTeam },
		textResponse: true
	})

	if (response.status === "error") {
		throw new Error(response.error)
	}
}

export const getCriteria = async(idProject: string | null, teamId: number): Promise<Criteria> => {
	const response = await apiQuery({
		route: `teams/${teamId}/criteria?idProject=${idProject}`,
		responseSchema: CriteriaSchema,
		method: "GET"
	})

	if (response.status === "error") {
		throw new Error(response.error)
	}
	return response.data
}

export const getTeamAverage = async(teamId: number): Promise<number> => {
	const response = await apiQuery({
		route: `teams/${teamId}/average`,
		responseSchema: z.number(),
		method: "GET"
	})

	if (response.status === "error") {
		throw new Error(response.error)
	}

	return response.data
}

export const deleteAllTeams = async(): Promise<void> => {
	const response = await apiQuery({
		route: "teams",
		responseSchema: z.string(),
		method: "DELETE",
		textResponse: true
	})

	if (response.status === "error") {
		throw new Error(response.error)
	}
}

export const getTeamByLeaderId = async(leaderId: string | null, idProject: string | null): Promise<Team> => {
	const response = await apiQuery({
		route: `teams/leader/${leaderId}?idProject=${idProject}`,
		responseSchema: TeamSchema,
		method: "GET"
	})

	if (response.status === "error") {
		throw new Error(response.error)
	}

	return response.data
}

export const moveTeamStudent = async(teamId: number, studentId: number): Promise<void> => {
	const response = await apiQuery({
		route: `teams/${teamId}/move-student?studentId=${studentId}`,
		responseSchema: TeamSchema,
		method: "PUT"
	})

	if (response.status === "error") {
		throw new Error(response.error)
	}
}