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
	Package,
	Blocks
} from "lucide-vue-next"
import { watch } from "vue"
import { getAverageByGradeType, getStudentsAverageByTeam, getTeamAverage } from "@/services/grade-service"
import { getStudentBonus } from "@/services/bonus-service"
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

// const useAverageQuery = (id: number, gradeTypeName: string) => {
// 	let { data: average, error } = useQuery({
// 		queryKey: ["average", gradeTypeName, id],
// 		queryFn: async() => {
// 			return await getAverageByGradeType(id, Number(props.sprintId), gradeTypeName)
// 		}
// 	})
// 	if (error) {
// 		average.value = -1
// 	}
// 	return average
// }

const { data: averageTeam, ...queryAverageTeam } = useQuery({
	queryKey: ["average", "team", props.teamId],
	queryFn: async() => {
		return await getTeamAverage(Number(props.teamId), props.sprintId)
	}
})
console.log(averageTeam.value)
const { data: averageStudents, ...queryAverageStudent } = useQuery({
	queryKey: ["average", "student", props.teamId],
	queryFn: async() => {
		return await getStudentsAverageByTeam(Number(props.teamId), props.sprintId)
	}
})

// const getAllStudentsGrades = (teamId: number) => {
// 	if (teamStudents.value && Array.isArray(teamStudents.value)) return []
// 	let allGrades : number[] = []
// 	let i = 0
// 	for (let gradeType of gradeTypeGroupNameArray) {
// 		// let { data: average, error } = useQuery({
// 		// 	queryKey: ["average", gradeType, teamId],
// 		// 	queryFn: async() => {
// 		// 		return await getAverageByGradeType(teamId, Number(props.sprintId), gradeType)
// 		// 	}
// 		// })
// 		// if (error) {
// 		// 	average.value = -1
// 		// }
// 		allGrades[i] = useAverageQuery(teamId, gradeType).value
// 		i += 1
// 	}
// 	// for (let student of Object.entries(teamStudents.value)) {
// 	// 	for (let gradeType of gradeTypeIndividualArray) {
// 	// 		let { data: average, error } = useQuery({
// 	// 			queryKey: ["average", gradeType, student.id],
// 	// 			queryFn: async() => {
// 	// 				return await getAverageByGradeType(student.id, Number(props.sprintId), gradeType)
// 	// 			}
// 	// 		})
// 	// 		allGrades[student.id] = average.value
// 	// 	}
// 	// }

// }

const getBonus = (id: number) => {
	let { data: bonusLimited } = useQuery({
		queryKey: ["bonus", id, true],
		queryFn: async() => {
			return await getStudentBonus(id, true)
		}
	})

	let { data: bonusUnlimited } = useQuery({
		queryKey: ["bonus", id, false],
		queryFn: async() => {
			return await getStudentBonus(id, false)
		}
	})

	let bonusLimitedValue = bonusLimited?.value?.value ?? 0
	let bonusUnlimitedValue = bonusUnlimited?.value?.value ?? 0

	let bonusSum = bonusLimitedValue + bonusUnlimitedValue

	return [bonusLimitedValue, bonusUnlimitedValue, bonusSum]
}

const currentUserId = Cookies.getUserId()
const { data: currentUserTeam } = useQuery({ queryKey: ["team", currentUserId], queryFn: async() => getTeamByUserId(currentUserId) })

// let grades = getAllStudentsGrades(Number(props.teamId))
watch(() => props.teamId, async() => {
	if (props.teamId !== oldTeamId) {
		await refetch()
		await queryAverageTeam.refetch()
		await queryAverageStudent.refetch()
		console.log(averageTeam.value)
		// grades = getAllStudentsGrades(Number(props.teamId))
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
// console.log("team props " + props.teamId)
// console.log("current user team :" + currentUserTeam.value?.id)
</script>

<template>
	<Table>
		<TableHeader>
			<TableRow>
				<TableHead :class="rowClass" >Nom</TableHead>
				<TableHead v-if="canView" :class="rowClass" title="Solution Technique"><Blocks :stroke-width="1"/></TableHead>
				<TableHead v-if="canView" :class="rowClass" title="Gestion de projet"><SquareGanttChart :stroke-width="1"/></TableHead>
				<TableHead v-if="canView" :class="rowClass" title="Contenu de la présentation"><Package :stroke-width="1"/></TableHead>
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
			<TableRow v-for="student in teamStudents"  :key="student.id">
				<TableCell class="font-medium" :class="rowClass">{{student.name}}</TableCell>
				<TableCell v-if="canView && averageTeam" :class="rowClass">{{averageTeam["Solution Technique"]}}</TableCell>
				<TableCell v-if="canView" :class="rowClass"></TableCell>
				<TableCell v-if="canView" :class="rowClass"></TableCell>
				<TableCell v-if="canView" :class="rowClass"></TableCell>
				<TableCell :class="rowClass">  </TableCell>
				<TableCell v-if="canView" :class="rowClass"></TableCell>
				<TableCell v-if="canViewAllWg || canViewAllOg || student.id === currentUserId" :class="rowClass"></TableCell>
				<TableCell v-if="canViewAllWg || canViewAllOg || student.id === currentUserId" :class="rowClass"></TableCell>
				<TableCell v-if="canView" :class="rowClass"></TableCell>
				<TableCell v-if="canViewAllWg || canViewAllOg || student.id === currentUserId" :class="rowClass"></TableCell>
				<TableCell v-if="canViewAllWg || canViewAllOg || student.id === currentUserId" :class="rowClass">  </TableCell>
				<TableCell v-if="canViewAllSg || (canViewOwnSg && student.id === currentUserId)" :class="rowClass">  </TableCell>
			</TableRow>
		</TableBody>
	</Table>
</template>

<style scoped>

</style>