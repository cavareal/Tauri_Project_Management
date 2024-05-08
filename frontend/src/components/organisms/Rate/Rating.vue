<script setup lang="ts">
import { ContainerGradeType } from "@/components/molecules/rateContainer"
import { Button } from "@/components/ui/button"
import {
	Users,
	LucideCircleFadingPlus,
	User,
	SquareGanttChart,
	Play,
	Presentation,
	Package,
	Blocks
} from "lucide-vue-next"
import { DialogRating, DialogViewGrades, DialogBonus } from "@/components/organisms/rating"
import { useQuery } from "@tanstack/vue-query"
import { getGradeTypeByName } from "@/services/grade-type-service"
import type { GradeType } from "@/types/grade-type"
import { hasPermission } from "@/services/user-service"

const props = defineProps<{
	teamId : string,
	sprintId : string
}>()

const canGradeGlobalPerformance = hasPermission("GRADE_GLOBAL_PERFORMANCE")
const canGradeBonus = hasPermission("LIMITED_BONUS_MALUS")
const canGradePresentationContent = hasPermission("GRADE_PRESENTATION_CONTENT")
const canGradeMaterialSupport = hasPermission("GRADE_SUPPORT_MATERIAL")
const canGradeIndividualPerformance = hasPermission("GRADE_INDIVIDUAL_PERFORMANCE")
const canAddComment = hasPermission("ADD_GRADE_COMMENT")
const gradeOwnTeam = hasPermission("GRADE_OWN_TEAM")
// const canGradeTechnicalSolution = hasPermission("GRADE_TECHNICAL_SOLUTION")
// const canGradeSprintConformity = hasPermission("GRADE_SPRINT_CONFORMITY")
// const canGradeProjectManagement = hasPermission("GRADE_PROJECT_MANAGEMENT")
const canGradeTechnicalSolution = hasPermission("GRADE_SUPPORT_MATERIAL")
const canGradeSprintConformity = hasPermission("GRADE_SUPPORT_MATERIAL")
const canGradeProjectManagement = hasPermission("GRADE_SUPPORT_MATERIAL")

const { data: gradeType } = useQuery<GradeType, Error>({
	queryKey: ["grade-type"],
	queryFn: () => getGradeTypeByName("Performance globale de l'équipe")
})
</script>

<template>
	<ContainerGradeType v-if="canGradeGlobalPerformance" title="Note Globale de présentation" infotext="Vous devez évaluer chaque équipe sur sa présentation globale">
		<template #icon>
			<Users :size="40" :stroke-width="1"/>
		</template>
		<template #dialog>
			<DialogViewGrades title="Voir les notes" description=""></DialogViewGrades>
			<DialogRating title="Note de présentation" :description="`Veuillez noter la présentation de l équipe : ${props.teamId}`" :gradeType=gradeType :teamId="props.teamId" :sprintId="props.sprintId" gradeTypeString="Performance globale de l'équipe">
				<template #trigger>
					<Button variant="default">Noter une équipe</Button>
				</template>
			</DialogRating>
		</template>
	</ContainerGradeType>

	<ContainerGradeType v-if="canGradeTechnicalSolution" title="Solution Technique" infotext="Vous devez évaluer chaque équipe sur la solution technique qui a été mise en œuvre.">
		<template #icon>
			<Blocks :size="40" :stroke-width="1"/>
		</template>
		<template #dialog>
			<DialogViewGrades title="Voir les notes" description=""></DialogViewGrades>
			<DialogRating title="Note de la solution technique" description="Veuillez noter la solution technique mise en œuvre par l'équipe" gradeType="gradeTechnicalSolution">
				<template #trigger>
					<Button variant="default">Noter une équipe</Button>
				</template>
			</DialogRating>
		</template>
	</ContainerGradeType>

	<ContainerGradeType v-if="canGradeSprintConformity" title="Conformité du sprint" infotext="Vous devez évaluer la conformité du sprint des équipes.">
		<template #icon>
			<Play :size="40" :stroke-width="1"/>
		</template>
		<template #dialog>
			<DialogViewGrades title="Voir les notes" description=""></DialogViewGrades>
			<DialogRating title="Note de conformité du sprint" description="Veuillez noter la conformité du sprint" gradeType="gradeSprintConformity">
				<template #trigger>
					<Button variant="default">Noter une équipe</Button>
				</template>
			</DialogRating>
		</template>
	</ContainerGradeType>

	<ContainerGradeType v-if="canGradeProjectManagement" title="Gestion du projet" infotext="Vous devez évaluer chaque équipe sur sa gestion du projet.">
		<template #icon>
			<SquareGanttChart :size="40" :stroke-width="1"/>
		</template>
		<template #dialog>
			<DialogViewGrades title="Voir les notes" description=""></DialogViewGrades>
			<DialogRating title="Note de la gestion du projet" description="Veuillez noter la gestion du projet" gradeType="gradeProjectManagement">
				<template #trigger>
					<Button variant="default">Noter une équipe</Button>
				</template>
			</DialogRating>
		</template>
	</ContainerGradeType>

	<ContainerGradeType v-if="canGradePresentationContent" title="Contenu de la présentation" infotext="Vous devez évaluer chaque équipe sur le contenu de sa présentation.">
		<template #icon>
			<Package :size="40" :stroke-width="1"/>
		</template>
		<template #dialog>
			<DialogViewGrades title="Voir les notes" description=""></DialogViewGrades>
			<DialogRating title="Note du contenu de la présentation" description="Veuillez noter le contenu de la présentation" gradeType="gradeContentPresentation">
				<template #trigger>
					<Button variant="default">Noter une équipe</Button>
				</template>
			</DialogRating>
		</template>
	</ContainerGradeType>

	<ContainerGradeType v-if="canGradeMaterialSupport" title="Support de présentation" infotext="Vous devez évaluer chaque équipe sur son support de présentation.">
		<template #icon>
			<Presentation :size="40" :stroke-width="1"/>
		</template>
		<template #dialog>
			<DialogViewGrades title="Voir les notes" description=""></DialogViewGrades>
			<DialogRating title="Note du support de présentation" description="Veuillez noter le support de présentation" gradeType="gradeMaterialSupport">
				<template #trigger>
					<Button variant="default">Noter une équipe</Button>
				</template>
			</DialogRating>
		</template>
	</ContainerGradeType>

	<ContainerGradeType v-if="canGradeIndividualPerformance" title="Performance individuelle" infotext="Vous devez évaluer chaque étudiant sur sa performance individuelle lors de sa présentation.">
		<template #icon>
			<User :size="40" :stroke-width="1"/>
		</template>
		<template #dialog>
			<DialogViewGrades title="Voir les notes" description=""></DialogViewGrades>
			<DialogRating title="Note de performance" description="Veuillez noter la performance individuelle de chaque étudiants">
				<template #trigger>
					<Button variant="default">Noter une équipe</Button>
				</template>
			</DialogRating>
		</template>
	</ContainerGradeType>

	<ContainerGradeType v-if="canGradeBonus" title="Bonus et malus de mon équipe" infotext="Vous pouvez attribuer des bonus et des malus à votre équipe">
		<template #icon>
			<LucideCircleFadingPlus :size="40" :stroke-width="1"/>
		</template>
		<template #dialog>
			<DialogBonus></DialogBonus>
		</template>
	</ContainerGradeType>
</template>

<style scoped>

</style>