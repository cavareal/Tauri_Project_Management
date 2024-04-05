import { z } from "zod"
import { ProjectSchema } from "@/type/Project"
import { UserSchema } from "@/type/User"


export const TeamSchema = z.object({
	//TO-DO: Delete optional and nullable
	id: z.number(),
	name: z.string(),
	project: ProjectSchema.optional().nullable(),
	leader: UserSchema.optional().nullable()
})

export type Team = z.infer<typeof TeamSchema>;