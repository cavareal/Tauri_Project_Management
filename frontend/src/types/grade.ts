import { z } from "zod"
import { GradeTypeSchema } from "./grade-type"
import { UserSchema } from "./user"
import { StudentSchema } from "./student"
import { TeamSchema } from "./team"

export const GradeSchema = z.object({
	id: z.number(),
	value: z.number(),
	comment: z.string().nullable(),
	gradeType: GradeTypeSchema,
	author: UserSchema.nullable(),
	student: StudentSchema.nullable(),
	team: TeamSchema.nullable()
})

export const GradeDoubleArraySchema = z.array(z.array(z.number()))

export type Grade = z.infer<typeof GradeSchema>