import { z } from "zod"

export const UserSchema = z.object({
	id: z.number(),
	name: z.string(),
	email: z.string().optional().nullable(),
	password: z.string().optional().nullable(),
	privateKey: z.string().optional().nullable()
})

export type User = z.infer<typeof UserSchema>