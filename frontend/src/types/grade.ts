import { z } from "zod"
import { GradeTypeSchema } from "./grade-type"
import { UserSchema } from "./user"
import { StudentSchema } from "./student"

export const GradeSchema = z.object({
	id: z.number(),
	value: z.number(),
	comment: z.string().nullable(),
	gradeType: GradeTypeSchema,
	author: UserSchema.nullable(),
	student: StudentSchema,
	team: z.number().nullable()
})

export const NamedGradeListSchema = z.object({
	name: z.string(),
	grades: GradeSchema
})

export type Grade = z.infer<typeof GradeSchema>