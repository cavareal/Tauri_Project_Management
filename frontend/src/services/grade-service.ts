import { GradeDoubleArraySchema, GradeSchema, type Grade } from "@/types/grade"
import { apiQuery } from "@/utils/api"
import { z } from "zod"
import type { GradeType } from "@/types/grade-type"
import type { User } from "@/types/user"
import type { Student } from "@/types/student"
import type { Team } from "@/types/team"
//import type { Sprint } from "@/types/sprint"

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
		route: `grades/average-grades-by-grade-type-by-role/${userId}`,
		responseSchema: GradeDoubleArraySchema,
		method: "GET"
	})

	if (response.status === "error") {
		throw new Error(response.error)
	}

	return response.data
}

export const updateStudentGrade = async(id: string | null, value: number | null, comment: string | null, gradeType: GradeType | null, author: User | null, student: Student | null/*, sprint: Sprint | null*/): Promise<void> => {
	const response = await apiQuery({
		method: "PUT",
		route: `grades/student/${id}`,
		body: { value, comment, gradeType, author, student },
		responseSchema: z.string(),
		textResponse: true
	})

	if (response.status === "error") {
		throw new Error(response.error)
	}
}

export const updateTeamGrade = async(id: string | null, value: number | null, comment: string | null, gradeType: GradeType | null, author: User | null, team: Team | null/*, sprint: Sprint | null*/): Promise<void> => {
	const response = await apiQuery({
		method: "PUT",
		route: `grades/team/${id}`,
		body: { value, comment, gradeType, author, team },
		responseSchema: z.string(),
		textResponse: true
	})

	if (response.status === "error") {
		throw new Error(response.error)
	}
}