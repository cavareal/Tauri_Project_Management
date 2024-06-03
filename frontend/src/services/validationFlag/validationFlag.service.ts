import { queryAndValidate } from "@/utils/api"
import { ValidationFlagSchema } from "@/types/validationFlag"
import type { ValidationFlag } from "@/types/validationFlag"

export const getValidationFlagsByFlagId = async(flagId: number): Promise<ValidationFlag[]> => {
	const response = await queryAndValidate({
		route: `flags/${flagId}/validation`,
		responseSchema: ValidationFlagSchema.array()
	})

	if (response.status === "error") {
		throw new Error(response.error)
	}

	return response.data
}