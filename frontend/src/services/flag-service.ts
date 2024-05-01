import { mutateAndValidate } from "@/utils/api"
import { FlagWithoutIdSchema, type FlagWithoutId } from "@/types/flag"
import { FlagType } from "@/types/flag"
import type { User } from "@/types/user"

export const addFlag = async(flag: FlagWithoutId): Promise<void> => {
	const response = await mutateAndValidate({
		method: "POST",
		body: flag,
		route: "flags/",
		bodySchema: FlagWithoutIdSchema
	})
	console.log(response)

	if (response.status === "error") {
		throw new Error(response.error)
	}
}

export const createValidationFlag = async(author: User): Promise<void> => {
	const flag: FlagWithoutId = {
		description: "Validation des équipes",
		type: FlagType.VALIDATION as string,
		firstStudent: null,
		secondStudent: null,
		author
	}
	await addFlag(flag)
}