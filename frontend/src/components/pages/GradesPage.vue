<script setup lang="ts">
import { SidebarTemplate } from "@/components/templates"
import NotAuthorized from "@/components/organisms/errors/NotAuthorized.vue"
import NotAutorized from "../organisms/errors/NotAuthorized.vue"
import { computed, ref } from "vue"
import { hasPermission } from "@/services/user"
import { useQuery } from "@tanstack/vue-query"
import { getTeams } from "@/services/team"
import { getSprints } from "@/services/sprint"
import { Select, SelectContent, SelectGroup, SelectItem, SelectTrigger, SelectValue } from "@/components/ui/select"
import { Header } from "@/components/molecules/header"
import { Column } from "@/components/atoms/containers"
import { ListChecks } from "lucide-vue-next"
import Grade from "@/components/organisms/Grade/GradeTable.vue"
import { Button } from "@/components/ui/button"
import ValidGradesDialog from "@/components/organisms/Grade/ValidGradesDialog.vue"
import { getGradesConfirmation } from "@/services/grade"
import ExportGrades from "../organisms/Grade/ExportGrades.vue"

const selectedTeam = ref("")
const selectedSprint = ref("")
const canViewOwnTeamGrade = hasPermission("VIEW_OWN_TEAM_GRADE")

const authorized = hasPermission("GRADES_PAGE")

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

const { data: teams } = useQuery({ queryKey: ["teams"], queryFn: getTeams })
const { data: sprints } = useQuery({
	queryKey: ["sprints"], queryFn: async() => {
		const sprints = await getSprints()
		return sprints.filter(sprint => sprint.endType === "NORMAL_SPRINT" || sprint.endType === "FINAL_SPRINT")
	}
})


const { data: isGradesConfirmed, refetch: refetchGradesConfirmation } = useQuery({
	queryKey: ["grades-confirmation", selectedSprint.value, selectedTeam.value],
	queryFn: async() => {
		if (selectedSprint.value === "" || selectedTeam.value === "") return false
		return await getGradesConfirmation(parseInt(selectedSprint.value), parseInt(selectedTeam.value))
	}
})

function forceRerender() {
	refetchGradesConfirmation()
}

</script>

<template>
	<SidebarTemplate>
		<NotAuthorized v-if="!authorized" />
		<Column v-else class="gap-4">
			<Header title="Notes">

				<ValidGradesDialog v-if="selectedTeam !== '' && selectedSprint !== '' && isGradesConfirmed"
					@valid:individual-grades="forceRerender()" :selectedTeam="selectedTeam"
					:selectedSprint="selectedSprint">
					<Button variant="default">Valider les notes individuelles</Button>
				</ValidGradesDialog>

				<Select v-model="selectedSprint" @update:modelValue="forceRerender()">
					<SelectTrigger class="w-[180px]">
						<SelectValue :placeholder="currentSprint ? 'Sprint ' + currentSprint.sprintOrder : 'Sélectionner le sprint'" />
					</SelectTrigger>
					<SelectContent>
						<SelectGroup>
							<SelectItem v-for="sprint in sprints" :key="sprint.id" :value="sprint.id.toString()"
								@click="forceRerender">Sprint {{ sprint.sprintOrder }}</SelectItem>
						</SelectGroup>
					</SelectContent>
				</Select>
				<Select v-model="selectedTeam" @update:modelValue="forceRerender()">
					<SelectTrigger class="w-[180px]">
						<SelectValue placeholder="Sélectionner l'équipe" />
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
			<Column v-if="selectedTeam !== '' && selectedSprint !== ''">
				<Grade v-if="authorized" :teamId="selectedTeam" :sprintId="selectedSprint" />
				<NotAutorized v-else />
			</Column>
			<Column v-else class="items-center py-4 gap-2 border border-gray-300 border-dashed rounded-lg">
				<ListChecks class="size-12 stroke-1 text-dark-blue" />
				<p class="text-dark-blue text-sm">Vous n'avez pas sélectionné de sprint et/ou une équipe.</p>
			</Column>
		</Column>
	</SidebarTemplate>
</template>