import { z } from "zod"
import { StudentSchema } from "./student"
import { UserSchema } from "./user"

export const FlagSchema = z.object({
	id: z.number(),
	description: z.string(),
	type: z.string(),
	firstStudent: StudentSchema.nullable(),
	secondStudent: StudentSchema.nullable(),
	author: UserSchema
})

export const FlagWithoutIdSchema = FlagSchema.omit({ id: true })

export enum FlagType {
    REPORTING = "Reporting",
    VALIDATION = "Validation"
}

export type Flag = z.infer<typeof FlagSchema>

export type FlagWithoutId = z.infer<typeof FlagWithoutIdSchema>