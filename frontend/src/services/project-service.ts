import { ProjectPhaseSchema, type ProjectPhase } from "@/types/project"
import { apiQuery } from "@/utils/api"
import { z } from "zod"

export const getCurrentPhase = async(): Promise<ProjectPhase> => {
	const response = await apiQuery({
		responseSchema: ProjectPhaseSchema,
		method: "GET",
		route: "projects/current-phase",
		textResponse: true
	})

	if (response.status === "error") {
		throw new Error(response.error)
	}

	return response.data
}

export const setCurrentPhase = async(phase: ProjectPhase): Promise<void> => {
	const response = await apiQuery({
		method: "PUT",
		route: "projects/current-phase",
		body: { phase },
		responseSchema: z.string(),
		textResponse: true
	})

	if (response.status === "error") {
		throw new Error(response.error)
	}
}