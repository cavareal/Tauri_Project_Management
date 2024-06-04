import { z } from "zod"

export const GradeTypeSchema = z.object({
	id: z.number(),
	name: z.string(),
	factor: z.coerce.number().nullable(),
	forGroup: z.boolean(),
	imported: z.boolean(),
	scaleTXTBlob: z.string().nullable() //Modified
})
export type GradeType = z.infer<typeof GradeTypeSchema>

export const CreateGradeTypeSchema = GradeTypeSchema.omit({
	id: true
})
export type CreateGradeType = z.infer<typeof CreateGradeTypeSchema>

export const UpdateGradeTypeSchema = CreateGradeTypeSchema.partial()
export type UpdateGradeType = z.infer<typeof UpdateGradeTypeSchema>

export const GradeTypeNameSchema = z.enum([
	"Solution Technique",
	"Conformité au sprint",
	"Gestion de projet",
	"Contenu de la présentation",
	// "Support de présentation",
	"Performance globale de l'équipe",
	"Performance individuelle"
])
export type GradeTypeName = z.infer<typeof GradeTypeNameSchema>

export const getGradeTypeDescription = (gradeTypeName: GradeTypeName): string => {
	switch (gradeTypeName) {
	case "Solution Technique":
		return "Vous devez évaluer chaque équipe sur la solution technique qui a été mise en œuvre."
	case "Conformité au sprint":
		return "Vous devez évaluer la conformité du sprint des équipes."
	case "Gestion de projet":
		return "Vous devez évaluer chaque équipe sur sa gestion du projet."
	case "Contenu de la présentation":
		return "Qualité du contenu de la présentation"
	// case "Support de présentation":
	// 	return "Vous devez évaluer chaque équipe sur son support de présentation."
	case "Performance globale de l'équipe":
		return "Vous devez évaluer chaque équipe sur sa présentation globale."
	case "Performance individuelle":
		return "Vous devez évaluer chaque étudiant sur sa performance individuelle lors de sa présentation."
	}
}