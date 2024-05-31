
import { mutateAndValidate, queryAndValidate } from "@/utils/api"
import { CreateNotificationSchema, NotificationSchema } from "@/types/notification"
import type { Notification, CreateNotification } from "@/types/notification"
import { z } from "zod"
import { getUsersByRole } from "@/services/user"
import { Cookies } from "@/utils/cookie"

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

export const getAllNotificationsFromUser = async(userId: number): Promise<Notification[]> => {
	const response = await queryAndValidate({
		route: `users/${userId}/notifications`,
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

export const addNotification = async(userToId: number, userFromId: number): Promise<void> => {
	const notification: CreateNotification = {
		message: "La composition des équipes a été prépubliée.",
		checked: false,
		type: "CREATE_TEAMS",
		userToId: userToId,
		userFromId: userFromId
	}

	const response = await mutateAndValidate({
		method: "POST",
		route: "notifications",
		body: notification,
		bodySchema: CreateNotificationSchema
	})

	if (response.status === "error") {
		throw new Error(response.error)
	}
}

export const sendManyNotifications = async(message: string): Promise<void> => {
	const teamMembers = await getUsersByRole("TEAM_MEMBER")
	const supervisors = await getUsersByRole("SUPERVISING_STAFF")

	const userFromId = Cookies.getUserId()

	await Promise.all([...teamMembers, ...supervisors].map(async(userTo) => {
		console.log(userTo)
		const notification: CreateNotification = {
			message: message,
			checked: false,
			type: "CREATE_TEAMS",
			userToId: userTo.id,
			userFromId: userFromId
		}

		const response = await mutateAndValidate({
			method: "POST",
			route: "notifications",
			body: notification,
			bodySchema: CreateNotificationSchema
		})

		if (response.status === "error") {
			throw new Error(response.error)
		}
	}))
}