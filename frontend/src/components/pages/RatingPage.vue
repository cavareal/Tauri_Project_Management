<script setup lang="ts">
import { ref } from "vue"
import { SidebarTemplate } from "@/components/templates"
import NotAutorized from "@/components/organisms/errors/NotAuthorized.vue"
import TMRateView from "@/components/organisms/Rate/TMRateView.vue"
import { Cookies } from "@/utils/cookie"
import SSTCRateView from "@/components/organisms/Rate/SSTCRateView.vue"
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

const token = Cookies.getToken()
const role = Cookies.getRole()
const currentProject = Cookies.getProjectId()
let nbSprints = ref("3")
const selectedTeam = ref("")
const selectedSprint = ref("")
const componentKey = ref(0)


const { data: teams, isLoading, error } = useQuery({ queryKey: ["teams"], queryFn: getTeams })
const { data: sprints } = useQuery({ queryKey: ["sprints"], queryFn: getSprints })

const forceRerender = () => {
	componentKey.value += 1
}

</script>

<template>
	<SidebarTemplate>
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
		<div v-if="selectedTeam !== ''">
			<NotAutorized v-if="!token || !role" />
			<TMRateView v-else-if="role === 'TEAM_MEMBER'" :teamId="selectedTeam" :sprintId="selectedSprint" :key="componentKey"/>
			<SSTCRateView v-else-if="role === 'SUPERVISING_STAFF' || role==='TECHNICAL_COACH'"/>
			<NotAutorized v-else/>
		</div>
	</SidebarTemplate>
</template>