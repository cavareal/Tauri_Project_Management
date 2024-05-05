import { mutateAndValidate, queryAndValidate } from "@/utils/api"
import type { Team, UpdateTeam } from "@/types/team"
import { TeamSchema } from "@/types/team"
import { z } from "zod"
import type { Criteria } from "@/types/criteria"
import { CriteriaSchema } from "@/types/criteria"
import { Cookies } from "@/utils/cookie"

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

export const updateTeam = async(id: number, body: UpdateTeam): Promise<void> => {
	const response = await mutateAndValidate({
		route: `teams/${id}`,
		method: "PATCH",
		body,
		bodySchema: z.any()
	})

	if (response.status === "error") {
		throw new Error(response.error)
	}
}

export const setTeamName = async(id: number, value: string): Promise<void> => {
	const response = await mutateAndValidate({
		route: `teams/update-name-team/${id}`,
		params: { newName: value },
		method: "PUT"
	})

	if (response.status === "error") {
		throw new Error(response.error)
	}
}

export const setTeamLeader = async(id: number, value: string): Promise<void> => {
	const response = await mutateAndValidate({
		route: `teams/update-leader-team/${id}`,
		params: { idLeader: value },
		method: "PUT"
	})

	if (response.status === "error") {
		throw new Error(response.error)
	}
}

export const generateTeams = async(nbTeams: string, nbWomen: string): Promise<void> => {
	const response = await mutateAndValidate({
		method: "POST",
		route: "teams",
		body: { nbTeams, nbWomen },
		bodySchema: z.any()
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

export const getTeamByUserId = async(userId: number): Promise<Team> => {
	const response = await queryAndValidate({
		route: `users/${userId}/team`,
		responseSchema: z.array(TeamSchema)
	})

	if (response.status === "error") {
		throw new Error(response.error)
	}

	const currentProjectId = Cookies.getProjectId()
	const team = response.data.find((team: Team) => team.project.id === currentProjectId)

	if (!team) {
		throw new Error("User is not in a team")
	}

	return team
}