<script setup lang="ts">

import { useQuery } from "@tanstack/vue-query"
import { getStudentsByTeamId } from "@/services/student-service"
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
import { watch } from "vue"
import { getStudentsAverageByTeam, getTeamAverage } from "@/services/grade-service"
import { getStudentBonus, getStudentBonuses } from "@/services/bonus-service"
import { hasPermission } from "@/services/user-service"
import { Cookies } from "@/utils/cookie"
import { getTeamByUserId } from "@/services/team-service"

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

const gradeTypeGroupNameArray = ["Solution Technique", "Gestion de projet", "Contenu de la présentation", "Support de présentation", "Performance globale de l'équipe"]
const gradeTypeIndividualArray = ["Performance individuelle"]

const { data: averageTeam, ...queryAverageTeam } = useQuery({
	queryKey: ["average", "team", props.teamId],
	queryFn: () => getTeamAverage(Number(props.teamId), props.sprintId)
})

const { data: averageStudents, ...queryAverageStudent } = useQuery({
	queryKey: ["average", "student", props.teamId],
	queryFn: async() => {
		return await getStudentsAverageByTeam(Number(props.teamId), props.sprintId)
	}
})

const { data: studentBonuses, refetch: refetchBonuses } = useQuery({
	queryKey: ["student-bonuses"],
	queryFn: async() => {
		if (!teamStudents.value) return null
		return await Promise.all(teamStudents.value.map(student => getStudentBonuses(student.id)))
	}
})


const currentUserId = Cookies.getUserId()
const { data: currentUserTeam } = useQuery({ queryKey: ["team", currentUserId], queryFn: async() => getTeamByUserId(currentUserId) })

watch(() => props.teamId, async() => {
	if (props.teamId !== oldTeamId) {
		await refetch()
		await queryAverageTeam.refetch()
		await queryAverageStudent.refetch()
		await refetchBonuses()
		oldTeamId = props.teamId
	}
})

const canViewOwnWg = hasPermission("VIEW_OWN_GRADES_WG")
const canViewAllWg = hasPermission("VIEW_ALL_WRITING_GRADES")
const canViewOwnSg = hasPermission("VIEW_OWN_SPRINT_GRADE")
const canViewAllSg = hasPermission("VIEW_ALL_SPRINTS_GRADES")
const canViewOwnTeamGrade = hasPermission("VIEW_OWN_TEAM_GRADE")
const canViewTeamGrade = hasPermission("VIEW_TEAM_GRADE")
const canViewAllOg = hasPermission("VIEW_ALL_ORAL_GRADES")

const canView = canViewAllOg || (canViewOwnTeamGrade && currentUserTeam && currentUserTeam.value?.id === Number(props.teamId)) || canViewAllWg

</script>

<template>
	<Table>
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
				<TableHead v-if="canViewAllOg || canViewOwnSg" :class="rowClass" >Note finale</TableHead>
			</TableRow>
		</TableHeader>
		<TableBody>
			<TableRow v-for="(student, index) in teamStudents" >
				<TableCell class="font-medium" :class="rowClass">{{student.name}}</TableCell>
				<TableCell v-if="canView && averageTeam" :class="rowClass">{{averageTeam["Solution Technique"]}}</TableCell>
				<TableCell v-if="canView && averageTeam" :class="rowClass">{{averageTeam["Gestion de projet"]}}</TableCell>
				<TableCell v-if="canView && averageTeam" :class="rowClass">{{averageTeam["Conformité au sprint"]}}</TableCell>
				<TableCell v-if="canView && averageTeam" :class="rowClass">{{averageTeam["Support de présentation"]}}</TableCell>
				<TableCell :class="rowClass"> / </TableCell>
				<TableCell v-if="canView && studentBonuses" :class="rowClass">{{ studentBonuses[index][1].value}} </TableCell>
				<TableCell v-if="(canViewAllWg || canViewAllOg || student.id === currentUserId) && studentBonuses" :class="rowClass">{{ studentBonuses[index][0].value}} </TableCell>
				<TableCell v-if="canViewAllWg || canViewAllOg || student.id === currentUserId" :class="rowClass">  / </TableCell>
				<TableCell v-if="canView && averageTeam" :class="rowClass"> {{averageTeam["Performance globale de l'équipe"]}} </TableCell>
				<TableCell v-if="(canViewAllWg || canViewAllOg || student.id === currentUserId) && averageStudents" :class="rowClass">{{averageStudents[student.id]}}</TableCell>
				<TableCell v-if="canViewAllWg || canViewAllOg || student.id === currentUserId" :class="rowClass">  </TableCell>
				<TableCell v-if="canViewAllSg || (canViewOwnSg && student.id === currentUserId)" :class="rowClass">  </TableCell>
			</TableRow>
		</TableBody>
	</Table>
</template>

<style scoped>

</style>