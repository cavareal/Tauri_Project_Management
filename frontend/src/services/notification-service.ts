
import { apiQuery } from "@/utils/api"
import { NotificationSchema } from "@/types/notification"
import type { Notification } from "@/types/notification"

export const getAllNotifications = async(): Promise<Notification[]> => {
	const response = await apiQuery({
		route: "notifications/",
		responseSchema: NotificationSchema.array(),
		method: "GET"
	})

	if (response.status === "error") {
		throw new Error(response.error)
	}

	return response.data
}