import { z } from "zod"

export const ProjectSchema = z.object({
	//TO-DO: Delete optional and nullable
	id: z.number().optional(),
	nbTeam: z.number().optional(),
	name: z.string().optional(),
	ratioGender: z.number().optional(),
	nbSprints: z.number().optional(),
	phase: z.string().optional()
})

export type Project = z.infer<typeof ProjectSchema>;