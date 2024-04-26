import { z } from "zod"

export const LoginSchema = z.object({
	token: z.string(),
	role: z.string()
})

export type Login = z.infer<typeof LoginSchema>