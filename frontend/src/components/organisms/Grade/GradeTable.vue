<script setup lang="ts">
import { useQuery } from "@tanstack/vue-query"
import { cn } from "@/utils/style"
import { watch } from "vue"
import {
	getTeamTotalGrade
} from "@/services/grade/grade.service"
import { hasPermission } from "@/services/user/user.service"
import { Cookies } from "@/utils/cookie"
import { getTeamByUserId, getTeams } from "@/services/team/team.service"
import CommentContainer from "@/components/organisms/rating/CommentContainer.vue"
import FullGradeTable from "@/components/organisms/Grade/FullGradeTable.vue"
import TeamGradeTable from "@/components/organisms/Grade/TeamGradeTable.vue"

const gradeConfirmed = cn("bg-green-100")
const gradeNotConfirmed = cn("bg-red-100")
const props = defineProps<{
	teamId : string,
	sprintId : string,
	isGradesConfirmed: boolean,
}>()

let oldTeamId = ""

const { data: teams, ...queryTeams } = useQuery({ queryKey: ["teams"], queryFn: getTeams })


const { data: totalGrade, ...queryTotalGrade } = useQuery({
	queryKey: ["totalGrade", props.teamId, props.sprintId],
	queryFn: () => getTeamTotalGrade(Number(props.teamId), Number(props.sprintId))
})


const currentUserId = Cookies.getUserId()
const { data: currentUserTeam } = useQuery({
	queryKey: ["team", currentUserId],
	queryFn: async() => getTeamByUserId(currentUserId)
})

watch(() => props.teamId, async() => {
	if (props.teamId !== oldTeamId) {
		await queryTeams.refetch()
		await queryTotalGrade.refetch()
		oldTeamId = props.teamId
	}
})


const canViewAllWg = hasPermission("VIEW_ALL_WRITING_GRADES")
const canViewOwnTeamGrade = hasPermission("VIEW_OWN_TEAM_GRADE")
const canViewAllOg = hasPermission("VIEW_ALL_ORAL_GRADES")

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
		<TeamGradeTable :sprint-id="props.sprintId" :team-id="props.teamId"></TeamGradeTable>
	</div>
</template>

<style scoped>

</style>