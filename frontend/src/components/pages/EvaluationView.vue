<script setup lang="ts">
import { computed, defineComponent, ref } from "vue"
import PageTemplate from "@/components/organisms/template/PageTemplate.vue"
import Tab from "@/components/molecules/tab/Tab.vue"
import Tabs from "@/components/molecules/tab/Tabs.vue"
import NotAutorized from "@/components/organisms/Teams/NotAuthorized.vue"
import TMRateView from "@/components/organisms/Rate/TMRateView.vue"
import getCookie from "@/utils/cookiesUtils"
import SSTCRateView from "@/components/organisms/Rate/SSTCRateView.vue"

const token = getCookie("token")
const role = getCookie("role")
let nbSprints = ref("3")
const sprintList = ref([1, 2, 3])
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
		const response = await fetch(import.meta.env.VITE_TAURI_API_URL + "projects/sprints-number", requestOptionsStudents)
		if (!response.ok) {
			throw new Error(`HTTP error! status: ${response.status}`)
		}
		const data = await response.text()
		nbSprints.value = data
	} catch (error) {
		console.error(error)
	}
}
void fetchNumberSprints()

defineComponent({
	// eslint-disable-next-line @typescript-eslint/no-unsafe-assignment
	components: { PageTemplate, Tabs, Tab }
})

console.log(parsedTeams)
</script>

<template>
	<PageTemplate>
		<h1 class="text-3xl font-title-bold">Evaluation</h1>
		<div class="tabs-example">
			<div class="example example-1">
				<Tabs>
					<template v-for="(sprint, index) in Array(parseInt(nbSprints))" :key="index">
						<Tab :title="`Sprint ${index + 1}`">
							<NotAutorized v-if="!token || !role"/>
							<TMRateView v-else-if="role === 'TM'" :listTeam="parsedTeams"/>
							<SSTCRateView v-else-if="role === 'SS' || role === 'TC'" :listTeam="parsedTeams"/>
							<NotAutorized v-else/>
						</Tab>
					</template>
				</Tabs>
			</div>
		</div>
	</PageTemplate>
</template>