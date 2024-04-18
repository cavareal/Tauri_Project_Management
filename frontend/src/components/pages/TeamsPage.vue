<script setup lang="ts">

import { SidebarTemplate } from "@/components/templates"
import TeamsCreated from "@/components/organisms/teams/TeamsCreated.vue"
import TeamsNotsCreated from "@/components/organisms/teams/TeamsNotsCreated.vue"
import { StudentsNotImported, PrepublishDialog, DeleteTeamsDialog } from "@/components/organisms/teams"
import GenerateTeams from "@/components/organisms/teams/GenerateTeams.vue"
import { getCookie } from "@/utils/cookie"
import { Button } from "@/components/ui/button"
import { getQuantityOfStudents } from "@/services/student-service"
import { NotAuthorized } from "@/components/organisms/errors"
import { getTeams } from "@/services/team-service"
import { getCurrentPhase } from "@/services/project-service"
import { Header } from "../molecules/header"
import { useQuery } from "@/utils/api"
import type { RoleType } from "@/types/role"
import { computed } from "vue"

const token = getCookie("token")
const role = getCookie<RoleType>("role")

const { data: currentPhase } = useQuery(getCurrentPhase)
const { data: nbStudents } = useQuery(getQuantityOfStudents)
const { data: teams } = useQuery(getTeams)

const displayButtons = computed(() => role === "PROJECT_LEADER" && nbStudents.value && nbStudents.value > 0
	&& teams.value && teams.value.length > 0 && currentPhase.value && currentPhase.value === "COMPOSING")

</script>

<template>
	<SidebarTemplate>
		<Header title="Équipes">
			<DeleteTeamsDialog v-if="displayButtons">
				<Button variant="outline">Supprimer les équipes</Button>
			</DeleteTeamsDialog>
			<PrepublishDialog v-if="displayButtons">
				<Button variant="default">Prépublier</Button>
			</PrepublishDialog>
		</Header>

		<NotAuthorized v-if="!token || !role" />
		<StudentsNotImported v-else-if="role === 'PROJECT_LEADER' && currentPhase === 'COMPOSING' && nbStudents === 0" />
		<GenerateTeams v-else-if="role === 'PROJECT_LEADER' && currentPhase === 'COMPOSING' && nbStudents > 0 && teams.length === 0" />
		<!-- eslint-disable-next-line max-len -->
		<TeamsCreated v-else-if="(role === 'PROJECT_LEADER' || (role === 'SUPERVISING_STAFF' && currentPhase !=='COMPOSING') || role === 'OPTION_LEADER' || (role === 'OPTION_STUDENT' && currentPhase !== 'COMPOSING')) && teams.length > 0" :phase="currentPhase" />
		<TeamsNotsCreated v-else-if="(role === 'SUPERVISING_STAFF' || role === 'OPTION_LEADER') && currentPhase === 'COMPOSING'" />
		<NotAuthorized v-else />
	</SidebarTemplate>
</template>