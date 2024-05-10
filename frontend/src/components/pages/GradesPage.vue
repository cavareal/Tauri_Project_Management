<script setup lang="ts">
import { SidebarTemplate } from "@/components/templates"
import Tab from "@/components/molecules/tab/Tab.vue"
import Tabs from "@/components/molecules/tab/Tabs.vue"
import { Cookies } from "@/utils/cookie"
import NotAuthorized from "@/components/organisms/errors/NotAuthorized.vue"
import NotAutorized from "../organisms/errors/NotAuthorized.vue"
import TMOwnGradeView from "@/components/organisms/Grade/TMOwnGradeView.vue"
import { ref } from "vue"
import { hasPermission } from "@/services/user-service"
import { useQuery } from "@tanstack/vue-query"
import { getTeams } from "@/services/team-service"
import { getSprints } from "@/services/sprint-service"
import { Select, SelectContent, SelectGroup, SelectItem, SelectTrigger, SelectValue } from "@/components/ui/select"
import { Header } from "@/components/molecules/header"
import { Text } from "@/components/atoms/texts"
import Rating from "@/components/organisms/Rate/Rating.vue"
import { Column } from "@/components/atoms/containers"
import { ListChecks } from "lucide-vue-next"
import Grade from "@/components/organisms/Grade/Grade.vue"

const selectedTeam = ref("")
const selectedSprint = ref("")
const componentKey = ref(0)

const authorized = hasPermission("GRADES_PAGE")

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
			<Header title="Notes">
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
				<Grade v-if="authorized" :teamId="selectedTeam" :sprintId="selectedSprint" :key="componentKey"/>
				<NotAutorized v-else/>
			</div>
			<Column v-else class="items-center justify-center p-6 gap-2 rounded-lg border transition-all cursor-pointer border-dashed bg-slate-50">
				<ListChecks class="w-16 h-16 stroke-[1.2]" />
				<Text>Vous n'avez pas sélectionné de sprint et/ou une équipe</Text>
			</Column>
		</div>
	</SidebarTemplate>
</template>