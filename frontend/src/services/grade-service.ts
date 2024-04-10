import { GradeDoubleArraySchema, GradeSchema, type Grade } from "@/types/grade"
import { apiQuery } from "@/utils/api"
import { z } from "zod"

export const getAllGrades = async(): Promise<Grade[]> => {
	const response = await apiQuery({
		route: "grades",
		responseSchema: z.array(GradeSchema),
		method: "GET"
	})

	if (response.status === "error") {
		throw new Error(response.error)
	}

	return response.data
}

export const getAverageGrades = async(userId: number): Promise<z.infer<typeof GradeDoubleArraySchema>> => {
	const response = await apiQuery({
		route: `grades/averageGradesByGradeTypeByRole/${userId}`,
		responseSchema: GradeDoubleArraySchema,
		method: "GET"
	})

	if (response.status === "error") {
		throw new Error(response.error)
	}

	return response.data
}