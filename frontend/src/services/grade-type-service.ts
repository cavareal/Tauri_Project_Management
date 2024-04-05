import { GradeTypeSchema } from "@/types/grade-type"
import { apiQuery } from "@/utils/api"
import { z } from "zod"

export const getAllImportedGradeTypes = async() => {
	const response = await apiQuery({
		responseSchema: z.array(GradeTypeSchema),
		method: "GET",
		route: "grade_types"
	})

	if (response.status === "error") {
		throw new Error(response.error)
	}

	const meanIndex = response.data.findIndex(gradeType => gradeType.name === "mean")
	if (meanIndex !== -1) {
		const mean = response.data[meanIndex]
		response.data.splice(meanIndex, 1)
		response.data.unshift(mean)
	}

	return response.data.filter(gradeType => gradeType.imported)
}