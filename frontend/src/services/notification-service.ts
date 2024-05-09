
import { mutateAndValidate, queryAndValidate } from "@/utils/api"
import { NotificationSchema } from "@/types/notification"
import type { Notification } from "@/types/notification"
import { z } from "zod"

export const getAllNotifications = async(): Promise<Notification[]> => {
	const response = await queryAndValidate({
		route: "notifications",
		responseSchema: z.array(NotificationSchema)
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

export const addNotification = async(notification: Notification): Promise<void> => {
	const formData = new FormData()
	if (!notification) {
		formData.append("notification", notification)
	}

	const response = await mutateAndValidate({
		method: "POST",
		route: "notifications",
		body: formData,
		bodySchema: z.instanceof(FormData),
		jsonContent: false
	})

	if (response.status === "error") {
		throw new Error(response.error)
	}
}