import { SprintSchema } from "@/types/sprint"
import { StudentSchema } from "@/types/student"
import { UserSchema } from "@/types/user"
import { z } from "zod"

export const BonusSchema = z.object({
	id: z.number(),
	value: z.number(),
	comment: z.string().nullable(),
	limited: z.boolean(),
	sprint: SprintSchema,
	student: StudentSchema,
	author: UserSchema
})
export type Bonus = z.infer<typeof BonusSchema>

export const CreateBonusSchema = BonusSchema.omit({
	id: true,
	sprint: true,
	student: true,
	author: true
}).extend({
	sprintId: z.number(),
	studentId: z.number(),
	authorId: z.number()
})
export type CreateBonus = z.infer<typeof CreateBonusSchema>

export const UpdateBonusSchema = CreateBonusSchema.partial()
export type UpdateBonus = z.infer<typeof UpdateBonusSchema>