import { z } from "zod"

export const AuthResponseSchema = z.object({
	login: z.string(),
	accessToken: z.string()
})

export type AuthResponse = z.infer<typeof AuthResponseSchema>