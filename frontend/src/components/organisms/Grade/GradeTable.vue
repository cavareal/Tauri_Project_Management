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
	Blocks,
	Info
} from "lucide-vue-next"
import { ref, watch } from "vue"
import {
	getStudentsAverageByTeam,
	getTeamAverage,
	getSprintGrades,
	getTeamTotalGrade,
	getIndividualTotalGrades,  getAverageSprintGrades
} from "@/services/grade/grade.service"
import { getStudentBonuses } from "@/services/bonus/bonus.service"
import { hasPermission } from "@/services/user/user.service"
import { Cookies } from "@/utils/cookie"
import { getTeamByUserId, getTeams } from "@/services/team/team.service"
import CommentContainer from "@/components/organisms/rating/CommentContainer.vue"
import type { Bonus } from "@/types/bonus"
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

const studentBonuses = ref<Bonus[][] | null>(null)

let oldTeamId = ""

const { data: teams, ...queryTeams } = useQuery({ queryKey: ["teams"], queryFn: getTeams })

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

const { data: sprintGrades, ...querySprintGrades } = useQuery({
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
		await queryTeamStudents.refetch()
		await queryAverageTeam.refetch()
		await queryAverageStudent.refetch()
		await querySprintGrades.refetch()
		await queryTotalGrade.refetch()
		await queryTotalIndividualGrades.refetch()
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
<!--		<FullGradeTable :sprint-id="props.sprintId" :team-id="props.teamId" :is-grades-confirmed="props.isGradesConfirmed"></FullGradeTable>-->
		<Table>
			<TableHeader>
				<TableRow>
					<TableHead :class="rowClass">Nom</TableHead>
					<TableHead :class="rowClass" title="Solution Technique"><Blocks :stroke-width="1"/></TableHead>
					<TableHead :class="rowClass" title="Gestion de projet"><SquareGanttChart :stroke-width="1"/></TableHead>
					<TableHead :class="rowClass" title="Conformité au sprint"><Play :stroke-width="1"/></TableHead>
					<TableHead :class="rowClass" title="Support de présentation"><Presentation :stroke-width="1"/></TableHead>
					<TableHead :class="rowClass" >
						<Row>
							<div class="mr-2">Total équipe</div>
							<Tooltip expression="\text{Total équipe}= \\ \frac{\text{Solution Technique} + \text{Gestion de projet} + \text{Conformité au sprint + \text{Présentation}} }{4}" position="left-0">
								<Info :stroke-width="1" :size="20"/>
							</Tooltip>
						</Row>
					</TableHead>
					<TableHead :class="rowClass" title="Bonus / Malus limités"><LucideCircleFadingPlus :stroke-width="1"/></TableHead>
					<TableHead :class="rowClass" title="Bonus / Malus illimités"><LucideCirclePlus :stroke-width="1"/></TableHead>
					<TableHead :class="rowClass" >
						<Row>
							<div class="mr-2">Total Bonus</div>
							<Tooltip expression="\text{Total Bonus}= \text{Bonus/Malus limités} + \text{Bonus/Malus illimités}" position="right-0">
								<Info :stroke-width="1" :size="20"/>
							</Tooltip>
						</Row>
					</TableHead>
					<TableHead :class="rowClass" title="Performance globale de l'équipe"><Users :stroke-width="1"/></TableHead>
					<TableHead :class="rowClass" title="Performance individuelle"><User :stroke-width="1"/></TableHead>
					<TableHead :class="rowClass" >
						<Row>
							<div class="mr-2">Total individuel</div>
							<Tooltip expression="\text{Total Individuel}= \\ \frac{\text{Performance Globale} + 2 \times \text{Performance Individuelle}}{3}" position="right-0">
								<Info :stroke-width="1" :size="20"/>
							</Tooltip>
						</Row>
					</TableHead>
					<TableHead :class="rowClass" >
						<Row>
							<div class="mr-2">Note finale</div>
							<Tooltip expression="\text{Note finale}= \\ 0.7 \times (\text{Total équipes + Total Bonus}) + \ 0.3 \times \text{Total Individuel}" position="right-0">
								<Info :stroke-width="1" :size="20"/>
							</Tooltip>
						</Row>
					</TableHead>
				</TableRow>
			</TableHeader>
			<TableBody>
				<TableRow v-for="(student, index) in teamStudents">
					<TableCell class="font-medium" :class="rowClass">{{student.name}}</TableCell>
					<TableCell v-if="averageTeam" :class="rowClass">{{averageTeam["Solution Technique"]}}</TableCell>
					<TableCell v-if="averageTeam" :class="rowClass">{{averageTeam["Gestion de projet"]}}</TableCell>
					<TableCell v-if="averageTeam" :class="rowClass">{{averageTeam["Conformité au sprint"]}}</TableCell>
					<TableCell v-if="averageTeam" :class="rowClass">{{averageTeam["Support de présentation"]}}</TableCell>
					<TableCell v-if="totalGrade" :class="rowClass"> {{totalGrade}} </TableCell>
					<TableCell v-if="studentBonuses" :class="rowClass">{{ studentBonuses[index][1].value}} </TableCell>
					<TableCell v-if="studentBonuses" :class="rowClass">{{ studentBonuses[index][0].value }} </TableCell>
					<TableCell v-if="studentBonuses" :class="rowClass">  {{ (studentBonuses[index][1].value ? studentBonuses[index][1].value : 0) + (studentBonuses[index][0].value ? studentBonuses[index][0].value : 0) }} </TableCell>
					<TableCell v-if="averageTeam" :class="rowClass"> {{averageTeam["Performance globale de l'équipe"]}} </TableCell>
					<TableCell v-if="averageStudents" :class="rowClass">{{averageStudents[student.id]}}</TableCell>
					<TableCell v-if="totalIndividualGrades" :class="rowClass"> {{totalIndividualGrades[index].toPrecision(4) ? totalIndividualGrades[index] : 0}} </TableCell>
					<TableCell v-if="sprintGrades" :class="rowClass"> {{sprintGrades[index]}} </TableCell>
				</TableRow>
			</TableBody>
		</Table>
	</div>
	<div v-if="(canViewOwnTeamGrade && currentUserTeam && Number(currentUserTeam.id) === Number(props.teamId))" >
		<div class="border bg-white rounded-md mb-4">
			<Table>
				<TableHeader>
					<TableRow>
						<TableHead :class="rowClass">Nom</TableHead>
						<TableHead :class="rowClass" title="Solution Technique"><Blocks :stroke-width="1"/></TableHead>
						<TableHead :class="rowClass" title="Gestion de projet"><SquareGanttChart :stroke-width="1"/></TableHead>
						<TableHead :class="rowClass" title="Conformité au sprint"><Play :stroke-width="1"/></TableHead>
						<TableHead :class="rowClass" title="Support de présentation"><Presentation :stroke-width="1"/></TableHead>
						<TableHead :class="rowClass" >
							<Row>
								<div class="mr-2">Total équipe</div>
								<Tooltip expression="\text{Total équipe}= \\ \frac{\text{Solution Technique} + \text{Gestion de projet} + \text{Conformité au sprint + \text{Présentation}} }{4}" position="left-0">
									<Info :stroke-width="1" :size="20"/>
								</Tooltip>
							</Row>
						</TableHead>
						<TableHead :class="rowClass" title="Bonus / Malus limités"><LucideCircleFadingPlus :stroke-width="1"/></TableHead>
						<TableHead :class="rowClass" title="Bonus / Malus illimités"><LucideCirclePlus :stroke-width="1"/></TableHead>
						<TableHead :class="rowClass" >
							<Row>
								<div class="mr-2">Total Bonus</div>
								<Tooltip expression="\text{Total Bonus}= \text{Bonus/Malus limités} + \text{Bonus/Malus illimités}" position="right-0">
									<Info :stroke-width="1" :size="20"/>
								</Tooltip>
							</Row>
						</TableHead>
						<TableHead :class="rowClass" title="Performance globale de l'équipe"><Users :stroke-width="1"/></TableHead>
						<TableHead :class="rowClass" title="Performance individuelle"><User :stroke-width="1"/></TableHead>
						<TableHead :class="rowClass" >
							<Row>
								<div class="mr-2">Total individuel</div>
								<Tooltip expression="\text{Total Individuel}= \\ \frac{\text{Performance Globale} + 2 \times \text{Performance Individuelle}}{3}" position="right-0">
									<Info :stroke-width="1" :size="20"/>
								</Tooltip>
							</Row>
						</TableHead>
						<TableHead :class="rowClass" >
							<Row>
								<div class="mr-2">Note finale</div>
								<Tooltip expression="\text{Note finale}= \\ 0.7 \times (\text{Total équipes + Total Bonus}) + \ 0.3 \times \text{Total Individuel}" position="right-0">
									<Info :stroke-width="1" :size="20"/>
								</Tooltip>
							</Row>
						</TableHead>
					</TableRow>
				</TableHeader>
				<TableBody>
					<TableRow v-for="(student, index) in teamStudents">
						<TableCell class="font-medium" :class="rowClass">{{student.name}}</TableCell>
						<TableCell v-if="averageTeam" :class="rowClass">{{averageTeam["Solution Technique"]}}</TableCell>
						<TableCell v-if="averageTeam" :class="rowClass">{{averageTeam["Gestion de projet"]}}</TableCell>
						<TableCell v-if="averageTeam" :class="rowClass">{{averageTeam["Conformité au sprint"]}}</TableCell>
						<TableCell v-if="averageTeam" :class="rowClass">{{averageTeam["Support de présentation"]}}</TableCell>
						<TableCell v-if="totalGrade" :class="rowClass"> {{totalGrade}} </TableCell>
						<TableCell v-if="studentBonuses" :class="rowClass">{{ studentBonuses[index][1].value}} </TableCell>
						<TableCell v-if="studentBonuses" :class="rowClass">{{ studentBonuses[index][0].value }} </TableCell>
						<TableCell v-if="studentBonuses" :class="rowClass">  {{ (studentBonuses[index][1].value ? studentBonuses[index][1].value : 0) + (studentBonuses[index][0].value ? studentBonuses[index][0].value : 0) }} </TableCell>
						<TableCell v-if="averageTeam" :class="rowClass"> {{averageTeam["Performance globale de l'équipe"]}} </TableCell>
						<TableCell v-if="averageStudents" :class="rowClass">{{averageStudents[student.id]}}</TableCell>
						<TableCell v-if="totalIndividualGrades" :class="rowClass"> {{totalIndividualGrades[index].toPrecision(4) ? totalIndividualGrades[index] : 0}} </TableCell>
						<TableCell v-if="sprintGrades" :class="rowClass"> {{sprintGrades[index]}} </TableCell>
					</TableRow>
				</TableBody>
			</Table>
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
					<TableHead :class="rowClass" >Note finale</TableHead>
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