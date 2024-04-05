import { z } from "zod"

export const GenderSchema = z.enum(["WOMAN", "MAN", "OTHER"])

export type Gender = z.infer<typeof GenderSchema>;