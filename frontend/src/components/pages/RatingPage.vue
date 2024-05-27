<script setup lang="ts">

import { computed, ref } from "vue"
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

const selectedTeam = ref("")
const selectedSprint = ref("")
const componentKey = ref(0)

const authorized = hasPermission("RATING_PAGE")

const { data: teams } = useQuery({ queryKey: ["teams"], queryFn: getTeams })
const { data: sprints } = useQuery({ queryKey: ["sprints"], queryFn: async() => {
	const sprints = await getSprints()
	return sprints.filter(sprint => sprint.endType === "NORMAL_SPRINT" || sprint.endType === "FINAL_SPRINT")
} })

const forceRerender = () => {
	componentKey.value += 1
}

// Get the current date
const currentDate = new Date()

// Determine the current sprint
const currentSprint = computed(() => {
	const sprint = sprints.value?.find(sprint => {
		const startDate = new Date(sprint.startDate)
		startDate.setHours(0, 0, 0, 0) // Set time to 00:00:00
		const endDate = new Date(sprint.endDate)
		endDate.setHours(23, 59, 59, 999) // Set time to end of the day
		return startDate <= currentDate && currentDate <= endDate
	})
	if (sprint) selectedSprint.value = sprint.id.toString()
	return sprint || null
})

</script>

<template>
	<SidebarTemplate>
		<NotAuthorized v-if="!authorized" />
		<Column v-else class="gap-4">
			<Header title="Évaluations">
				<Select v-model="selectedSprint">
					<SelectTrigger class="w-[180px]">
						<SelectValue :placeholder="currentSprint ? 'Sprint ' + currentSprint.sprintOrder : 'Sélectionner le sprint'" />
					</SelectTrigger>
					<SelectContent>
						<SelectGroup>
							<SelectItem v-for="sprint in sprints" :key="sprint.id" :value="sprint.id.toString()" @click="forceRerender">Sprint {{sprint.sprintOrder}}</SelectItem>
						</SelectGroup>
					</SelectContent>
				</Select>
				<Select v-model="selectedTeam">
					<SelectTrigger class="w-[180px]">
						<SelectValue placeholder="Sélectionner l'équipe" />
					</SelectTrigger>
					<SelectContent>
						<SelectGroup>
							<SelectItem v-for="team in teams" :key="team.id" :value="team.id.toString()" @click="forceRerender">{{ team.name }}</SelectItem>
						</SelectGroup>
					</SelectContent>
				</Select>
			</Header>
			<Column v-if="selectedTeam !== '' && selectedSprint !== ''" class="gap-4">
				<Rating v-if="authorized" :teamId="selectedTeam" :sprintId="selectedSprint" :key="componentKey"/>
				<NotAutorized v-else/>
			</Column>
            <Column v-else class="items-center py-4 gap-2 border border-gray-300 border-dashed rounded-lg">
				<ListChecks class="size-12 stroke-1 text-dark-blue" />
                <p class="text-dark-blue text-sm">Vous n'avez pas sélectionné de sprint et/ou une équipe à évaluer.</p>
            </Column>
		</Column>
	</SidebarTemplate>
</template>