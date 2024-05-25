import { mutateAndValidate, queryAndValidate } from "@/utils/api"
import { FlagSchema, type CreateFlag, CreateFlagSchema } from "@/types/flag"
import { z } from "zod"
import { getConnectedUser } from "@/services/user"
import { Cookies } from "@/utils/cookie"

export const createFlag = async(body: Omit<CreateFlag, "authorId" | "projectId">): Promise<void> => {
	const user = await getConnectedUser()
	const projectId = Cookies.getProjectId()

	const response = await mutateAndValidate({
		method: "POST",
		route: "flags",
		body: { ...body, authorId: user.id, projectId },
		bodySchema: CreateFlagSchema
	})

	if (response.status === "error") {
		throw new Error(response.error)
	}
}

export const createValidationFlag = async(): Promise<void> => {
	await createFlag({ type: "VALIDATION" })
}

export const createReportingFlag = async(description: string): Promise<void> => {
	await createFlag({ description, type: "REPORTING" })
}

export const userHasValidateTeams = async(authorId: number): Promise<boolean> => {
	const response = await queryAndValidate({
		route: `flags/author/${authorId}/type/VALIDATION`,
		responseSchema: z.array(FlagSchema)
	})

	if (response.status === "error") {
		throw new Error(response.error)
	}

	return response.data.length > 0
}