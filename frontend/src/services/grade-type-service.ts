import { GradeTypeSchema } from "@/types/grade-type"
import { mutateAndValidate, queryAndValidate } from "@/utils/api"
import { z } from "zod"

export const getAllImportedGradeTypes = async() => {
	const response = await queryAndValidate({
		route: "grade-types",
		responseSchema: z.array(GradeTypeSchema)
	})

	if (response.status === "error") {
		throw new Error(response.error)
	}

	const meanIndex = response.data.findIndex(gradeType => gradeType.name.toLowerCase() === "mean")
	if (meanIndex !== -1) {
		const mean = response.data[meanIndex]
		response.data.splice(meanIndex, 1)
		response.data.unshift(mean)
	}

	const averageIndex = response.data.findIndex(gradeType => gradeType.name.toLowerCase() === "average")
	if (averageIndex !== -1) {
		const average = response.data[averageIndex]
		response.data.splice(averageIndex, 1)
		response.data.unshift(average)
	}

	return response.data.filter(gradeType => gradeType.imported)
}

export const updateGradeTypeFactor = async(id: number, factor: number): Promise<void> => {
	const response = await mutateAndValidate({
		method: "PATCH",
		route: `grade-types/${id}`,
		body: { factor },
		bodySchema: z.object({
			factor: z.number()
		})
	})

	if (response.status === "error") {
		throw new Error(response.error)
	}
}