<script setup lang="ts">

import { ref } from "vue"
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
import { getTeams } from "@/services/team/team.service"
import { getSprints } from "@/services/sprint/sprint.service"
import { Column } from "@/components/atoms/containers"
import { ListChecks } from "lucide-vue-next"
import { hasPermission } from "@/services/user/user.service"
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

</script>

<template>
	<SidebarTemplate>
		<NotAuthorized v-if="!authorized" />
		<Column v-else class="gap-4">
			<Header title="Évaluations">
				<Select v-model="selectedSprint">
					<SelectTrigger class="w-[180px]">
						<SelectValue placeholder="Sprint par défaut" />
					</SelectTrigger>
					<SelectContent>
						<SelectGroup>
							<SelectItem v-for="sprint in sprints" :key="sprint.id" :value="sprint.id.toString()" @click="forceRerender">Sprint {{sprint.sprintOrder}}</SelectItem>
						</SelectGroup>
					</SelectContent>
				</Select>
				<Select v-model="selectedTeam">
					<SelectTrigger class="w-[180px]">
						<SelectValue placeholder="Selectionner l'équipe" />
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