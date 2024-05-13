import type { CreateFeedback } from "@/types/feedback"
import { mutateAndValidate, queryAndValidate } from "@/utils/api"
import { CreateFeedbackSchema, FeedbackSchema } from "@/types/feedback"
import { Cookies } from "@/utils/cookie"
import type { Feedback } from "@/types/feedback"

export const addFeedback = async(feedback: CreateFeedback): Promise<void> => {
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

export const createFeedback = async(teamId: number, feedbackContent: string, sprintId: number): Promise<void> => {
	const authorId = Cookies.getUserId()
	const feedback: CreateFeedback = {
		teamId,
		content: feedbackContent,
		feedback: true,
		sprintId,
		authorId
	}
	return await addFeedback(feedback)
}

export const getFeedbacksBySprintAndTeam = async(teamId: number, sprintId: number): Promise<Feedback[]> => {
	const response = await queryAndValidate({
		route: `teams/${teamId}/sprints/${sprintId}/feedbacks`,
		responseSchema: FeedbackSchema.array()
	})

	if (response.status === "error") {
		throw new Error(response.error)
	}

	return response.data
}