import { z } from "zod"
import { UserSchema } from "@/types/user"

export const NotificationSchema = z.object({
	id: z.number(),
	message: z.string().optional().nullable(),
	checked: z.boolean().optional().nullable(),
	type: z.string().optional().nullable(),
	userTo: UserSchema,
	userFrom: UserSchema
})

export type Notification = z.infer<typeof NotificationSchema>

export const CreateNotificationSchema = z.object({
	message: z.string().optional().nullable(),
	checked: z.boolean().optional().nullable(),
	type: z.string().optional().nullable(),
	userToId: z.number(),
	userFromId: z.number()
})

export type CreateNotification = z.infer<typeof CreateNotificationSchema>