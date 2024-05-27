import {
	GradeDoubleArraySchema,
	GradeSchema,
	type Grade,
	CreateGradeSchema,
	type CreateGrade,
	GradeMapSchema,
	UpdateGradeSchema, type UpdateGrade
} from "@/types/grade"
import { mutateAndValidate, queryAndValidate } from "@/utils/api"
import { z } from "zod"
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

export const updateGrade = async(id: number, body: UpdateGrade): Promise<void> => {
	const response = await mutateAndValidate({
		method: "PATCH",
		route: `grades/${id}`,
		body,
		bodySchema: UpdateGradeSchema
	})

	if (response.status === "error") {
		throw new Error(response.error)
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


export const getGradesConfirmation = async(sprintId: number, teamId: number, ssTeam: number): Promise<boolean> => {
	const response = await queryAndValidate({
		route: `grades/confirmation/${sprintId}/team/${teamId}`,
		params: { ssTeam: ssTeam.toString() },
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

export const getGradeByGradeTypeAndAuthorAndSprint = async(studentId: number, gradeTypeId: number, authorId: number, sprintId: number): Promise<Grade | null> => {
	const response = await queryAndValidate({
		route: `students/${studentId}/gradeType/${gradeTypeId}/author/${authorId}`,
		params: { sprintId: sprintId.toString() },
		responseSchema: GradeSchema.nullable()
	})

	console.log(response)

	if (response.status === "error") {
		return null
	}

	return response.data
}