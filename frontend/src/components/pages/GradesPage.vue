<script setup lang="ts">
import { SidebarTemplate } from "@/components/templates"
import NotAuthorized from "@/components/organisms/errors/NotAuthorized.vue"
import NotAutorized from "../organisms/errors/NotAuthorized.vue"
import { computed, ref, watch } from "vue"
import { hasPermission } from "@/services/user-service"
import { useQuery } from "@tanstack/vue-query"
import { getTeams } from "@/services/team-service"
import { getSprints } from "@/services/sprint-service"
import { Select, SelectContent, SelectGroup, SelectItem, SelectTrigger, SelectValue } from "@/components/ui/select"
import { Header } from "@/components/molecules/header"
import { Column } from "@/components/atoms/containers"
import { Cookie, ListChecks } from "lucide-vue-next"
import Grade from "@/components/organisms/Grade/GradeTable.vue"
import { Button } from "@/components/ui/button"
import ValidGradesDialog from "@/components/organisms/Grade/ValidGradesDialog.vue"
import { getGradesConfirmation } from "@/services/grade-service"
import ExportGrades from "../organisms/Grade/ExportGrades.vue"
import type { Team } from "@/types/team"
import type { Sprint } from "@/types/sprint"
import { getTeamByUserId } from "@/services/team-service"
import { Cookies } from "@/utils/cookie"

// Get the current date
const currentDate = new Date()
const authorized = hasPermission("GRADES_PAGE")
const canViewOwnTeamGrade = hasPermission("VIEW_OWN_TEAM_GRADE")
const canConfirmOwnTeamGrade = hasPermission("GRADE_CONFIRMATION")

function forceRerender() {
	refetchGradesConfirmation()
}

const selectedTeamId = ref("")
const selectedSprintId = ref("")
const selectedTeam = ref(null as Team | null)
const selectedSprint = ref(null as Sprint | null)
const currentSprint = ref(null as Sprint | null)

const { data: ssTeam } = useQuery({ queryKey: ["team", Cookies.getUserId()], queryFn: () => getTeamByUserId(Cookies.getUserId()) })
const { data: teams } = useQuery({ queryKey: ["teams"], queryFn: getTeams })
const { data: sprints } = useQuery({ queryKey: ["sprints"], queryFn: async() => {
	const unfilteredSprints = await getSprints()

	let sprint = unfilteredSprints.find(sprint => {
		const startDate = new Date(sprint.startDate)
		startDate.setHours(0, 0, 0, 0)
		const endDate = new Date(sprint.endDate)
		endDate.setHours(23, 59, 59, 999)
		return startDate <= currentDate && currentDate <= endDate
	})

	// If no current sprint is found, find the next upcoming sprint
	if (!sprint) {
		sprint = unfilteredSprints.find(sprint => {
			const startDate = new Date(sprint.startDate)
			startDate.setHours(0, 0, 0, 0)
			return startDate > currentDate
		})
	}

	currentSprint.value = sprint || null

	return unfilteredSprints.filter(sprint => sprint.endType === "NORMAL_SPRINT" || sprint.endType === "FINAL_SPRINT")
} })

const previousSprint = computed(() => {
	if (!currentSprint.value || !sprints.value) return null

	const previousSprints = sprints.value.filter(sprint => sprint.sprintOrder < currentSprint.value.sprintOrder)

	return previousSprints[previousSprints.length - 1] || null
})

function initializeSelectedTeamId(teamsList: Team[]) {
	if (!sessionStorage.getItem("gradesPageSelectedTeamId") || !teamsList.some(team => team.id.toString() === sessionStorage.getItem("gradesPageSelectedTeamId"))) {
		sessionStorage.setItem("gradesPageSelectedTeamId", "")
	}
	selectedTeamId.value = sessionStorage.getItem("gradesPageSelectedTeamId")
	selectedTeam.value = teams.value.find(team => team.id.toString() === selectedTeamId.value)
}

function initializeSelectedSprintId(sprintsList: Sprint[]) {
	if (!sessionStorage.getItem("gradesPageSelectedSprintId") || !sprintsList.some(sprint => sprint.id.toString() === sessionStorage.getItem("gradesPageSelectedSprintId"))) {
		if (!currentSprint.value || (currentSprint.value.endType === "UNGRADED_SPRINT") && !previousSprint.value) {
			sessionStorage.setItem("gradesPageSelectedSprintId", "")
		} else if (!previousSprint.value || currentSprint.value.endType === "FINAL_SPRINT") {
			sessionStorage.setItem("gradesPageSelectedSprintId", currentSprint.value.id.toString())
		} else {
			sessionStorage.setItem("gradesPageSelectedSprintId", previousSprint.value.id.toString())
		}
	}
	selectedSprintId.value = sessionStorage.getItem("gradesPageSelectedSprintId")
	selectedSprint.value = sprints.value.find(sprint => sprint.id.toString() === selectedSprintId.value)
}

