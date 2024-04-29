import { z } from "zod"
import { UserSchema } from "@/types/user"

export const NotificationSchema = z.object({
	id: z.number(),
	message: z.string(),
	bachelor: z.boolean().optional().nullable(),
	type: z.string().optional().nullable(),
	userTo: UserSchema.optional().nullable(),
	userFrom: UserSchema.optional().nullable()
})

export type Notification = z.infer<typeof NotificationSchema>