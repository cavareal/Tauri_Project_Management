import { GradeDoubleArraySchema, GradeSchema, type Grade, CreateGradeSchema } from "@/types/grade"
import { mutateAndValidate, queryAndValidate } from "@/utils/api"
import { type CreateGrade } from "@/types/grade"
import { z } from "zod"
import type { GradeType } from "@/types/grade-type"
import type { User } from "@/types/user"
import type { Student } from "@/types/student"
import type { Team } from "@/types/team"
import { getConnectedUser } from "@/services/user-service"


export const createGrade = async(body : Omit<CreateGrade, "authorId">): Promise<void> => {
	const user = await getConnectedUser()
	const response	= await mutateAndValidate({
		method: "POST",
		route: "grades",
		body: {
			...body,
			authorId: user.id
		},
		bodySchema: CreateGradeSchema
	})
	if (response.status === "error") {
		throw new Error(response.error)
	}
}

export const getAllImportedGrades = async(): Promise<Grade[]> => {
	const response = await queryAndValidate({
		route: "grades/imported",
		responseSchema: GradeSchema.array()
	})

	if (response.status === "error") {
		throw new Error(response.error)
	}

	return response.data
}

export const getAllUnimportedGrades = async(): Promise<Grade[]> => {
	const response = await queryAndValidate({
		route: "grades/unimported",
		responseSchema: GradeSchema.array()
	})

	if (response.status === "error") {
		throw new Error(response.error)
	}

	return response.data

}

export const getAverageGrades = async(userId: number): Promise<z.infer<typeof GradeDoubleArraySchema>> => {
	const response = await queryAndValidate({
		route: `grades/average-grades-by-grade-type-by-role/${userId}`,
		responseSchema: GradeDoubleArraySchema
	})

	if (response.status === "error") {
		throw new Error(response.error)
	}

	return response.data
}

export const updateStudentGrade = async(id: string | null, value: number | null, comment: string | null, gradeType: GradeType | null, author: User | null, student: Student | null/*, sprint: Sprint | null*/): Promise<void> => {
	const response = await mutateAndValidate({
		method: "PATCH",
		route: `grades/student/${id}`,
		body: { value, comment, gradeType, author, student },
		bodySchema: z.any()
	})

	if (response.status === "error") {
		throw new Error(response.error)
	}
}

export const updateTeamGrade = async(id: string | null, value: number | null, comment: string | null, gradeType: GradeType | null, author: User | null, team: Team | null/*, sprint: Sprint | null*/): Promise<void> => {
	const response = await mutateAndValidate({
		method: "PATCH",
		route: `grades/team/${id}`,
		body: { value, comment, gradeType, author, team },
		bodySchema: z.any()
	})

	if (response.status === "error") {
		throw new Error(response.error)
	}
}

export const addGradesToTeam = async(userId: string | null, rate : any): Promise<void> => {
	const response = await mutateAndValidate({
		method: "POST",
		route: `grades/add-grade-to-team/${userId}`,
		body: rate,
		bodySchema: z.any()
	})

	if (response.status === "error") {
		throw new Error(response.error)
	}
}

export const addGradeToTeam = async(userId: number, evaluations: any, token: any) => {
	try {
		const response = await fetch(import.meta.env.VITE_TAURI_API_URL + `grades/add-grade-to-team/${userId}`, {
			method: "POST",
			headers: {
				"Content-Type": "application/json",
				Authorization: token || "null"
			},
			body: JSON.stringify(evaluations.value)
		})

		if (!response.ok) {
			throw new Error(`HTTP error! status: ${response.status}`)
		}
		// eslint-disable-next-line @typescript-eslint/no-unsafe-return
		return await response.json()
	} catch (error) {
		console.error(error)
	}
}