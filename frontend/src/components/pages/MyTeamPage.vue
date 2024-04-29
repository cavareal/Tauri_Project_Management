<script setup lang="ts">
import { getCookie } from "@/utils/cookie"
import { onMounted, ref, watch } from "vue"
import type { ProjectPhase } from "@/types/project"
import type { Team } from "@/types/team"
import { SidebarTemplate } from "@/components/templates"
import MyTeamCreated from "@/components/organisms/my-team/MyTeamCreated.vue"
import { NotAuthorized, NotFound } from "@/components/organisms/errors"
import { getTeamByLeaderId } from "@/services/team-service"
import { getCurrentPhase } from "@/services/project-service"
import { Header } from "@/components/molecules/header"

const token = getCookie("token")
const role = getCookie("role")
const idProject = getCookie("currentProject")
const currentUser = getCookie("user")
const currentPhase = ref<ProjectPhase>("COMPOSING")
const team = ref<Team>()

watch(() => { }, async() => {
	currentPhase.value = await getCurrentPhase(idProject)
}, { immediate: true })

onMounted(async() => {
	const data = await getTeamByLeaderId(currentUser, idProject)
	team.value = data
})
</script>

<template>
	<SidebarTemplate>
		<Header title="Mon équipe" />
		<NotAuthorized v-if="!token || !role" />
		<MyTeamCreated v-else-if="role === 'SUPERVISING_STAFF' && currentPhase !== 'COMPOSING' && team" :team="team"
			:phase="currentPhase" />
		<NotFound v-else />
	</SidebarTemplate>
</template>

<style scoped></style>