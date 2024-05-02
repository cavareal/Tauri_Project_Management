import { z } from "zod"
import { StudentSchema } from "./student"
import { UserSchema } from "./user"

export enum FlagType {
	REPORTING = "REPORTING",
	VALIDATION = "VALIDATION"
}

export const FlagTypeSchema = z.enum([
	FlagType.REPORTING,
	FlagType.VALIDATION
])

export const FlagSchema = z.object({
	id: z.number(),
	description: z.string(),
	type: FlagTypeSchema,
	firstStudent: StudentSchema.nullable(),
	secondStudent: StudentSchema.nullable(),
	author: UserSchema
})

export const FlagWithoutIdSchema = FlagSchema.omit({ id: true })

export type Flag = z.infer<typeof FlagSchema>

export type FlagWithoutId = z.infer<typeof FlagWithoutIdSchema>