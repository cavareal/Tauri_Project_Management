import type { Student } from "@/type/Student"
import { StudentSchema } from "@/type/Student"
import { apiQuery } from "@/utils/api"

export async function getStudentsByTeamId(teamId: number): Promise<Student[]> {
	const response = await apiQuery({
		route: `students/team/${teamId}`,
		responseSchema: StudentSchema.array(),
		method: "GET"
	})

	if (response.status === "error") {
		throw new Error(response.error)
	}
	console.log(response.data)
	return response.data
}