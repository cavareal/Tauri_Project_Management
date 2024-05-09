import { mutateAndValidate } from "@/utils/api"
import { CreateSprintSchema, type CreateSprint } from "@/types/sprint"
import { Cookies } from "@/utils/cookie"

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

//TO-DO: Implement getCurrentSprint
// eslint-disable-next-line @typescript-eslint/require-await
export const getCurrentSprint = async(): Promise<number> => {
	return 1
}