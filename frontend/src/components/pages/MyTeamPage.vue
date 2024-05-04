<script setup lang="ts">
import { getCookie } from "@/utils/cookie"
import { computed, onMounted, ref } from "vue"
import type { Team } from "@/types/team"
import { SidebarTemplate } from "@/components/templates"
import { NotAuthorized, NotFound } from "@/components/organisms/errors"
import { getTeamByUserId } from "@/services/team-service"
import { getProjectById } from "@/services/project-service"
import { Header } from "@/components/molecules/header"
import { useQuery } from "@tanstack/vue-query"
import MyTeamAccordion from "@/components/organisms/my-team/MyTeamAccordion.vue"

const token = getCookie("token")
const role = getCookie("role")
const currentProjectId = getCookie("currentProject")
const currentUser = getCookie("user")
const team = ref<Team>()


const { data: currentPhase, refetch: refetchCurrentPhase } = useQuery({
	queryKey: ["project"], queryFn: async() => (await (getProjectById(currentProjectId))).phase
})

const displayTeam = computed(() => (role === "SUPERVISING_STAFF" || role === "OPTION_STUDENT")
    && currentPhase.value !== "COMPOSING")

onMounted(async() => {
	team.value = await getTeamByUserId(currentUser, currentProjectId)
})
</script>

<template>
	<SidebarTemplate>
		<Header title="Mon équipe" />
		<NotAuthorized v-if="!token || !role" />
    <MyTeamAccordion v-else-if="displayTeam && team"
                     :phase="currentPhase"
                     :team="team"/>
		<NotFound v-else />
	</SidebarTemplate>
</template>

<style scoped></style>