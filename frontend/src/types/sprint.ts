import { z } from "zod"

export const SprintSchema = z.object({
    startDate: z.string(),
    endDate: z.string(),
    type: z.string(), 
})

export type Sprint = z.infer<typeof SprintSchema>;