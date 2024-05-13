import type { Sprint } from "@/types/sprint"
import { SprintSchema } from "@/types/sprint"
import { mutateAndValidate, queryAndValidate } from "@/utils/api"
import { z } from "zod"
import { Cookies } from "@/utils/cookie"


export const getAllSprints = async (): Promise<Sprint[]> => {

	const currentProjectId = Cookies.getProjectId().toString()

	const response = await queryAndValidate({
		responseSchema: SprintSchema.array(),
		params: { projectId: currentProjectId ?? "" },
		route: "sprints"
	})

	if (response.status === "error") {
		throw new Error(response.error)
	}

	response.data.sort((a, b) => a.sprintOrder - b.sprintOrder);

	return response.data
}

export const addSprint = async (sprint: unknown): Promise<void> => {
	const response = await mutateAndValidate({
		method: "POST",
		body: sprint,
		route: `sprints`,
		bodySchema: z.unknown()
	})

	if (response.status === "error") {
		throw new Error(response.error)
	}
}

export const updateSprint = async (sprint: unknown, sprintId: number): Promise<void> => {
	const response = await mutateAndValidate({
		method: "PATCH",
		body: sprint,
		route: `sprints/${sprintId}`,
		bodySchema: z.unknown()
	})

	if (response.status === "error") {
		throw new Error(response.error)
	}
}


export const deleteSprint = async(sprintId: number | null): Promise<void> => {
	const response = await mutateAndValidate({
		method: "DELETE",
		route: `sprints/${sprintId}`,
	})

	if (response.status === "error") {
		throw new Error(response.error)
	}
}
