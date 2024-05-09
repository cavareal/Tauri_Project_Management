import type { CreateFeedback } from "@/types/feedback"
import { mutateAndValidate } from "@/utils/api"
import { CreateFeedbackSchema } from "@/types/feedback"
import { Cookies } from "@/utils/cookie"
import { getCurrentSprint } from "@/services/sprint-service"

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

export const createFeedback = async(teamId: string, feedbackContent: string): Promise<void> => {
	const authorId = Cookies.getUserId()
	const sprintId = await getCurrentSprint()
	const feedback: CreateFeedback = {
		teamId,
		content: feedbackContent,
		feedback: true,
		sprintId,
		authorId
	}
	return await addFeedback(feedback)
}