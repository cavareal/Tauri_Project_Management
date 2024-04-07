import { z } from "zod"
import { ProjectSchema } from "./project"
import { UserSchema } from "./user"
import { TeamSchema } from "./team"

export const GenderSchema = z.enum(["MAN", "WOMAN"])

export const StudentSchema = z.intersection(UserSchema, z.object({
	gender: GenderSchema,
	bachelor: z.boolean().optional().nullable(),
	teamRole: z.string().nullable().optional().nullable(),
	team: TeamSchema.nullable().optional().nullable(),
	project: ProjectSchema.optional().nullable()
}))

export type Gender = z.infer<typeof GenderSchema>

export type Student = z.infer<typeof StudentSchema>