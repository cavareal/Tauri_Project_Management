import { z } from "zod"
import { ProjectSchema } from "./project"
import { UserSchema } from "./user"

export const GenderSchema = z.enum(["MAN", "WOMAN"])

export const StudentSchema = z.intersection(UserSchema, z.object({
	gender: GenderSchema,
	bachelor: z.boolean(),
	teamRole: z.string().nullable(),
	team: z.number().nullable(),
	project: ProjectSchema
}))

export type Gender = z.infer<typeof GenderSchema>

export type Student = z.infer<typeof StudentSchema>