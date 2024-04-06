import { z } from "zod"

export const CriteriaSchema = z.object({
	nbStudents: z.number(),
	nbWomans: z.number(),
	nbBachelors: z.number(),
	validCriteriaWoman: z.boolean(),
	validCriteriaBachelor: z.boolean()
})

export type Criteria = z.infer<typeof CriteriaSchema>;