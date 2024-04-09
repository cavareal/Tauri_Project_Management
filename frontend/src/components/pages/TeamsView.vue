<script setup lang="ts">
import { ref, watch } from "vue"
import PageTemplate from "@/components/organisms/template/PageTemplate.vue"
import TeamsCreated from "@/components/organisms/teams/TeamsCreated.vue"
import TeamsNotsCreated from "@/components/organisms/teams/TeamsNotsCreated.vue"
import { GenerateTeamsDialog, StudentsNotImported, PrepublishDialog, DeleteTeamsDialog } from "@/components/organisms/teams"
import GenerateTeams from "@/components/organisms/teams/GenerateTeams.vue"
import getCookie from "@/utils/cookiesUtils"
import { Separator } from "@/components/ui/separator"
import { Row } from "@/components/atoms/containers"
import { Button } from "@/components/ui/button"
import { getQuantityOfStudents } from "@/services/student-service"
import { NotAuthorized } from "@/components/organisms/errors"
import type { ProjectPhase } from "@/types/project"
import { getTeams } from "@/services/team-service"
import type { Team } from "@/types/team"
import { getCurrentPhase } from "@/services/project-service"

const token = getCookie("token")
const role = getCookie("role")
const currentPhase = ref<ProjectPhase>("COMPOSING")
const nbStudents = ref(1)
const teams = ref<Team[]>([])

watch(() => { }, async() => {
	currentPhase.value = await getCurrentPhase()
}, { immediate: true })

watch(() => { }, async() => {
	nbStudents.value = await getQuantityOfStudents()
}, { immediate: true })

watch(() => { }, async() => {
	teams.value = await getTeams()
}, { immediate: true })

</script>

<template>
	<PageTemplate>
		<Row class="items-center justify-between">
			<h1 class="text-3xl font-title-bold">Équipes</h1>

			<Row class="gap-4" v-if="role === 'PL' && nbStudents > 0 && teams.length > 0 && currentPhase === 'COMPOSING'">
				<DeleteTeamsDialog>
					<Button variant="outline">Supprimer les équipes</Button>
				</DeleteTeamsDialog>
				<PrepublishDialog>
					<Button variant="default">Prépublier</Button>
				</PrepublishDialog>
			</Row>
		</Row>

		<Separator />

		<NotAuthorized v-if="!token || !role" />
		<StudentsNotImported v-else-if="role === 'PL' && currentPhase === 'COMPOSING' && nbStudents === 0" />
		<GenerateTeams v-else-if="role === 'PL' && currentPhase === 'COMPOSING' && nbStudents > 0 && teams.length === 0" />
		<!-- eslint-disable-next-line max-len -->
		<TeamsCreated v-else-if="(role === 'PL' || (role === 'SS' && currentPhase !=='COMPOSING') || role === 'OL' || (role === 'OS' && currentPhase !== 'COMPOSING')) && teams.length > 0" :phase="currentPhase" />
		<TeamsNotsCreated v-else-if="(role === 'SS' || role === 'OL') && currentPhase === 'COMPOSING'" />
		<NotAuthorized v-else />
	</PageTemplate>
</template>