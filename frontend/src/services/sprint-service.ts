import { mutateAndValidate, queryAndValidate } from "@/utils/api"
import { z } from "zod"
import { SprintSchema } from "@/types/sprint"



export const addSprint = async(sprint: unknown): Promise<void> => {
	
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