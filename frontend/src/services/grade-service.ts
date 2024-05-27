import { GradeDoubleArraySchema, GradeSchema, type Grade, CreateGradeSchema, GradeMapSchema } from "@/types/grade"
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

export const getAverageByGradeType = async(id: number, sprintId: number, gradeTypeName: string): Promise<number> => {
	const response = await queryAndValidate({
		route: `grades/average/${id}`,
		params: { sprintId: sprintId.toString(), gradeTypeName: gradeTypeName },
		responseSchema: z.number()
	})

	if (response.status === "error") {
		throw new Error(response.error)
	}

	return response.data
}

export const getTeamAverage = async(teamId: number, sprintId: string): Promise<z.infer<typeof GradeMapSchema>> => {
	const response = await queryAndValidate({
		route: `grades/average-team/${teamId}`,
		params: { sprintId: sprintId },
		responseSchema: GradeMapSchema
	})

	if (response.status === "error") {
		throw new Error(response.error)
	}
	return response.data
}

export const getStudentsAverageByTeam = async(teamId: number, sprintId: string): Promise<z.infer<typeof GradeMapSchema>> => {
	const response = await queryAndValidate({
		route: `grades/average-students/${teamId}`,
		params: { sprintId: sprintId },
		responseSchema: GradeMapSchema
	})

	if (response.status === "error") {
		throw new Error(response.error)
	}

	return response.data
}

export const getTeamTotalGrade = async(teamId: number, sprintId: number): Promise<number> => {
	const response = await queryAndValidate({
		route: `teams/${teamId}/sprint/${sprintId}/total`,
		responseSchema: z.number()
	})

	if (response.status === "error") {
		throw new Error(response.error)
	}

	return response.data
}

export const getIndividualTotalGrade = async(studentId: number, sprintId: number): Promise<number> => {
	const response = await queryAndValidate({
		route: `students/${studentId}/sprint/${sprintId}/total`,
		responseSchema: z.number()
	})

	if (response.status === "error") {
		throw new Error(response.error)
	}

	return response.data
}

export const getSprintGrade = async(studentId: number, sprintId: number): Promise<number> => {
	const response = await queryAndValidate({
		route: `students/${studentId}/sprint/${sprintId}/grade`,
		responseSchema: z.number()
	})

	if (response.status === "error") {
		throw new Error(response.error)
	}

	return response.data
}

export const getIndividualTotalGrades = async(teamId: number, sprintId: number): Promise<number[]> => {
	const response = await queryAndValidate({
		route: `teams/${teamId}/sprint/${sprintId}/individual/totals`,
		responseSchema: z.number().array()
	})

	if (response.status === "error") {
		throw new Error(response.error)
	}

	return response.data
}

export const getSprintGrades = async(teamId: number, sprintId: number): Promise<number[]> => {
	const response = await queryAndValidate({
		route: `teams/${teamId}/sprint/${sprintId}/grades`,
		responseSchema: z.number().array()
	})

	if (response.status === "error") {
		throw new Error(response.error)
	}

	return response.data
}

export const getAverageSprintGrades = async(sprintId: number): Promise<number[]> => {
	const response = await queryAndValidate({
		route: `teams/sprint/${sprintId}/average`,
		responseSchema: z.number().array()
	})

	if (response.status === "error") {
		throw new Error(response.error)
	}

	return response.data
}


export const getGradesConfirmation = async(sprintId: number, teamId: number): Promise<number> => {
	const response = await queryAndValidate({
		route: `grades/confirmation/${sprintId}/team/${teamId}`,
		responseSchema: z.any()
	})


	if (response.status === "error") {
		throw new Error(response.error)
	}

	return response.data
}

export const setGradesConfirmation = async(teamId: number, sprintId: number) => {
	const response = await mutateAndValidate({
		method: "POST",
		route: `grades/confirmation/${sprintId}/team/${teamId}`,
		bodySchema: z.any()
	})

	if (response.status === "error") {
		throw new Error(response.error)
	}
}

export const downloadGradesFile = async() => {
	const response = await queryAndValidate({
		route: "grades/download",
		responseSchema: z.string()
	})

	if (response.status === "error") {
		throw new Error(response.error)
	}

	const url = window.URL.createObjectURL(new Blob([response.data]))
	const link = document.createElement("a")
	link.href = url
	link.setAttribute("download", "grades.csv")
	document.body.appendChild(link)
	link.click()
	document.body.removeChild(link)
}