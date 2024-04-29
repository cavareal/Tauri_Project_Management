import { z } from "zod"

export const ProjectPhaseSchema = z.enum(["COMPOSING", "PREPUBLISHED", "PUBLISHED", "FINISHED"])

export const ProjectSchema = z.object({
	id: z.number(),
	nbTeams: z.number().nullable(),
	nbWomen: z.number().nullable(),
	nbSprint: z.number().nullable(),
	phase: ProjectPhaseSchema
})

export type ProjectPhase = z.infer<typeof ProjectPhaseSchema>

export type Project = z.infer<typeof ProjectSchema>