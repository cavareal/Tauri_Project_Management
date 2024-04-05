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