if (teams.value) initializeSelectedTeamId(teams.value)
if (sprints.value) initializeSelectedSprintId(sprints.value)

watch(teams, newValue => {
	if (newValue) {
		initializeSelectedTeamId(newValue)
	}
})

watch(sprints, newValue => {
	if (newValue) {
		initializeSelectedSprintId(newValue)
	}
})

//TODO remplacer ces 2 watchs par les mêmes actions dans le forceRerender ?

watch(selectedTeamId, newValue => {
	if (newValue) {
		sessionStorage.setItem("gradesPageSelectedTeamId", newValue)
		selectedTeam.value = teams.value.find(team => team.id.toString() === newValue)
	}
})

watch(selectedSprintId, newValue => {
	if (newValue) {
		sessionStorage.setItem("gradesPageSelectedSprintId", newValue)
		selectedSprint.value = sprints.value.find(sprint => sprint.id.toString() === newValue)
	}
})

const { data: isGradesConfirmed, refetch: refetchGradesConfirmation } = useQuery({
	queryKey: ["grades-confirmation", selectedSprintId.value, selectedTeamId.value],
	queryFn: async() => {
		if (selectedSprintId.value === "" || selectedTeamId.value === "") return false
		if(ssTeam.value.id != undefined){
			return await getGradesConfirmation(parseInt(selectedSprintId.value), parseInt(selectedTeamId.value), ssTeam.value?.id)
		}
	}
})

</script>

<template>
	<SidebarTemplate>
		<NotAuthorized v-if="!authorized" />
		<Column v-else class="gap-4">
			<Header title="Notes">
				<ValidGradesDialog v-if="selectedTeamId !== '' && selectedSprintId !== '' && canConfirmOwnTeamGrade && isGradesConfirmed && ssTeam?.id.toString() == selectedTeamId"
					@valid:individual-grades="forceRerender()" :selectedTeam="selectedTeamId"
					:selectedSprint="selectedSprintId">
					<Button variant="default">Valider les notes individuelles</Button>
				</ValidGradesDialog>

				<Select v-model="selectedSprintId" @update:modelValue="forceRerender()">
					<SelectTrigger class="w-[180px]">
						<SelectValue :placeholder="selectedSprint ? 'Sprint ' + selectedSprint.sprintOrder : 'Sélectionner le sprint'" />
					</SelectTrigger>
					<SelectContent>
						<SelectGroup>
							<SelectItem v-for="sprint in sprints" :key="sprint.id" :value="sprint.id.toString()"
								@click="forceRerender">Sprint {{ sprint.sprintOrder }}</SelectItem>
						</SelectGroup>
					</SelectContent>
				</Select>
				<Select v-model="selectedTeamId" @update:modelValue="forceRerender()">
					<SelectTrigger class="w-[180px]">
						<SelectValue :placeholder="selectedTeam ? selectedTeam.name : 'Sélectionner une équipe'" />
					</SelectTrigger>
					<SelectContent>
						<SelectGroup>
							<SelectItem v-for="team in teams" :key="team.id" :value="team.id.toString()"
								@click="forceRerender">{{ team.name }}</SelectItem>
						</SelectGroup>
					</SelectContent>
				</Select>

				<ExportGrades v-if="hasPermission('EXPORT_INDIVIDUAL_GRADES')">
					<Button variant="default">Exporter</Button>
				</ExportGrades>
			</Header>
			<Column v-if="selectedTeamId !== '' && selectedSprintId !== ''">
				<Grade v-if="authorized" :isGradesConfirmed="isGradesConfirmed" :teamId="selectedTeamId" :sprintId="selectedSprintId" />
				<NotAutorized v-else />
			</Column>
			<Column v-else class="items-center py-4 gap-2 border border-gray-300 border-dashed rounded-lg">
				<ListChecks class="size-12 stroke-1 text-dark-blue" />
				<p class="text-dark-blue text-sm">Vous n'avez pas sélectionné de sprint et/ou une équipe.</p>
			</Column>
		</Column>
	</SidebarTemplate>
</template>