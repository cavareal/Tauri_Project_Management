import type { Student, UpdateStudent } from "@/types/student"
import { StudentSchema } from "@/types/student"
import { mutateAndValidate, queryAndValidate } from "@/utils/api"
import { z } from "zod"

export const getAllStudents = async(): Promise<Student[]> => {
	const response = await queryAndValidate({
		responseSchema: StudentSchema.array(),
		route: "students"
	})

	if (response.status === "error") {
		throw new Error(response.error)
	}

	return response.data
}

export const getStudentsByTeamId = async(teamId: number): Promise<Student[]> => {
	const response = await queryAndValidate({
		route: `teams/${teamId}/students`,
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
		route: "students/upload",
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

export const updateStudent = async(id: string | null, body: UpdateStudent): Promise<void> => {
	const response = await mutateAndValidate({
		method: "PATCH",
		route: `students/${id}`,
		body,
		bodySchema: z.any()
	})

	if (response.status === "error") {
		throw new Error(response.error)
	}
}

export const downloadStudentFile = async(): Promise<void> => {
	const response = await queryAndValidate({
		route: "students/download",
		responseSchema: z.string()
	})

	if (response.status === "error") {
		throw new Error(response.error)
	}

	const url = window.URL.createObjectURL(new Blob([response.data]))
	const link = document.createElement("a")
	link.href = url
	link.setAttribute("download", "students.csv")
	document.body.appendChild(link)
	link.click()
	document.body.removeChild(link)
}