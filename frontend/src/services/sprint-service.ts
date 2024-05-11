import { mutateAndValidate, queryAndValidate } from "@/utils/api"
import { CreateSprintSchema, type CreateSprint, SprintSchema } from "@/types/sprint"
import { Cookies } from "@/utils/cookie"
import type { Sprint } from "@/types/sprint"

export const createSprint = async(body: Omit<CreateSprint, "projectId">): Promise<void> => {
	const projectId = Cookies.getProjectId()

	const response = await mutateAndValidate({
		method: "POST",
		route: "sprints",
		body: { ...body, projectId },
		bodySchema: CreateSprintSchema
	})

	if (response.status === "error") {
		throw new Error(response.error)
	}
}

export const getSprints = async(): Promise<Sprint[]> => {
	const response = await queryAndValidate({
		route: "sprints",
		responseSchema: SprintSchema.array()
	})

	if (response.status === "error") {
		throw new Error(response.error)
	}

	return response.data
}