import { ProjectPhaseSchema, type ProjectPhase, type Project, ProjectSchema } from "@/types/project"
import { apiQuery } from "@/utils/api"
import { z } from "zod"

export const getCurrentPhase = async(idProject : string | null): Promise<ProjectPhase> => {
	const response = await apiQuery({
		responseSchema: ProjectPhaseSchema,
		method: "GET",
		route: `projects/phase/${idProject}`,
		textResponse: true
	})

	if (response.status === "error") {
		throw new Error(response.error)
	}

	return response.data
}

//A supprimer
export const setCurrentPhase = async(phase: ProjectPhase | null, idProject : string | null): Promise<void> => {
	const response = await apiQuery({
		method: "PUT",
		route: `projects/phase/${idProject}`,
		body: { phase },
		responseSchema: z.string(),
		textResponse: true
	})

	if (response.status === "error") {
		throw new Error(response.error)
	}
}

export const getProjectById = async(id : string | null): Promise<Project> => {
	const response = await apiQuery({
		responseSchema: ProjectSchema,
		method: "GET",
		route: `projects/${id}`,
		textResponse: true
	})

	if (response.status === "error") {
		throw new Error(response.error)
	}

	return response.data
}

export const updateProject = async(id: string | null, nbTeams: number | null, nbWomen: number | null, phase: ProjectPhase | null): Promise<void> => {
	const response = await apiQuery({
		method: "PATCH",
		route: `projects/${id}`,
		body: { nbTeams, nbWomen, phase },
		responseSchema: z.string(),
		textResponse: true
	})

	if (response.status === "error") {
		throw new Error(response.error)
	}
}