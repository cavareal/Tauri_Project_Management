<script setup lang="ts">
import { Blocks, SquareGanttChart, Package, Presentation, User, Play } from "lucide-vue-next"
import { Button } from "@/components/ui/button"
import { ContainerGradeType } from "@/components/molecules/rateContainer"
import { DialogRating, DialogViewGrades } from "@/components/organisms/rating"

</script>


<template>
	<ContainerGradeType title="Solution Technique" infotext="Vous devez évaluer chaque équipe sur la solution technique qui a été mise en œuvre.">
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

	<ContainerGradeType title="Conformité du sprint" infotext="Vous devez évaluer la conformité du sprint des équipes.">
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

	<ContainerGradeType title="Gestion du projet" infotext="Vous devez évaluer chaque équipe sur sa gestion du projet.">
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

	<ContainerGradeType title="Contenu de la présentation" infotext="Vous devez évaluer chaque équipe sur le contenu de sa présentation.">
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

	<ContainerGradeType title="Support de présentation" infotext="Vous devez évaluer chaque équipe sur son support de présentation.">
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

	<ContainerGradeType title="Performance individuelle" infotext="Vous devez évaluer chaque étudiant sur sa performance individuelle lors de sa présentation.">
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
</template>


<style scoped></style>