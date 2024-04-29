import { apiQuery, mutateAndValidate, queryAndValidate } from "@/utils/api"
import { TeamSchema } from "@/types/team"
import type { Team } from "@/types/team"
import { z } from "zod"
import type { Criteria } from "@/types/criteria"
import { CriteriaSchema } from "@/types/criteria"

export const getTeams = async(): Promise<Team[]> => {
	const response = await queryAndValidate({
		route: "teams",
		responseSchema: TeamSchema.array()
	})

	if (response.status === "error") {
		throw new Error(response.error)
	}

	return response.data
}

export const getTeamById = async(id: number): Promise<Team> => {
	const response = await queryAndValidate({
		route: `teams/${id}`,
		responseSchema: TeamSchema
	})

	if (response.status === "error") {
		throw new Error(response.error)
	}

	return response.data
}

export const setTeamName = async(id: number, value: string): Promise<void> => {
	const response = await mutateAndValidate({
		method: "PUT",
		route: `teams/update-name-team/${id}`,
		params: { newName: value }
	})

	if (response.status === "error") {
		throw new Error(response.error)
	}
}

export const setTeamLeader = async(id: number, value: string): Promise<void> => {
	const response = await mutateAndValidate({
		method: "PUT",
		route: `teams/update-leader-team/${id}`,
		params: { idLeader: value }
	})

	if (response.status === "error") {
		throw new Error(response.error)
	}
}

export const generateTeams = async(nbTeams: string, womenPerTeam: string): Promise<void> => {
	const response = await mutateAndValidate({
		method: "POST",
		route: "teams/",
		body: {
			nbTeams: parseInt(nbTeams),
			womenPerTeam: parseInt(womenPerTeam)
		},
		bodySchema: z.object({
			nbTeams: z.number(),
			womenPerTeam: z.number()
		})
	})

	if (response.status === "error") {
		throw new Error(response.error)
	}
}

export const getCriteria = async(teamId: number): Promise<Criteria> => {
	const response = await queryAndValidate({
		route: `teams/${teamId}/criteria`,
		responseSchema: CriteriaSchema
	})

	if (response.status === "error") {
		throw new Error(response.error)
	}

	return response.data
}

export const getTeamAverage = async(teamId: number): Promise<number> => {
	const response = await queryAndValidate({
		route: `teams/${teamId}/average`,
		responseSchema: z.number()
	})

	if (response.status === "error") {
		throw new Error(response.error)
	}

	return response.data
}

export const deleteAllTeams = async(): Promise<void> => {
	const response = await mutateAndValidate({
		method: "DELETE",
		route: "teams"
	})

	if (response.status === "error") {
		throw new Error(response.error)
	}
}

export const getTeamBySSId = async(ssId: string | null): Promise<Team> => {
	const response = await queryAndValidate({
		route: `teams/ss/${ssId}`,
		responseSchema: TeamSchema
	})

	if (response.status === "error") {
		throw new Error(response.error)
	}

	return response.data
}

export const moveTeamStudent = async(teamId: number, studentId: number): Promise<void> => {
	const response = await mutateAndValidate({
		method: "PUT",
		route: `teams/${teamId}/move-student`,
		params: { studentId: studentId.toString() }
	})

	if (response.status === "error") {
		throw new Error(response.error)
	}
}