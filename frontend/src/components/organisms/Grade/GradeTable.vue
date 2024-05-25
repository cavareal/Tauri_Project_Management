<script setup lang="ts">

import { useQuery } from "@tanstack/vue-query"
import { getStudentsByTeamId } from "@/services/student/student.service"
import { cn } from "@/utils/style"
import { Table, TableBody, TableCell, TableHead, TableHeader, TableRow } from "@/components/ui/table"
import {
	Users,
	LucideCircleFadingPlus,
	LucideCirclePlus,
	User,
	SquareGanttChart,
	Presentation,
	Play,
	Blocks
} from "lucide-vue-next"
import { ref, watch } from "vue"
import {
	getStudentsAverageByTeam,
	getTeamAverage,
	getSprintGrades,
	getTeamTotalGrade,
	getIndividualTotalGrades
} from "@/services/grade/grade.service"
import { getStudentBonuses } from "@/services/bonus/bonus.service"
import { hasPermission } from "@/services/user/user.service"
import { Cookies } from "@/utils/cookie"
import { getTeamByUserId } from "@/services/team/team.service"
import Tabs from "@/components/molecules/tab/Tabs.vue"
import Tab from "@/components/molecules/tab/Tab.vue"
import CommentContainer from "@/components/organisms/rating/CommentContainer.vue"
import type { Bonus } from "@/types/bonus"

const rowClass = cn("py-2 h-auto mt-2 mb-2")
const props = defineProps<{
	teamId : string,
	sprintId : string,
}>()

const studentBonuses = ref<Bonus[][] | null>(null)

let oldTeamId = ""

const { data: teamStudents, ...queryTeamStudents } = useQuery({
	queryKey: ["team-students", props.teamId],
	queryFn: async() => {
		if (props.teamId === "") return
		const students = await getStudentsByTeamId(Number(props.teamId))
		studentBonuses.value = await Promise.all(students.map(student => getStudentBonuses(student.id, props.sprintId)))
		return students
	}
})

const { data: averageTeam, ...queryAverageTeam } = useQuery({
	queryKey: ["average", "team", props.teamId, props.sprintId],
	queryFn: () => getTeamAverage(Number(props.teamId), props.sprintId)
})

const { data: averageStudents, ...queryAverageStudent } = useQuery({
	queryKey: ["average", "student", props.teamId, props.sprintId],
	queryFn: async() => {
		return await getStudentsAverageByTeam(Number(props.teamId), props.sprintId)
	}
})

// const { data: studentBonuses, refetch: refetchBonuses } = useQuery({
// 	queryKey: ["student-bonuses", teamStudents.value],
// 	queryFn: async() => {
// 		if (!teamStudents.value) return null
// 		return await Promise.all(teamStudents.value.map(student => getStudentBonuses(student.id)))
// 	}
// })

const { data: sprintGrades, ...querySprintGrade } = useQuery({
	queryKey: ["sprint-grades", props.teamId, props.sprintId],
	queryFn: () => getSprintGrades(Number(props.teamId), Number(props.sprintId))
})

const { data: totalGrade, ...queryTotalGrade } = useQuery({
	queryKey: ["totalGrade", props.teamId, props.sprintId],
	queryFn: () => getTeamTotalGrade(Number(props.teamId), Number(props.sprintId))
})

const { data: totalIndividualGrades, ...queryTotalIndividualGrades } = useQuery({
	queryKey: ["individual", props.teamId, props.sprintId],
	queryFn: () => getIndividualTotalGrades(Number(props.teamId), Number(props.sprintId))
})

const currentUserId = Cookies.getUserId()
const { data: currentUserTeam } = useQuery({
	queryKey: ["team", currentUserId],
	queryFn: async() => getTeamByUserId(currentUserId)
})

