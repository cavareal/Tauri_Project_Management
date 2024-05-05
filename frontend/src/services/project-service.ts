import { ProjectPhaseSchema, type ProjectPhase, type Project, ProjectSchema, type UpdateProject, UpdateProjectSchema } from "@/types/project"
import { mutateAndValidate, queryAndValidate } from "@/utils/api"
import { Cookies } from "@/utils/cookie"

export const getCurrentProject = async(): Promise<Project> => {
	const id = Cookies.getProjectId()
	if (id === null) {
		throw new Error("No project selected")
	}

	const response = await queryAndValidate({
		route: `projects/${id}`,
		responseSchema: ProjectSchema
	})

	if (response.status === "error") {
		throw new Error(response.error)
	}

	return response.data
}

export const getCurrentPhase = async(): Promise<ProjectPhase> => {
	const id = Cookies.getProjectId()
	if (id === null) {
		throw new Error("No project selected")
	}

	const response = await queryAndValidate({
		route: `projects/phase/${id}`,
		responseSchema: ProjectPhaseSchema
	})

	if (response.status === "error") {
		throw new Error(response.error)
	}

	return response.data
}

export const updateProject = async(body: UpdateProject): Promise<void> => {
	const id = Cookies.getProjectId()
	if (id === null) {
		throw new Error("No project selected")
	}

	const response = await mutateAndValidate({
		method: "PATCH",
		route: `projects/${id}`,
		body,
		bodySchema: UpdateProjectSchema
	})

	if (response.status === "error") {
		throw new Error(response.error)
	}
}