<script setup lang="ts">
import { ContainerGradeType } from "@/components/molecules/rateContainer"
import { Button } from "@/components/ui/button"
import { Users, LucideCircleFadingPlus } from "lucide-vue-next"
import { DialogRating, DialogViewGrades } from "@/components/organisms/rating"

</script>

<template>
	<ContainerGradeType title="Note Globale de présentation" infotext="Vous devez évaluer chaque équipe sur sa présentation globale">
		<template #icon>
			<Users :size="40" :stroke-width="1"/>
		</template>
		<template #dialog>
			<DialogViewGrades title="Voir les notes" description=""></DialogViewGrades>
			<DialogRating title="Note de présentation" description="Veuillez noter la présentation de l'équipe" gradeType="gradeOverallPerformance">
				<template #trigger>
					<Button variant="default">Noter une équipe</Button>
				</template>
			</DialogRating>
		</template>
	</ContainerGradeType>
	<ContainerGradeType title="Bonus et malus de mon équipe" infotext="Vous pouvez attribuer des bonus et des malus à votre équipe">
		<template #icon>
			<LucideCircleFadingPlus :size="40" :stroke-width="1"/>
		</template>
		<template #dialog>
		</template>
	</ContainerGradeType>
</template>

<style scoped>

</style>