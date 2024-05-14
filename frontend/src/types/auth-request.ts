import { z } from "zod"

export const AuthRequestSchema = z.object({
	login: z.string({ required_error: "L'adresse email est requise." })
		.email({ message: "L'adresse email n'est pas valide."}),
	password: z.string({ required_error: "Le mot de passe est requis." })
})

export type AuthRequest = z.infer<typeof AuthRequestSchema>;