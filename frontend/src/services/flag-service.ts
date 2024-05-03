import { mutateAndValidate, queryAndValidate } from "@/utils/api"
import { FlagWithoutIdSchema, type FlagWithoutId, FlagSchema } from "@/types/flag"
import type { User } from "@/types/user"
import { z } from "zod"

export const addFlag = async(flag: FlagWithoutId): Promise<void> => {
	const response = await mutateAndValidate({
		method: "POST",
		body: flag,
		route: "flags",
		bodySchema: FlagWithoutIdSchema
	})
	console.log(response)

	if (response.status === "error") {
		throw new Error(response.error)
	}
}

export const createValidationFlag = async(author: User): Promise<void> => {
	const flag: FlagWithoutId = {
		description: "Validation des équipes prépubliées",
		type: "VALIDATION",
		firstStudent: null,
		secondStudent: null,
		author
	}
	await addFlag(flag)
}

export const createReportingFlag = async(author: User,  description: string): Promise<void> => {
	const flag: FlagWithoutId = {
		description,
		type: "REPORTING",
		firstStudent: null,
		secondStudent: null,
		author
	}
	await addFlag(flag)
}

export const userHasValidateTeams = async(authorId: string, description: string): Promise<boolean> => {
	const response = await queryAndValidate({
		route: `flags/author/${authorId}/description/${description}`,
		responseSchema: z.array(FlagSchema)
	})

	if (response.status === "error") {
		throw new Error(response.error)
	}

	return response.data.length > 0
}