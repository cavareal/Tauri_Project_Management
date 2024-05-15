<script setup lang="ts">
import { SidebarTemplate } from "@/components/templates"
import NotAuthorized from "@/components/organisms/errors/NotAuthorized.vue"
import NotAutorized from "../organisms/errors/NotAuthorized.vue"
import { computed, ref } from "vue"
import { hasPermission } from "@/services/user-service"
import { useQuery } from "@tanstack/vue-query"
import { getTeams } from "@/services/team-service"
import { getSprints } from "@/services/sprint-service"
import { Select, SelectContent, SelectGroup, SelectItem, SelectTrigger, SelectValue } from "@/components/ui/select"
import { Header } from "@/components/molecules/header"
import { Column } from "@/components/atoms/containers"
import { ListChecks } from "lucide-vue-next"
import Grade from "@/components/organisms/Grade/GradeTable.vue"
import { Button } from "@/components/ui/button"
import ValidGradesDialog from "@/components/organisms/Grade/ValidGradesDialog.vue"
import { getGradesConfirmation } from "@/services/grade-service"


const selectedTeam = ref("")
const selectedSprint = ref("")
const componentKey = ref(0)
const canViewOwnTeamGrade = hasPermission("VIEW_OWN_TEAM_GRADE")

const authorized = hasPermission("GRADES_PAGE")

const { data: teams } = useQuery({ queryKey: ["teams"], queryFn: getTeams })
const { data: sprints } = useQuery({ queryKey: ["sprints"], queryFn: getSprints })
const ratedSprints = computed(() => {
	return sprints.value?.filter(sprint => sprint.endType === "NORMAL_SPRINT" || sprint.endType === "FINAL_SPRINT") || []
})


const { data: isGradesConfirmed, refetch: refetchGradesConfirmation } = useQuery({ queryKey: ["grades-confirmation"], queryFn: async() => {
	if (selectedSprint.value != "" && selectedTeam.value != "") return
	return await getGradesConfirmation(selectedSprint.value, selectedTeam.value)

} })

const forceRerender = () => {
	componentKey.value += 1
}

</script>

<template>
	<SidebarTemplate>
		<NotAuthorized v-if="!authorized" />
		<Column v-else class="gap-4">
			<Header title="Notes">

				<ValidGradesDialog v-if="selectedTeam !== '' && selectedSprint !== '' && isGradesConfirmed" @valid:individual-grades="forceRerender()" :selectedTeam="selectedTeam" :selectedSprint="selectedSprint" >
					<Button variant="default">Valider les notes individuelles</Button>
				</ValidGradesDialog>

				<Select v-model="selectedSprint">
					<SelectTrigger class="w-[180px]">
						<SelectValue placeholder="Sprint par défaut" />
					</SelectTrigger>
					<SelectContent>
						<SelectGroup>
							<SelectItem v-for="sprint in ratedSprints" :key="sprint.id" :value="sprint.id"
								@click="forceRerender">{{ sprint.id }}</SelectItem>
						</SelectGroup>
					</SelectContent>
				</Select>
				<Select v-model="selectedTeam">
					<SelectTrigger class="w-[180px]">
						<SelectValue placeholder="Selectionner l'équipe" />
					</SelectTrigger>
					<SelectContent>
						<SelectGroup>
							<SelectItem v-for="team in teams" :key="team.id" :value="team.id" @click="forceRerender">{{
								team.name }}</SelectItem>
						</SelectGroup>
					</SelectContent>
				</Select>
			</Header>
			<Column v-if="selectedTeam !== '' && selectedSprint !== ''">
          <Grade v-if="authorized" :teamId="selectedTeam" :sprintId="selectedSprint" :key="componentKey"/>
        <NotAutorized v-else/>
			</Column>
			<Column v-else class="items-center py-4 gap-2 border border-gray-300 border-dashed rounded-lg">
				<ListChecks class="size-12 stroke-1 text-dark-blue" />
				<p class="text-dark-blue text-sm">Vous n'avez pas sélectionné de sprint et/ou une équipe à évaluer.</p>
			</Column>
		</Column>
	</SidebarTemplate>
</template>