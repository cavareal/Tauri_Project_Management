import type { Sprint } from "@/types/sprint"
import type { Project } from "@/types/project"

export const fakeSprint = (project: Project): Sprint => {
	return {
		id: 1,
		startDate: new Date(),
		endDate: new Date(),
		endType: "NORMAL_SPRINT",
		sprintOrder: 1,
		project
	}
}