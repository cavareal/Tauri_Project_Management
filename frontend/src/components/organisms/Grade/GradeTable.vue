<script setup lang="ts">

import { useQuery } from "@tanstack/vue-query"
import { getStudentsByTeamId } from "@/services/student-service"
import { cn } from "@/utils/style"
import { Table, TableBody, TableCell, TableHead, TableHeader, TableRow } from "@/components/ui/table"
import {
	Users,
	UserCog,
	LucideCircleFadingPlus,
	LucideCirclePlus,
	User,
	SquareGanttChart,
	Play,
	Presentation,
	Package,
	Blocks
} from "lucide-vue-next"
import { watch } from "vue"
import { getAverageByGradeType } from "@/services/grade-service"

const rowClass = cn("py-2 h-auto mt-2 mb-2")
const props = defineProps<{
	teamId : string,
	sprintId : string,
}>()

let oldTeamId = props.teamId

const { data: teamStudents, refetch } = useQuery({ queryKey: ["team-students"], queryFn: async() => {
	if (!props.teamId) return
	return await getStudentsByTeamId(Number(props.teamId))
} })


const useAverageQuery = (id: number, gradeTypeName: string) => {
	let { data: average, error } = useQuery({
		queryKey: ["average", gradeTypeName, id],
		queryFn: async() => {
			return await getAverageByGradeType(id, Number(props.sprintId), gradeTypeName)
		}
	})
	if (error) {
		average.value = -1
	}
	return average
}

watch(() => props.teamId, async() => {
	if (props.teamId !== oldTeamId) {
		await refetch()
		oldTeamId = props.teamId
	}
})

</script>

<template>
	<Table>
		<TableHeader>
			<TableRow>
				<TableHead :class="rowClass" >Nom</TableHead>
				<TableHead :class="rowClass" title="Solution Technique"><Blocks :stroke-width="1"/></TableHead>
				<TableHead :class="rowClass" title="Gestion de projet"><SquareGanttChart :stroke-width="1"/></TableHead>
				<TableHead :class="rowClass" title="Contenu de la présentation"><Package :stroke-width="1"/></TableHead>
				<TableHead :class="rowClass" title="Support de présentation"><Presentation :stroke-width="1"/></TableHead>
				<TableHead :class="rowClass" >Total équipes</TableHead>
				<TableHead :class="rowClass" title="Bonus / Malus limités"><LucideCircleFadingPlus :stroke-width="1"/></TableHead>
				<TableHead :class="rowClass" title="Bonus / Malus illimités"><LucideCirclePlus :stroke-width="1"/></TableHead>
				<TableHead :class="rowClass" >Total bonus</TableHead>
				<TableHead :class="rowClass" title="Performance globale de l'équipe"><Users :stroke-width="1"/></TableHead>
				<TableHead :class="rowClass" ><UserCog :stroke-width="1"/></TableHead>
				<TableHead :class="rowClass" title="Performance individuelle"><User :stroke-width="1"/></TableHead>
				<TableHead :class="rowClass" >Total individuel</TableHead>
				<TableHead :class="rowClass" >Note finale</TableHead>
			</TableRow>
		</TableHeader>
		<TableBody>
			<TableRow v-for="student in teamStudents"  :key="student.id">
				<TableCell class="font-medium" :class="rowClass">{{student.name}}</TableCell>
<!--				<TableCell :class="rowClass">{{useAverageQuery(Number(props.teamId), "Solution Technique")}}</TableCell>-->
				<TableCell :class="rowClass">{{ useAverageQuery(Number(props.teamId), 'Solution Technique').value >= 0 ? useAverageQuery(Number(props.teamId), 'Solution Technique').value : ''}}</TableCell>
				<TableCell :class="rowClass">{{ useAverageQuery(Number(props.teamId), "Gestion de projet").value >= 0 ? useAverageQuery(Number(props.teamId), 'Gestion de projet').value : '' }}</TableCell>
				<TableCell :class="rowClass">{{ useAverageQuery(Number(props.teamId), 'Contenu de la présentation').value >= 0 ? useAverageQuery(Number(props.teamId), 'Contenu de la présentation').value : '' }}</TableCell>
				<TableCell :class="rowClass">{{ useAverageQuery(Number(props.teamId), 'Support de présentation').value >= 0 ? useAverageQuery(Number(props.teamId), 'Support de présentation').value : ''}}</TableCell>
				<TableCell :class="rowClass">  </TableCell>
				<TableCell :class="rowClass">  </TableCell>
				<TableCell :class="rowClass">  </TableCell>
				<TableCell :class="rowClass">  </TableCell>
				<TableCell :class="rowClass">{{ useAverageQuery(Number(props.teamId), "Performance globale de l'équipe").value >= 0 ? useAverageQuery(Number(props.teamId), "Performance globale de l'équipe").value : ''}}</TableCell>
				<TableCell :class="rowClass">  </TableCell>
				<TableCell :class="rowClass">{{ useAverageQuery(Number(student.id), "Performance individuelle").value >= 0 ? useAverageQuery(Number(student.id), "Performance individuelle").value : ''}}</TableCell>
				<TableCell :class="rowClass">  </TableCell>
				<TableCell :class="rowClass">  </TableCell>
			</TableRow>
		</TableBody>
	</Table>
</template>

<style scoped>

</style>