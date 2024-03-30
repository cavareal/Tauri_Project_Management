<script setup lang="ts">
import { ref } from 'vue';
import PageTemplate from "@/components/organisms/PageTemplate.vue"
import TeamsCreated from "@/components/organisms/Teams/TeamsCreated.vue"
import TeamsNotsCreated from "@/components/organisms/Teams/TeamsNotsCreated.vue"
import StudentsNotImported from "@/components/organisms/Teams/StudentsNotImported.vue"
import NotAutorized from "@/components/organisms/Teams/NotAuthorized.vue"
import GenerateTeams from '@/components/organisms/Teams/GenerateTeams.vue'
import getCookie from "@/utils/cookiesUtils"

const token = getCookie("token");
const role = getCookie("role");
const currentPhase = ref('');

const requestOptions = {
	method: "GET",
	headers: {
		"Content-Type": "application/json",
		Authorization: token || "null"
	},
}

const fetchCurrentPhase = async () => {
	try {
		const response = await fetch(import.meta.env.VITE_TAURI_API_URL + "projects/current-phase", requestOptions)
		if (!response.ok) {
			throw new Error(`HTTP error! status: ${response.status}`)
		}
		const data = await response.text();
		currentPhase.value = data;
	} catch (error) {
		console.error(error);
	}
}

fetchCurrentPhase();
</script>

<template>
	<PageTemplate>
		<h1 class="text-3xl font-title-bold">Equipes</h1>
		<NotAutorized v-if="!token || !role" />
		<StudentsNotImported v-else-if="role === 'PL' && currentPhase === ''" />
		<GenerateTeams v-else-if="role === 'PL' && currentPhase === 'COMPOSING'" />
		<TeamsCreated v-else-if="(role === 'PL' || role === 'SS' || role === 'OL') && currentPhase === 'PREPUBLISHED'" />
		<TeamsNotsCreated v-else-if="(role === 'SS' || role === 'OL') && currentPhase === 'COMPOSING'" />
		<NotAutorized v-else />
	</PageTemplate>
</template>
