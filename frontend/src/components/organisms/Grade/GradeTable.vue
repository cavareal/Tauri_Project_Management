<script setup lang="ts">
import { useQuery } from "@tanstack/vue-query"
import { cn } from "@/utils/style"
import { Table, TableBody, TableCell, TableHead, TableHeader, TableRow } from "@/components/ui/table"
import {
	Info
} from "lucide-vue-next"
import { watch } from "vue"
import {
	getSprintGrades,
	getTeamTotalGrade,
	getAverageSprintGrades
} from "@/services/grade/grade.service"
import { hasPermission } from "@/services/user/user.service"
import { Cookies } from "@/utils/cookie"
import { getTeamByUserId, getTeams } from "@/services/team/team.service"
import CommentContainer from "@/components/organisms/rating/CommentContainer.vue"
import FullGradeTable from "@/components/organisms/Grade/FullGradeTable.vue"
import Tooltip from "@/components/molecules/tooltip/Tooltip.vue"
import { Row } from "@/components/atoms/containers"

const rowClass = cn("py-2 h-auto mt-2 mb-2")
const gradeConfirmed = cn("bg-green-100")
const gradeNotConfirmed = cn("bg-red-100")
const props = defineProps<{
	teamId : string,
	sprintId : string,
	isGradesConfirmed: boolean,
}>()

let oldTeamId = ""

const { data: teams, ...queryTeams } = useQuery({ queryKey: ["teams"], queryFn: getTeams })

const { data: sprintGrades, ...querySprintGrades } = useQuery({
	queryKey: ["sprint-grades", props.teamId, props.sprintId],
	queryFn: () => getSprintGrades(Number(props.teamId), Number(props.sprintId))
})

const { data: totalGrade, ...queryTotalGrade } = useQuery({
	queryKey: ["totalGrade", props.teamId, props.sprintId],
	queryFn: () => getTeamTotalGrade(Number(props.teamId), Number(props.sprintId))
})

const { data: averageSprintGrades, ...queryAverageSprintGrades } = useQuery({
	queryKey: ["averageSprintGrade", props.sprintId],
	queryFn: () => getAverageSprintGrades(Number(props.sprintId))
})

const currentUserId = Cookies.getUserId()
const { data: currentUserTeam } = useQuery({
	queryKey: ["team", currentUserId],
	queryFn: async() => getTeamByUserId(currentUserId)
})

watch(() => props.teamId, async() => {
	if (props.teamId !== oldTeamId) {
		await queryTeams.refetch()
		await querySprintGrades.refetch()
		await queryTotalGrade.refetch()
		await queryAverageSprintGrades.refetch()
		oldTeamId = props.teamId
	}
})


const canViewAllWg = hasPermission("VIEW_ALL_WRITING_GRADES")
const canViewOwnSg = hasPermission("VIEW_OWN_SPRINT_GRADE")
const canViewAllSg = hasPermission("VIEW_ALL_SPRINTS_GRADES")
const canViewOwnTeamGrade = hasPermission("VIEW_OWN_TEAM_GRADE")
const canViewAllOg = hasPermission("VIEW_ALL_ORAL_GRADES")

const canView = canViewAllOg || (canViewOwnTeamGrade && currentUserTeam && Number(currentUserTeam.value?.id) === Number(props.teamId)) || canViewAllWg
</script>

<template>
	<div  v-if="(canViewAllWg || canViewAllOg) && queryTotalGrade.isFetched" class="border bg-white rounded-md">
		<FullGradeTable :sprint-id="props.sprintId" :team-id="props.teamId" :is-grades-confirmed="props.isGradesConfirmed"></FullGradeTable>
	</div>
	<div v-if="(canViewOwnTeamGrade && currentUserTeam && Number(currentUserTeam.id) === Number(props.teamId))" >
		<div class="border bg-white rounded-md mb-4">
			<FullGradeTable :sprint-id="props.sprintId" :team-id="props.teamId" :is-grades-confirmed="props.isGradesConfirmed"></FullGradeTable>
		</div>
		<div class="border bg-white rounded-md">
			<CommentContainer  title="Feedback" infoText="Visualisez les feedbacks donner à votre équipe durant le sprint" :sprintId="props.sprintId" :teamId="props.teamId" :feedback="true"/>
		</div>
	</div>
	<div  v-if="(canViewOwnTeamGrade && currentUserTeam && Number(currentUserTeam.id) !== Number(props.teamId))"  class="border bg-white rounded-md">
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
	</div>
</template>

<style scoped>

</style>