import { z } from "zod"

export const GradeTypeSchema = z.object({
	id: z.number(),
	name: z.string(),
	factor: z.number().nullable(),
	forGroup: z.boolean(),
	imported: z.boolean(),
	scaleUrl: z.string().nullable()
})
export type GradeType = z.infer<typeof GradeTypeSchema>

export const CreateGradeTypeSchema = GradeTypeSchema.omit({
	id: true
})
export type CreateGradeType = z.infer<typeof CreateGradeTypeSchema>

export const UpdateGradeTypeSchema = CreateGradeTypeSchema.partial()
export type UpdateGradeType = z.infer<typeof UpdateGradeTypeSchema>