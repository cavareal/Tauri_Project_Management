import { ProjectPhaseSchema, type ProjectPhase } from "@/types/project"
import { mutateAndValidate, queryAndValidate } from "@/utils/api"
import { z } from "zod"

export const getCurrentPhase = async(): Promise<ProjectPhase> => {
	const response = await queryAndValidate({
		route: "projects/current-phase",
		responseSchema: ProjectPhaseSchema
	})

	if (response.status === "error") {
		throw new Error(response.error)
	}

	return response.data
}

export const setCurrentPhase = async(phase: ProjectPhase): Promise<void> => {
	const response = await mutateAndValidate({
		method: "PUT",
		route: "projects/current-phase",
		body: { phase },
		bodySchema: z.object({
			phase: ProjectPhaseSchema
		})
	})

	if (response.status === "error") {
		throw new Error(response.error)
	}
}