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
import FullGradeTable from "@/components/organisms/Grade/FullGradeTable.vue"
import TeamGradeTable from "@/components/organisms/Grade/TeamGradeTable.vue"
import FeedbacksAndCommentsView from "@/components/organisms/Grade/FeedbacksAndCommentsView.vue"
import { Row } from "@/components/atoms/containers"
import FeedbacksAndCommentForStudents from "@/components/organisms/Grade/FeedbacksAndCommentForStudents.vue"
import IndividualFeedbacks from "@/components/organisms/Grade/IndividualFeedbacks.vue"

const props = defineProps<{
	teamId : string,
	sprintId : string,
	isGradesConfirmed : boolean | undefined
}>()

let oldTeamId = ""

const role = Cookies.getRole()

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
const canViewFeedbacks = hasPermission("VIEW_FEEDBACK")

</script>

<template>
	<div  v-if="(canViewAllWg || canViewAllOg) && queryTotalGrade.isFetched" class="border bg-white rounded-md">
		<FullGradeTable :sprint-id="props.sprintId" :team-id="props.teamId" :is-grades-confirmed="props.isGradesConfirmed"></FullGradeTable>
	</div>
	<div v-if="(canViewOwnTeamGrade && currentUserTeam && Number(currentUserTeam.id) === Number(props.teamId))" >
		<div class="border bg-white rounded-md mb-4">
			<FullGradeTable :sprint-id="props.sprintId" :team-id="props.teamId" :is-grades-confirmed="props.isGradesConfirmed"></FullGradeTable>
		</div>
	</div>
  <Row class="justify-between">
    <FeedbacksAndCommentsView v-if="canViewAllOg || canViewAllWg || canViewOwnTeamGrade" :teamId="props.teamId" :sprintId="props.sprintId" :isFeedback="true"/>
    <FeedbacksAndCommentsView class="ml-2" v-if="((canViewAllWg || canViewAllOg) && queryTotalGrade.isFetched)" :sprint-id="props.teamId" :is-feedback="false" :team-id="props.teamId" />
  </Row>
  <FeedbacksAndCommentForStudents v-if="role !== 'TEAM_MEMBER' && canViewFeedbacks" :sprint-id="props.sprintId" :team-id="props.teamId"></FeedbacksAndCommentForStudents>
  <IndividualFeedbacks v-else :sprint-id="props.sprintId" :teamId="props.teamId"></IndividualFeedbacks>
	<div  v-if="(canViewOwnTeamGrade && currentUserTeam && Number(currentUserTeam.id) !== Number(props.teamId))"  class="border bg-white rounded-md">
		<TeamGradeTable v-if="canViewFeedbacks" :sprint-id="props.sprintId" :team-id="props.teamId"></TeamGradeTable>
	</div>
</template>

<style scoped>

</style>