import type { CreateFeedback } from "@/types/feedback"
import { mutateAndValidate, queryAndValidate } from "@/utils/api"
import { CreateFeedbackSchema, FeedbackSchema } from "@/types/feedback"
import { Cookies } from "@/utils/cookie"
import type { Feedback } from "@/types/feedback"

export const addComment = async(feedback: CreateFeedback): Promise<void> => {
	const response = await mutateAndValidate({
		method: "POST",
		route: "comments",
		body: feedback,
		bodySchema: CreateFeedbackSchema
	})

	if (response.status === "error") {
		throw new Error(response.error)
	}
}

export const createComment = async(teamId: string, feedbackContent: string, sprintId: string, feedback: boolean): Promise<void> => {
	const authorId = Cookies.getUserId()
	const comment: CreateFeedback = {
		teamId: Number(teamId),
		content: feedbackContent,
		feedback,
		sprintId: Number(sprintId),
		authorId
	}
	return await addComment(comment)
}

export const getCommentsBySprintAndTeam = async(teamId: string, sprintId: string): Promise<Feedback[]> => {
	const response = await queryAndValidate({
		route: `teams/${teamId}/sprints/${sprintId}/feedbacks`,
		responseSchema: FeedbackSchema.array()
	})

	if (response.status === "error") {
		throw new Error(response.error)
	}

	return response.data
}