<script setup lang="ts">

import { Info } from "lucide-vue-next"
import { Table, TableBody, TableCell, TableHead, TableHeader, TableRow } from "@/components/ui/table"
import Tooltip from "@/components/molecules/tooltip/Tooltip.vue"
import { Row } from "@/components/atoms/containers"
import { cn } from "@/utils/style"
import { useQuery } from "@tanstack/vue-query"
import { getTeams } from "@/services/team"
import { getAverageSprintGrades, getSprintGrades } from "@/services/grade"
import { watch } from "vue"

const rowClass = cn("py-2 h-auto mt-2 mb-2")
let oldTeamId = ""

const props = defineProps<{
	teamId : string,
	sprintId : string,
}>()

const { data: teams, ...queryTeams } = useQuery({ queryKey: ["teams"], queryFn: getTeams })
const { data: sprintGrades, ...querySprintGrades } = useQuery({
	queryKey: ["sprint-grades", props.teamId, props.sprintId],
	queryFn: () => getSprintGrades(Number(props.teamId), Number(props.sprintId))
})

const { data: averageSprintGrades, ...queryAverageSprintGrades } = useQuery({
	queryKey: ["averageSprintGrade", props.sprintId],
	queryFn: () => getAverageSprintGrades(Number(props.sprintId))
})

watch(() => props.teamId, async() => {
	if (props.teamId !== oldTeamId) {
		await queryTeams.refetch()
		await querySprintGrades.refetch()
		await queryAverageSprintGrades.refetch()
		oldTeamId = props.teamId
	}
})
</script>

<template>
	<Table>
		<TableHeader>
			<TableRow>
				<TableHead :class="rowClass" >Nom</TableHead>
				<TableHead :class="rowClass">
					<div class="flex items-center justify-center">
						<Row>
							<div class="mr-2">Note finale</div>
							<Tooltip expression="\text{Note finale}= \\ 0.7 \times (\text{Total équipes + Total Bonus}) + \ 0.3 \times \text{Total Individuel}" position="right-0">
								<Info :stroke-width="1" :size="20"/>
							</Tooltip>
						</Row>
					</div>
				</TableHead>
			</TableRow>
		</TableHeader>
		<TableBody>
			<TableRow v-for="team in teams" >
				<TableCell class="font-medium" :class="rowClass">{{team.name}}</TableCell>
				<TableCell v-if="sprintGrades && averageSprintGrades" :class="rowClass"> {{averageSprintGrades[team.id - 1]}}</TableCell>
			</TableRow>
		</TableBody>
	</Table>
</template>

<style scoped>

</style>