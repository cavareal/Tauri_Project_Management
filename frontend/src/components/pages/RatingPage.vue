<script setup lang="ts">

import { computed, ref, watch } from "vue"
import { SidebarTemplate } from "@/components/templates"
import NotAutorized from "@/components/organisms/errors/NotAuthorized.vue"
import { Header } from "@/components/molecules/header"
import {
	Select,
	SelectContent,
	SelectGroup,
	SelectItem,
	SelectTrigger,
	SelectValue
} from "@/components/ui/select"
import { useQuery } from "@tanstack/vue-query"
import { getTeams } from "@/services/team-service"
import { getSprints } from "@/services/sprint-service"
import { Column } from "@/components/atoms/containers"
import { ListChecks } from "lucide-vue-next"
import { hasPermission } from "@/services/user-service"
import { NotAuthorized } from "@/components/organisms/errors"
import Rating from "@/components/organisms/Rate/Rating.vue"
import type { Sprint } from "@/types/sprint"
import type { Team } from "@/types/team"

// Get the current date
const currentDate = new Date()
const authorized = hasPermission("RATING_PAGE")
const componentKey = ref(0)

const forceRerender = () => {
	componentKey.value += 1
}

const selectedTeamId = ref("")
const selectedSprintId = ref("")
const selectedTeam = ref(null as Team | null)
const selectedSprint = ref(null as Sprint | null)
const currentSprint = ref(null as Sprint | null)

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
	if (!sessionStorage.getItem("ratingPageSelectedTeamId") || !teamsList.some(team => team.id.toString() === sessionStorage.getItem("ratingPageSelectedTeamId"))) {
		sessionStorage.setItem("ratingPageSelectedTeamId", "")
	}
	selectedTeamId.value = sessionStorage.getItem("ratingPageSelectedTeamId")
	selectedTeam.value = teams.value.find(team => team.id.toString() === selectedTeamId.value)
}

function initializeSelectedSprintId(sprintsList: Sprint[]) {
	if (!sessionStorage.getItem("ratingPageSelectedSprintId") || !sprintsList.some(sprint => sprint.id.toString() === sessionStorage.getItem("ratingPageSelectedSprintId"))) {
		if (!currentSprint.value || (currentSprint.value.endType === "UNGRADED_SPRINT") && !previousSprint.value) {
			sessionStorage.setItem("ratingPageSelectedSprintId", "")
		} else if (!previousSprint.value || currentSprint.value.endType === "FINAL_SPRINT") {
			sessionStorage.setItem("ratingPageSelectedSprintId", currentSprint.value.id.toString())
		} else {
			sessionStorage.setItem("ratingPageSelectedSprintId", previousSprint.value.id.toString())
		}
	}
	selectedSprintId.value = sessionStorage.getItem("ratingPageSelectedSprintId")
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
		sessionStorage.setItem("ratingPageSelectedTeamId", newValue)
		selectedTeam.value = teams.value.find(team => team.id.toString() === newValue)
	}
})

watch(selectedSprintId, newValue => {
	if (newValue) {
		sessionStorage.setItem("ratingPageSelectedSprintId", newValue)
		selectedSprint.value = sprints.value.find(sprint => sprint.id.toString() === newValue)
	}
})

console.log(selectedTeamId.value)
console.log(selectedSprintId.value)

</script>

<template>
	<SidebarTemplate>
		<NotAuthorized v-if="!authorized" />
		<Column v-else class="gap-4">
			<Header title="Évaluations">
				<Select v-model="selectedSprintId">
					<SelectTrigger class="w-[180px]">
						<SelectValue :placeholder="selectedSprint ? 'Sprint ' + selectedSprint.sprintOrder : 'Sélectionner le sprint'" />
					</SelectTrigger>
					<SelectContent>
						<SelectGroup>
							<SelectItem v-for="sprint in sprints" :key="sprint.id" :value="sprint.id.toString()" @click="forceRerender">Sprint {{sprint.sprintOrder}}</SelectItem>
						</SelectGroup>
					</SelectContent>
				</Select>
				<Select v-model="selectedTeamId">
					<SelectTrigger class="w-[180px]">
						<SelectValue :placeholder="selectedTeam ? selectedTeam.name : 'Sélectionner une équipe'" />
					</SelectTrigger>
					<SelectContent>
						<SelectGroup>
							<SelectItem v-for="team in teams" :key="team.id" :value="team.id.toString()" @click="forceRerender">{{ team.name }}</SelectItem>
						</SelectGroup>
					</SelectContent>
				</Select>
			</Header>
			<Column v-if="selectedTeamId !== '' && selectedSprintId !== ''" class="gap-4">
				<Rating v-if="authorized" :teamId="selectedTeamId" :sprintId="selectedSprintId" :key="componentKey"/>
				<NotAutorized v-else/>
			</Column>
            <Column v-else class="items-center py-4 gap-2 border border-gray-300 border-dashed rounded-lg">
				<ListChecks class="size-12 stroke-1 text-dark-blue" />
                <p class="text-dark-blue text-sm">Vous n'avez pas sélectionné de sprint et/ou une équipe à évaluer.</p>
            </Column>
		</Column>
	</SidebarTemplate>
</template>