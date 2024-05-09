import { z } from "zod"

export const FeedbackSchema = z.object({
	id: z.number(),
	content: z.string(),
	feedback: z.boolean(),
	teamId: z.number(),
	sprintId: z.number(),
	authorId: z.number()
})

export const CreateFeedbackSchema = FeedbackSchema.omit({
	id: true,
	authorId: true,
	teamId: true
}).extend({
	authorId: z.coerce.number(),
	teamId: z.string()
})


export type Feedback = z.infer<typeof FeedbackSchema>
export type CreateFeedback = z.infer<typeof CreateFeedbackSchema>