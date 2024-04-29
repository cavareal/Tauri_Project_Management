<script setup lang="ts">
import { computed, ref } from "vue"
import { SidebarTemplate } from "@/components/templates"
import Tab from "@/components/molecules/tab/Tab.vue"
import Tabs from "@/components/molecules/tab/Tabs.vue"
import NotAutorized from "@/components/organisms/errors/NotAuthorized.vue"
import TMRateView from "@/components/organisms/Rate/TMRateView.vue"
import { getCookie } from "@/utils/cookie"
import SSTCRateView from "@/components/organisms/Rate/SSTCRateView.vue"

const token = getCookie("token")
const role = getCookie("role")
const project = getCookie("currentProject")
let nbSprints = ref("3")
const teamsName = ref<string[]>([])


const request = {
	method: "GET",
	headers: {
		"Content-Type": "application/json",
		Authorization: token || "null"
	}
}

const fetchTeamNames = async() => {
	try {
		const response = await fetch(import.meta.env.VITE_TAURI_API_URL + "teams/names", request)
		if (!response.ok) {
			throw new Error(`HTTP error! status: ${response.status}`)
		}
		// eslint-disable-next-line @typescript-eslint/no-unsafe-assignment,max-len
		teamsName.value = await response.json()
	} catch (error) {
		console.error(error)
	}
}
void fetchTeamNames()

const parsedTeams = computed(() => {
	teamsName.value.map(team => team.replace(/[\[\]"]+/g, "").split(", "))
	return teamsName.value.flat()
})

/* GET number of sprints of this project */
const requestOptionsStudents = {
	method: "GET",
	headers: {
		"Content-Type": "application/json",
		Authorization: token || "null"
	}
}
const fetchNumberSprints = async() => {
	try {
		const response = await fetch(import.meta.env.VITE_TAURI_API_URL + "projects/sprints/" + project, requestOptionsStudents)
		if (!response.ok) {
			throw new Error(`HTTP error! status: ${response.status}`)
		}
		nbSprints.value = await response.text()
	} catch (error) {
		console.error(error)
	}
}
void fetchNumberSprints()

</script>

<template>
	<SidebarTemplate>
		<h1 class="text-3xl font-title-bold">Evaluation</h1>
		<div class="tabs-example">
			<div class="example example-1">
				<Tabs>
					<template v-for="(sprint, index) in Array(parseInt(nbSprints))" :key="index">
						<Tab :title="`Sprint ${index + 1}`">
							<NotAutorized v-if="!token || !role"/>
							<TMRateView v-else-if="role === 'TEAM_MEMBER'" :listTeam="parsedTeams"/>
							<SSTCRateView v-else-if="role === 'SUPERVISING_STAFF' || role==='TECHNICAL_COACH'" :listTeam="parsedTeams"/>
							<NotAutorized v-else/>
						</Tab>
					</template>
				</Tabs>
			</div>
		</div>
	</SidebarTemplate>
</template>