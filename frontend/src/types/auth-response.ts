import { z } from "zod"

export const AuthResponseSchema = z.object({
	id: z.number(),
	accessToken: z.string()
})

export type AuthResponse = z.infer<typeof AuthResponseSchema>