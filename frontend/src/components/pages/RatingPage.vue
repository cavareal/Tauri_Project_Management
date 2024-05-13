<script setup lang="ts">
import { ref } from "vue"
import { SidebarTemplate } from "@/components/templates"
import NotAutorized from "@/components/organisms/errors/NotAuthorized.vue"
import { Cookies } from "@/utils/cookie"
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
import { getAllSprints } from "@/services/sprint-service"
import { Column } from "@/components/atoms/containers"
import { ListChecks } from "lucide-vue-next"
import { Text } from "@/components/atoms/texts"
import { hasPermission } from "@/services/user-service"
import { NotAuthorized } from "@/components/organisms/errors"
import Rating from "@/components/organisms/Rate/Rating.vue"


const selectedTeam = ref("")
const selectedSprint = ref("")
const componentKey = ref(0)

const authorized = hasPermission("RATING_PAGE")

const { data: teams, isLoading, error } = useQuery({ queryKey: ["teams"], queryFn: getTeams })
const { data: sprints } = useQuery({ queryKey: ["sprints"], queryFn: getSprints })

const forceRerender = () => {
	componentKey.value += 1
}

</script>

<template>
	<SidebarTemplate>
		<NotAuthorized v-if="!authorized" />
		<div v-else>
			<Header title="Evaluation">
				<Select v-model="selectedSprint">
					<SelectTrigger class="w-[180px]">
						<SelectValue placeholder="Sprint par défaut" />
					</SelectTrigger>
					<SelectContent>
						<SelectGroup>
							<SelectItem v-for="sprint in sprints" :key="sprint.id" :value="sprint.id" @click="forceRerender">{{sprint.id}}</SelectItem>
						</SelectGroup>
					</SelectContent>
				</Select>
				<Select v-model="selectedTeam">
					<SelectTrigger class="w-[180px]">
						<SelectValue placeholder="Selectionner l'équipe" />
					</SelectTrigger>
					<SelectContent>
						<SelectGroup>
							<SelectItem v-for="team in teams" :key="team.id" :value="team.id" @click="forceRerender">{{ team.name }}</SelectItem>
						</SelectGroup>
					</SelectContent>
				</Select>
			</Header>
			<div v-if="selectedTeam !== '' && selectedSprint !== ''">
				<Rating v-if="authorized" :teamId="selectedTeam" :sprintId="selectedSprint" :key="componentKey"/>
				<NotAutorized v-else/>
			</div>
			<Column v-else class="items-center justify-center p-6 gap-2 rounded-lg border transition-all cursor-pointer border-dashed bg-slate-50">
				<ListChecks class="w-16 h-16 stroke-[1.2]" />
				<Text>Vous n'avez pas sélectionné de sprint et/ou une équipe à évaluer</Text>
			</Column>
		</div>
	</SidebarTemplate>
</template>