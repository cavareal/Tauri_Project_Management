<script setup lang="ts">
import { computed, defineComponent, ref } from "vue"
import PageTemplate from "@/components/organisms/template/PageTemplate.vue"
import Tab from "@/components/molecules/tab/Tab.vue"
import Tabs from "@/components/molecules/tab/Tabs.vue"
import NotAutorized from "@/components/organisms/Teams/NotAuthorized.vue"
import TMRateView from "@/components/organisms/Rate/TMRateView.vue"
import getCookie from "@/utils/cookiesUtils"
import SSRateView from "@/components/organisms/Rate/SSRateView.vue"

const token = getCookie("token")
const role = getCookie("role")
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

defineComponent({
	// eslint-disable-next-line @typescript-eslint/no-unsafe-assignment
	components: { PageTemplate, Tabs, Tab },
	data: () => {
		return { dynamicTabs: [1, 2, 3] }
	}
})

console.log(parsedTeams)
</script>

<template>
	<PageTemplate>
		<h1 class="text-3xl font-title-bold">Evaluation</h1>
		<div class="tabs-example">
			<div class="example example-1">
				<Tabs>
					<template v-for="(sprint, index) in sprintList" :key="index">
						<Tab :title="`Sprint ${index + 1}`">
							<NotAutorized v-if="!token || !role"/>
							<TMRateView v-else-if="role === 'TM'" :listTeam="parsedTeams"/>
							<SSRateView v-else-if="role === 'SS'" :listTeam="parsedTeams"/>
							<NotAutorized v-else/>
						</Tab>
					</template>
				</Tabs>
			</div>
		</div>
	</PageTemplate>
</template>