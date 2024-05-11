import { z } from "zod"

export const AuthRequestSchema = z.object({
	login: z.string(),
	password: z.string()
})

export type AuthRequest = z.infer<typeof AuthRequestSchema>;