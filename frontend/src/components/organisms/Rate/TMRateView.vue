<script setup lang="ts">
import { ContainerGradeType } from "@/components/molecules/rateContainer"
import { Button } from "@/components/ui/button"
import { Users, LucideCircleFadingPlus } from "lucide-vue-next"
import { DialogRating, DialogViewGrades, DialogBonus } from "@/components/organisms/rating"
import { useQuery } from "@tanstack/vue-query"
import { getGradeTypeByName } from "@/services/grade-type-service"
import type { GradeType } from "@/types/grade-type"


const props = defineProps<{
	teamId : string,
	sprintId : string
}>()

const { data: gradeType } = useQuery<GradeType, Error>({
	queryKey: ["grade-type"],
	queryFn: () => getGradeTypeByName("Performance globale de l'équipe")
})
</script>

<template>
	<ContainerGradeType title="Note Globale de présentation" infotext="Vous devez évaluer chaque équipe sur sa présentation globale">
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
	<ContainerGradeType title="Bonus et malus de mon équipe" infotext="Vous pouvez attribuer des bonus et des malus à votre équipe">
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