import { z } from "zod"

export const UserSchema = z.object({
	id: z.number(),
	name: z.string(),
	email: z.string(),
	password: z.string(),
	privateKey: z.string()
})
export type User = z.infer<typeof UserSchema>

export const CreateUserSchema = UserSchema.omit({
	id: true
})
export type CreateUser = z.infer<typeof CreateUserSchema>

export const UpdateUserSchema = CreateUserSchema.partial()
export type UpdateUser = z.infer<typeof UpdateUserSchema>