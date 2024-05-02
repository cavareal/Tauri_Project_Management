import type { Student } from "@/types/student"
import { StudentSchema } from "@/types/student"
import { apiQuery } from "@/utils/api"
import { uploadFile } from "@/utils/api/api.util"
import { z } from "zod"
import type { Team } from "@/types/team"

export const getAllStudents = async(projectId: string | null): Promise<Student[]> => {
	const response = await apiQuery({
		responseSchema: z.array(StudentSchema),
		method: "GET",
		route: `students?projectId=${projectId}`
	})

	if (response.status === "error") {
		throw new Error(response.error)
	}

	return response.data
}

export const getQuantityOfStudents = async(): Promise<number> => {
	const response = await apiQuery({
		responseSchema: z.number(),
		method: "GET",
		route: "students/quantity-all"
	})

	if (response.status === "error") {
		throw new Error(response.error)
	}

	return response.data
}

export const getStudentsByTeamId = async(teamId: number): Promise<Student[]> => {
	const response = await apiQuery({
		route: `teams/${teamId}/students`,
		responseSchema: StudentSchema.array(),
		method: "GET"
	})

	if (response.status === "error") {
		throw new Error(response.error)
	}

	return response.data
}

export const importStudentFile = async(file: File, projectId: string | null): Promise<void> => {
	const response = await uploadFile({
		file,
		route: `students/upload?projectId=${projectId}`
	})

	if (response.status === "error") {
		console.error(response.error)
		throw new Error(response.error)
	}

	return
}

export const deleteAllStudents = async(projectId: string | null): Promise<void> => {
	const response = await apiQuery({
		method: "DELETE",
		route: `students?projectId=${projectId}`,
		responseSchema: z.string(),
		textResponse: true
	})

	if (response.status === "error") {
		throw new Error(response.error)
	}
}

export const updateStudent = async(id: string | null, gender: string | null, bachelor: boolean | null, teamRole: string | null, team: Team | null): Promise<void> => {
	const response = await apiQuery({
		method: "PUT",
		route: `students/${id}`,
		body: { gender, bachelor, teamRole, team },
		responseSchema: z.string(),
		textResponse: true
	})

	if (response.status === "error") {
		throw new Error(response.error)
	}
}

export const changeStudentTeam = async(studentId: number, teamId: number): Promise<void> => {
	const response = await apiQuery({
		method: "PATCH",
		route: `students/${studentId}`,
		body: { teamId },
		responseSchema: z.string(),
		textResponse: true
	})

	if (response.status === "error") {
		throw new Error(response.error)
	}
}