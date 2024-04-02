import { z } from "zod"
import { ProjectSchema } from "@/type/Project"
import { UserSchema } from "@/type/User"


export const TeamSchema = z.object({
	id: z.number(),
	name: z.string(),
	projectId: ProjectSchema.optional(),
	leaderId: UserSchema.optional()
})

export type Team = z.infer<typeof TeamSchema>;