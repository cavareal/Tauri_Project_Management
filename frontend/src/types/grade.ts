import { GradeTypeSchema } from "@/types/grade-type"
import { SprintSchema } from "@/types/sprint"
import { StudentSchema } from "@/types/student"
import { TeamSchema } from "@/types/team"
import { UserSchema } from "@/types/user"
import { z } from "zod"

export const GradeSchema = z.object({
	id: z.number(),
	value: z.number(),
	comment: z.string().nullable(),
	gradeType: GradeTypeSchema,
	author: UserSchema.nullable(),
	student: StudentSchema.nullable(),
	team: TeamSchema.nullable(),
	sprint: SprintSchema.nullable()
})
export type Grade = z.infer<typeof GradeSchema>

export const CreateGradeSchema = GradeSchema.omit({
	id: true,
	gradeType: true,
	author: true,
	student: true,
	team: true,
	sprint: true
}).extend({
	gradeTypeId: z.number(),
	authorId: z.number(),
	studentId: z.number().nullable(),
	teamId: z.number().nullable(),
	sprintId: z.number()
})
export type CreateGrade = z.infer<typeof CreateGradeSchema>

export const UpdateGradeSchema = CreateGradeSchema.partial()
export type UpdateGrade = z.infer<typeof UpdateGradeSchema>

export const GradeDoubleArraySchema = z.array(z.array(z.number()))