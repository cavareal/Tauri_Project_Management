
import { mutateAndValidate, queryAndValidate } from "@/utils/api"
import { NotificationSchema } from "@/types/notification"
import type { Notification } from "@/types/notification"

export const getAllNotifications = async(): Promise<Notification[]> => {
	const response = await queryAndValidate({
		route: "notifications",
		responseSchema: NotificationSchema.array()
	})

	if (response.status === "error") {
		throw new Error(response.error)
	}

	return response.data
}

export const changeStateChecked = async(id: number): Promise<void> => {
	const response = await mutateAndValidate({
		route: `notifications/${id}/changeStateChecked`,
		method: "PATCH"
	})

	if (response.status === "error") {
		throw new Error(response.error)
	}
}