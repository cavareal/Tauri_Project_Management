import { z } from "zod"

export const UserSchema = z.object({
	id: z.number(),
	name: z.string(),
	email: z.string().email(),
	password: z.string(),
	privateKey: z.string()
})

export type User = z.infer<typeof UserSchema>