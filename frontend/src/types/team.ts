import { z } from "zod"
import { ProjectSchema } from "./project"
import { UserSchema } from "./user"

export const TeamSchema = z.object({
	id: z.number(),
	name: z.string(),
	project: ProjectSchema.nullable(),
	leader: UserSchema.nullable()
})

export type Team = z.infer<typeof TeamSchema>