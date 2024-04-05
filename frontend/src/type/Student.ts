import { z } from "zod"
import { GenderSchema } from "@/type/enumeration/Gender"
import { UserSchema } from "@/type/User"
import { TeamSchema } from "@/type/Team"

export const StudentSchema = z.intersection(UserSchema, z.object({
	//TO-DO: Delete optional and nullable
	gender: GenderSchema.optional().nullable(),
	bachelor: z.string().optional().nullable(),
	teamRole: z.string().optional().nullable(),
	teamId: TeamSchema.optional()
}))

export type Student = z.infer<typeof StudentSchema>;