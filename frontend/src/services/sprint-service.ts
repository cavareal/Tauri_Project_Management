import type { Sprint } from "@/types/sprint"
import { SprintSchema } from "@/types/sprint"
import { mutateAndValidate, queryAndValidate } from "@/utils/api"
import { z } from "zod"
import { getCookie } from "@/utils/cookie"


export const getAllSprints = async (): Promise<Sprint[]> => {

	const currentProjectId = getCookie("currentProject")
	console.log("getAllSprints", currentProjectId)

	const response = await queryAndValidate({
		responseSchema: SprintSchema.array(),
		params: { projectId: "1" },
		route: "sprints"
	})

	if (response.status === "error") {
		throw new Error(response.error)
	}

	return response.data
}

export const addSprint = async (sprint: unknown): Promise<void> => {

	const response = await mutateAndValidate({
		method: "POST",
		route: `sprints/`,
		body: sprint,
		bodySchema: z.unknown()
	})

	if (response.status === "error") {
		throw new Error(response.error)
	}
}