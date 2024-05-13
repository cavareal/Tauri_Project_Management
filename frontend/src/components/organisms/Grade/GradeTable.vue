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
import { getAllImportedGradeTypes } from "@/services/grade-type-service"
import { getAllImportedGrades, getAllUnimportedGrades } from "@/services/grade-service"

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

const { data: grades, ...gradesQuery } = useQuery({ queryKey: ["grades"], queryFn: getAllUnimportedGrades })

watch(() => props.teamId, async() => {
	if (props.teamId !== oldTeamId) {
		await refetch()
		await gradesQuery.refetch()
		oldTeamId = props.teamId
	}
})

</script>

<template>
	<Table>
		<TableHeader>
			<TableRow>
				<TableHead :class="rowClass" >Nom</TableHead>
				<TableHead :class="rowClass" ><Blocks :stroke-width="1"/></TableHead>
				<TableHead :class="rowClass" ><SquareGanttChart :stroke-width="1"/></TableHead>
				<TableHead :class="rowClass" ><Package :stroke-width="1"/></TableHead>
				<TableHead :class="rowClass" ><Presentation :stroke-width="1"/></TableHead>
				<TableHead :class="rowClass" >Total équipes</TableHead>
				<TableHead :class="rowClass" ><LucideCircleFadingPlus :stroke-width="1"/></TableHead>
				<TableHead :class="rowClass" ><LucideCirclePlus :stroke-width="1"/></TableHead>
				<TableHead :class="rowClass" >Total bonus</TableHead>
				<TableHead :class="rowClass" ><Users :stroke-width="1"/></TableHead>
				<TableHead :class="rowClass" ><UserCog :stroke-width="1"/></TableHead>
				<TableHead :class="rowClass" ><User :stroke-width="1"/></TableHead>
				<TableHead :class="rowClass" >Total individuel</TableHead>
				<TableHead :class="rowClass" >Note finale</TableHead>
			</TableRow>
		</TableHeader>
		<TableBody>
			<TableRow v-for="student in teamStudents"  :key="student.id">
				<TableCell class="font-medium" :class="rowClass">{{student.name}}</TableCell>
			</TableRow>
		</TableBody>
	</Table>
</template>

<style scoped>

</style>