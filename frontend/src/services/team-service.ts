import { mutateAndValidate, queryAndValidate } from "@/utils/api"
import { TeamSchema } from "@/types/team"
import type { Team } from "@/types/team"
import { z } from "zod"
import type { Criteria } from "@/types/criteria"
import { CriteriaSchema } from "@/types/criteria"

export const getTeams = async(projectId: string | null): Promise<Team[]> => {
	const response = await queryAndValidate({
		route: "teams",
		params: { projectId: projectId ?? "" },
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

export const generateTeams = async(projectId: string | null, nbTeams: string, nbWomen: string): Promise<void> => {
	const response = await mutateAndValidate({
		method: "POST",
		route: "teams",
		params: { projectId: projectId ?? "" },
		body: { nbTeams, nbWomen },
		bodySchema: z.any()
	})

	if (response.status === "error") {
		throw new Error(response.error)
	}
}

export const getCriteria = async(projectId: string | null, teamId: number): Promise<Criteria> => {
	const response = await queryAndValidate({
		route: `teams/${teamId}/criteria`,
		params: { projectId: projectId ?? "" },
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

export const deleteAllTeams = async(projectId: string | null): Promise<void> => {
	const response = await mutateAndValidate({
		method: "DELETE",
		route: "teams",
		params: { projectId: projectId ?? "" }
	})

	if (response.status === "error") {
		throw new Error(response.error)
	}
}

export const getTeamByLeaderId = async(leaderId: string | null, projectId: string | null): Promise<Team> => {
	const response = await queryAndValidate({
		route: `teams/leader/${leaderId}`,
		params: { projectId: projectId ?? "" },
		responseSchema: TeamSchema
	})

	if (response.status === "error") {
		throw new Error(response.error)
	}

	return response.data
}