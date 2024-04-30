import type { Student } from "@/types/student"
import { StudentSchema } from "@/types/student"
import { mutateAndValidate, queryAndValidate } from "@/utils/api/api.util"
import { z } from "zod"

export const getAllStudents = async(): Promise<Student[]> => {
	const response = await queryAndValidate({
		route: "students",
		responseSchema: StudentSchema.array()
	})

	if (response.status === "error") {
		throw new Error(response.error)
	}

	return response.data
}

export const getQuantityOfStudents = async(): Promise<number> => {
	const response = await queryAndValidate({
		route: "students/quantity-all",
		responseSchema: z.number()
	})

	if (response.status === "error") {
		throw new Error(response.error)
	}

	return response.data
}

export const getStudentsByTeamId = async(teamId: number): Promise<Student[]> => {
	const response = await queryAndValidate({
		route: `students/team/${teamId}`,
		responseSchema: StudentSchema.array()
	})

	if (response.status === "error") {
		throw new Error(response.error)
	}

	return response.data
}

export const importStudentFile = async(file: File): Promise<void> => {
	const formData = new FormData()
	formData.append("file-upload", file)

	const response = await mutateAndValidate({
		method: "POST",
		route: "students/uploadCSV",
		body: formData,
		bodySchema: z.instanceof(FormData),
		jsonContent: false
	})

	if (response.status === "error") {
		throw new Error(response.error)
	}
}

export const deleteAllStudents = async(): Promise<void> => {
	const response = await mutateAndValidate({
		method: "DELETE",
		route: "students"
	})

	if (response.status === "error") {
		throw new Error(response.error)
	}
}