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

export const createComment = async(teamId: number, feedbackContent: string, sprintId: number, feedback: boolean): Promise<void> => {
	const authorId = Cookies.getUserId()
	const comment: CreateFeedback = {
		teamId,
		content: feedbackContent,
		feedback,
		sprintId,
		authorId
	}
	return await addComment(comment)
}

export const getCommentsBySprintAndTeam = async(teamId: number, sprintId: number): Promise<Feedback[]> => {
	const response = await queryAndValidate({
		route: `teams/${teamId}/sprints/${sprintId}/feedbacks`,
		responseSchema: FeedbackSchema.array()
	})

	if (response.status === "error") {
		throw new Error(response.error)
	}

	return response.data
}