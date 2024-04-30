<script setup lang="ts">

import { SidebarTemplate } from "@/components/templates"
import {
	RedirectImportStudents, GenerateTeams, PrepublishDialog, DeleteTeamsDialog, TeamAccordion, TeamsNotCreated
} from "@/components/organisms/teams"
import { getCookie } from "@/utils/cookie"
import { Button } from "@/components/ui/button"
import { getQuantityOfStudents } from "@/services/student-service"
import { NotAuthorized } from "@/components/organisms/errors"
import { getTeams } from "@/services/team-service"
import { getProjectById } from "@/services/project-service"
import { Header } from "@/components/molecules/header"
import type { RoleType } from "@/types/role"
import { computed } from "vue"
import { PageSkeleton } from "@/components/atoms/skeletons"
import { useQuery } from "@tanstack/vue-query"

const token = getCookie("token")
const role = getCookie<RoleType>("role")
const currentProjectId = getCookie("currentProject")

const { data: currentPhase, refetch: refetchCurrentPhase } = useQuery({
	queryKey: ["project"], queryFn: async() => (await (getProjectById(currentProjectId))).phase
})
const { data: nbStudents } = useQuery({ queryKey: ["nbStudents"], queryFn: getQuantityOfStudents })
const { data: nbTeams, refetch: refetchTeams } = useQuery({ queryKey: ["nb-teams"], queryFn: async() => (await getTeams(currentProjectId)).length })

const displayButtons = computed(() => role === "PROJECT_LEADER" && nbStudents.value && nbStudents.value > 0
	&& nbTeams.value && nbTeams.value > 0 && currentPhase.value && currentPhase.value === "COMPOSING")

const generateTeams = computed(() => role === "PROJECT_LEADER" && currentPhase.value === "COMPOSING")
const displayTeams = computed(() => (role === "PROJECT_LEADER" || (role === "SUPERVISING_STAFF" && currentPhase.value !== "COMPOSING")
	|| role === "OPTION_LEADER" || (role === "OPTION_STUDENT" && currentPhase.value !== "COMPOSING")))

</script>

<template>
	<SidebarTemplate>
		<Header title="Équipes">
			<DeleteTeamsDialog v-if="displayButtons" @delete:teams="refetchTeams">
				<Button variant="outline">Supprimer les équipes</Button>
			</DeleteTeamsDialog>
			<PrepublishDialog v-if="displayButtons" @prepublish:teams="refetchCurrentPhase">
				<Button variant="default">Prépublier</Button>
			</PrepublishDialog>
		</Header>

		<NotAuthorized v-if="!token || !role" />
		<PageSkeleton v-else-if="currentPhase === undefined || nbStudents === undefined || nbTeams === undefined" />
		<RedirectImportStudents v-else-if="generateTeams && nbStudents === 0" />
		<GenerateTeams v-else-if="generateTeams && nbStudents > 0 && nbTeams === 0" @generate:teams="refetchTeams" :nb-students="nbStudents" />
		<TeamAccordion v-else-if="nbTeams > 0 && displayTeams" :phase="currentPhase" />
		<TeamsNotCreated v-else-if="(role === 'SUPERVISING_STAFF' || role === 'OPTION_LEADER') && currentPhase === 'COMPOSING'" />
		<NotAuthorized v-else />
	</SidebarTemplate>
</template>