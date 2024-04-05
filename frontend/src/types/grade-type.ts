import { z } from "zod"

export const GradeTypeSchema = z.object({
	id: z.number(),
	name: z.string(),
	factor: z.number(),
	forGroup: z.boolean(),
	imported: z.boolean()
})

export type GradeType = z.infer<typeof GradeTypeSchema>