watch(() => props.teamId, async() => {
	if (props.teamId !== oldTeamId) {
		await queryTeamStudents.refetch()
		await queryAverageTeam.refetch()
		// await refetchBonuses()
		await queryAverageStudent.refetch()
		await querySprintGrade.refetch()
		await queryTotalGrade.refetch()
		await queryTotalIndividualGrades.refetch()
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
	<Table v-if="(canViewAllWg || canViewAllOg) && queryTotalGrade.isFetched">
		<TableHeader>
			<TableRow>
				<TableHead :class="rowClass" >Nom</TableHead>
				<TableHead v-if="canView" :class="rowClass" title="Solution Technique"><Blocks :stroke-width="1"/></TableHead>
				<TableHead v-if="canView" :class="rowClass" title="Gestion de projet"><SquareGanttChart :stroke-width="1"/></TableHead>
				<TableHead v-if="canView" :class="rowClass" title="Conformité au sprint"><Play :stroke-width="1"/></TableHead>
				<TableHead v-if="canView" :class="rowClass" title="Support de présentation"><Presentation :stroke-width="1"/></TableHead>
				<TableHead v-if="canView" :class="rowClass" >Total équipes</TableHead>
				<TableHead v-if="canView" :class="rowClass" title="Bonus / Malus limités"><LucideCircleFadingPlus :stroke-width="1"/></TableHead>
				<TableHead v-if="canView" :class="rowClass" title="Bonus / Malus illimités"><LucideCirclePlus :stroke-width="1"/></TableHead>
				<TableHead v-if="canView" :class="rowClass" >Total bonus</TableHead>
				<TableHead v-if="canView" :class="rowClass" title="Performance globale de l'équipe"><Users :stroke-width="1"/></TableHead>
				<TableHead v-if="canView" :class="rowClass" title="Performance individuelle"><User :stroke-width="1"/></TableHead>
				<TableHead v-if="canView" :class="rowClass" >Total individuel</TableHead>
				<TableHead v-if="canView" :class="rowClass" >Note finale</TableHead>
			</TableRow>
		</TableHeader>
		<TableBody>
			<TableRow v-for="(student, index) in teamStudents" :key="student.id">
				<TableCell class="font-medium" :class="rowClass">{{student.name}}</TableCell>
				<TableCell v-if="canView && averageTeam" :class="rowClass">{{averageTeam["Solution Technique"]}}</TableCell>
				<TableCell v-if="canView && averageTeam" :class="rowClass">{{averageTeam["Gestion de projet"]}}</TableCell>
				<TableCell v-if="canView && averageTeam" :class="rowClass">{{averageTeam["Conformité au sprint"]}}</TableCell>
				<TableCell v-if="canView && averageTeam" :class="rowClass">{{averageTeam["Support de présentation"]}}</TableCell>
				<TableCell v-if="totalGrade" :class="rowClass"> {{totalGrade}} </TableCell>
				<TableCell v-if="canView && studentBonuses" :class="rowClass">{{ studentBonuses[index][1].value}} </TableCell>
				<TableCell v-if="(canViewAllWg || canViewAllOg ) && studentBonuses" :class="rowClass">{{ studentBonuses[index][0].value }} </TableCell>
				<TableCell v-if="(canViewAllWg || canViewAllOg ) && studentBonuses" :class="rowClass">  {{ (studentBonuses[index][1].value ? studentBonuses[index][1].value : 0) + (studentBonuses[index][0].value ? studentBonuses[index][0].value : 0) }} </TableCell>
				<TableCell v-if="canView && averageTeam" :class="rowClass"> {{averageTeam["Performance globale de l'équipe"]}} </TableCell>
				<TableCell v-if="(canViewAllWg || canViewAllOg ) && averageStudents" :class="rowClass">{{averageStudents[student.id]}}</TableCell>
				<TableCell v-if="(canViewAllWg || canViewAllOg || student.id === currentUserId) && totalIndividualGrades" :class="rowClass"> {{totalIndividualGrades[index].toPrecision(4) ? totalIndividualGrades[index] : 0}} </TableCell>
				<TableCell v-if="(canViewAllSg || (canViewOwnSg && student.id === currentUserId)) && sprintGrades" :class="rowClass"> {{sprintGrades[index].toPrecision(4)}} </TableCell>
			</TableRow>
		</TableBody>
	</Table>
	<Tabs v-if="(canViewOwnTeamGrade && currentUserTeam && Number(currentUserTeam.id) === Number(props.teamId))">
		<Tab title="Mes notes" >

		</Tab>
		<Tab title="Mon équipe">
			<Table>
				<TableHeader>
					<TableRow>
						<TableHead :class="rowClass" >Nom</TableHead>
						<TableHead :class="rowClass" title="Solution Technique"><Blocks :stroke-width="1"/></TableHead>
						<TableHead :class="rowClass" title="Gestion de projet"><SquareGanttChart :stroke-width="1"/></TableHead>
						<TableHead :class="rowClass" title="Conformité au sprint"><Play :stroke-width="1"/></TableHead>
						<TableHead :class="rowClass" title="Support de présentation"><Presentation :stroke-width="1"/></TableHead>
						<TableHead :class="rowClass" >Total équipes</TableHead>
						<TableHead :class="rowClass" title="Bonus / Malus limités"><LucideCircleFadingPlus :stroke-width="1"/></TableHead>
						<TableHead :class="rowClass" title="Bonus / Malus illimités"><LucideCirclePlus :stroke-width="1"/></TableHead>
						<TableHead :class="rowClass" >Total bonus</TableHead>
						<TableHead :class="rowClass" title="Performance globale de l'équipe"><Users :stroke-width="1"/></TableHead>
						<TableHead :class="rowClass" title="Performance individuelle"><User :stroke-width="1"/></TableHead>
						<TableHead :class="rowClass" >Total individuel</TableHead>
						<TableHead :class="rowClass" >Note finale</TableHead>
					</TableRow>
				</TableHeader>
				<TableBody>
					<TableRow v-for="(student, index) in teamStudents" >
						<TableCell class="font-medium" :class="rowClass">{{student.name}}</TableCell>
						<TableCell v-if="averageTeam" :class="rowClass">{{averageTeam["Solution Technique"]}}</TableCell>
						<TableCell v-if="averageTeam" :class="rowClass">{{averageTeam["Gestion de projet"]}}</TableCell>
						<TableCell v-if="averageTeam" :class="rowClass">{{averageTeam["Conformité au sprint"]}}</TableCell>
						<TableCell v-if=" averageTeam" :class="rowClass">{{averageTeam["Support de présentation"]}}</TableCell>
						<TableCell v-if="totalGrade" :class="rowClass"> {{totalGrade}} </TableCell>
						<TableCell v-if="studentBonuses" :class="rowClass">{{ studentBonuses[index][1].value}} </TableCell>
						<TableCell v-if="studentBonuses" :class="rowClass">{{ studentBonuses[index][0].value}} </TableCell>
						<TableCell v-if="studentBonuses" :class="rowClass">   {{ studentBonuses[index][1].value + studentBonuses[index][0].value  }} </TableCell>
						<TableCell v-if="averageTeam" :class="rowClass"> {{averageTeam["Performance globale de l'équipe"]}} </TableCell>
						<TableCell v-if=" averageStudents" :class="rowClass">{{averageStudents[student.id]}}</TableCell>
						<TableCell v-if="totalIndividualGrades" :class="rowClass"> {{totalIndividualGrades[index].toPrecision(4) ? totalIndividualGrades[index] : 0}} </TableCell>
						<TableCell v-if="sprintGrades" :class="rowClass"> {{sprintGrades[index].toPrecision(4) }} </TableCell>
					</TableRow>
				</TableBody>
			</Table>
			<CommentContainer  title="Feedback" infoText="Visualisez les feedbacks donner à votre équipe durant le sprint" :sprintId="props.sprintId" :teamId="props.teamId" :feedback="true"/>
		</Tab>
	</Tabs>
	<Table  v-if="(canViewOwnTeamGrade && currentUserTeam && Number(currentUserTeam.id) !== Number(props.teamId))">
		<TableHeader>
			<TableRow>
				<TableHead :class="rowClass" >Nom</TableHead>
				<TableHead :class="rowClass" >Note finale</TableHead>
			</TableRow>
		</TableHeader>
		<TableBody>
			<TableRow v-for="(student, index) in teamStudents" >
				<TableCell class="font-medium" :class="rowClass">{{student.name}}</TableCell>
				<TableCell v-if="sprintGrades" :class="rowClass"> {{sprintGrades[index].toPrecision(4)}}</TableCell>
			</TableRow>
		</TableBody>
	</Table>
</template>

<style scoped>

</style>