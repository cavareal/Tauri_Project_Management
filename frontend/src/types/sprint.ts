import { ProjectSchema } from "@/types/project"
import { z } from "zod"

export const SprintEndTypeSchema = z.enum([
	"NORMAL_SPRINT",
	"UNGRADED_SPRINT",
	"FINAL_SPRINT"
])
export type SprintEndType = z.infer<typeof SprintEndTypeSchema>

export const SprintSchema = z.object({
	id: z.number(),
	startDate: z.coerce.date(),
	endDate: z.coerce.date(),
	endType: SprintEndTypeSchema,
	sprintOrder: z.coerce.number(),
	project: ProjectSchema
})
export type Sprint = z.infer<typeof SprintSchema>

export const CreateSprintSchema = SprintSchema.omit({
	id: true,
	project: true
}).extend({
	projectId: z.number()
})
export type CreateSprint = z.infer<typeof CreateSprintSchema>

export const UpdateSprintSchema = CreateSprintSchema.partial()
export type UpdateSprint = z.infer<typeof UpdateSprintSchema>