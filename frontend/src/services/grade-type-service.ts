import { GradeTypeSchema } from "@/types/grade-type"
import { mutateAndValidate, queryAndValidate } from "@/utils/api"
import { z } from "zod"

export const getAllImportedGradeTypes = async() => {
	const response = await queryAndValidate({
		responseSchema: z.array(GradeTypeSchema),
		route: "grade-types/imported"
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
		bodySchema: z.object({ factor: z.number() })
	})

	if (response.status === "error") {
		throw new Error(response.error)
	}
}

export const updateGradeType = async(id: string | null, name: string | null, factor: number | null, group: boolean | null, imported: boolean | null, scaleURL: string | null): Promise<void> => {
	const response = await mutateAndValidate({
		method: "PUT",
		route: `grade-types/${id}`,
		body: { name, factor, group, imported, scaleURL },
		bodySchema: z.any()
	})

	if (response.status === "error") {
		throw new Error(response.error)
	}
}