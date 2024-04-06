import { z } from "zod"

export const UserSchema = z.object({
	//TO-DO: Delete optional and nullable
	id: z.number(),
	name: z.string(),
	email: z.string().optional().nullable(),
	password: z.string().optional().nullable(),
	privateKey: z.string().optional().nullable()
})

export type User = z.infer<typeof UserSchema>;