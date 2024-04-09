import type { Student } from "@/types/student"
import { StudentSchema } from "@/types/student"
import { apiQuery } from "@/utils/api"
import { z } from "zod"

export const getAllStudents = async(): Promise<Student[]> => {
	const response = await apiQuery({
		responseSchema: z.array(StudentSchema),
		method: "GET",
		route: "students"
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
		route: `students/team/${teamId}`,
		responseSchema: StudentSchema.array(),
		method: "GET"
	})

	if (response.status === "error") {
		throw new Error(response.error)
	}

	return response.data
}

export const importStudentFile = async(file: File): Promise<void> => {
	const formData = new FormData()
	formData.append("file-upload", file)

	const response = await apiQuery({
		method: "POST",
		route: "students/uploadCSV",
		body: formData,
		responseSchema: z.string(),
		textResponse: true
	})

	if (response.status === "error") {
		throw new Error(response.error)
	}
}

export const deleteAllStudents = async(): Promise<void> => {
	const response = await apiQuery({
		method: "DELETE",
		route: "students",
		responseSchema: z.string(),
		textResponse: true
	})

	if (response.status === "error") {
		throw new Error(response.error)
	}
}