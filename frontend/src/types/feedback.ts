import { z } from "zod"
import { TeamSchema } from "@/types/team"
import { SprintSchema } from "@/types/sprint"
import { UserSchema } from "@/types/user"

export const FeedbackSchema = z.object({
	id: z.number(),
	content: z.string(),
	feedback: z.boolean(),
	team: TeamSchema,
	sprint: SprintSchema,
	author: UserSchema
})

export const CreateFeedbackSchema = FeedbackSchema.omit({
	id: true,
	author: true,
	team: true,
	sprint: true
}).extend({
	authorId: z.coerce.number(),
	teamId: z.coerce.number(),
	sprintId: z.coerce.number()
})


export type Feedback = z.infer<typeof FeedbackSchema>
export type CreateFeedback = z.infer<typeof CreateFeedbackSchema>