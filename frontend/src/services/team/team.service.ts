import { mutateAndValidate, queryAndValidate } from "@/utils/api"
import type { Team, UpdateTeam } from "@/types/team"
import { TeamSchema } from "@/types/team"
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

export const getTeamByUserId = async(userId: number): Promise<Team | null> => {
	const response = await queryAndValidate({
		route: `users/${userId}/team`,
		responseSchema: TeamSchema.or(z.literal(""))
	})

	if (response.status === "error") {
		throw new Error(response.error)
	}
	console.log("response.data : ", response.data)
	return response.data === "" ? null : response.data
}