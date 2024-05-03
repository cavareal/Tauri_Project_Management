import { ProjectPhaseSchema, type ProjectPhase, type Project, ProjectSchema } from "@/types/project"
import { mutateAndValidate, queryAndValidate } from "@/utils/api"

export const getCurrentPhase = async(projectId : string | null): Promise<ProjectPhase> => {
	const response = await queryAndValidate({
		route: `projects/phase/${projectId}`,
		responseSchema: ProjectPhaseSchema
	})

	if (response.status === "error") {
		throw new Error(response.error)
	}

	return response.data
}

export const getProjectById = async(id : string | null): Promise<Project> => {
	const response = await queryAndValidate({
		responseSchema: ProjectSchema,
		route: `projects/${id}`
	})

	if (response.status === "error") {
		throw new Error(response.error)
	}

	return response.data
}

export const updateProject = async(id: string | null, body: Partial<Omit<Project, "id">>): Promise<void> => {
	const response = await mutateAndValidate({
		method: "PATCH",
		route: `projects/${id}`,
		body,
		bodySchema: ProjectSchema.omit({ id: true }).partial()
	})

	if (response.status === "error") {
		throw new Error(response.error)
	}
}