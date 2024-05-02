import type { Student } from "@/types/student"
import { StudentSchema } from "@/types/student"
import { mutateAndValidate, queryAndValidate } from "@/utils/api"
import { z } from "zod"
import type { Team } from "@/types/team"

export const getAllStudents = async(projectId: string | null): Promise<Student[]> => {
	const response = await queryAndValidate({
		responseSchema: StudentSchema.array(),
		route: "students",
		params: { projectId: projectId ?? "" }
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
		route: `teams/${teamId}/students`,
		responseSchema: StudentSchema.array()
	})

	if (response.status === "error") {
		throw new Error(response.error)
	}

	return response.data
}

export const importStudentFile = async(file: File, projectId: string | null): Promise<void> => {
	const formData = new FormData()
	formData.append("file-upload", file)

	const response = await mutateAndValidate({
		method: "POST",
		route: "students/upload",
		params: { projectId: projectId ?? "" },
		body: formData,
		bodySchema: z.instanceof(FormData),
		jsonContent: false
	})

	if (response.status === "error") {
		throw new Error(response.error)
	}
}

export const deleteAllStudents = async(projectId: string | null): Promise<void> => {
	const response = await mutateAndValidate({
		method: "DELETE",
		route: "students",
		params: { projectId: projectId ?? "" }
	})

	if (response.status === "error") {
		throw new Error(response.error)
	}
}

export const updateStudent = async(id: string | null, gender: string | null, bachelor: boolean | null, teamRole: string | null, teamId: number | null): Promise<void> => {
	const response = await mutateAndValidate({
		method: "PATCH",
		route: `students/${id}`,
		body: { gender, bachelor, teamRole, teamId },
		bodySchema: z.any()
	})

	if (response.status === "error") {
		throw new Error(response.error)
	}
}

export const changeStudentTeam = async(studentId: number, teamId: number): Promise<void> => {
	const response = await mutateAndValidate({
		method: "PATCH",
		route: `students/${studentId}`,
		body: { teamId },
		bodySchema: z.object({ teamId: z.number() })
	})

	if (response.status === "error") {
		throw new Error(response.error)
	}
}