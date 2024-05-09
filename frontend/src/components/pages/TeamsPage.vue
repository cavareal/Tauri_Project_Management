<script setup lang="ts">

import { SidebarTemplate } from "@/components/templates"
import {
	RedirectImportStudents, GenerateTeams, PrepublishDialog, DeleteTeamsDialog, TeamAccordion, TeamsNotCreated
} from "@/components/organisms/teams"
import { getCookie } from "@/utils/cookie"
import { Button } from "@/components/ui/button"
import { getAllStudents } from "@/services/student-service"
import { NotAuthorized } from "@/components/organisms/errors"
import { getTeams } from "@/services/team-service"
import { getProjectById } from "@/services/project-service"
import { Header } from "@/components/molecules/header"
import type { RoleType } from "@/types/role"
import { computed, onMounted, ref } from "vue"
import { PageSkeleton } from "@/components/atoms/skeletons"
import { useMutation, useQuery } from "@tanstack/vue-query"
import SignalTeamDialog from "@/components/organisms/teams/SignalTeamDialog.vue"
import ValidTeamDialog from "@/components/organisms/teams/ValidTeamDialog.vue"
import { userHasValidateTeams } from "@/services/flag-service"
import { addNotification } from "@/services/notification-service"
import { getUserById } from "@/services/user-service"
import type { Notification } from "@/types/notification"
import type { User } from "@/types/user"

const validateTeamDescription = "Validation des équipes prépubliées"
const token = getCookie("token")
const role = getCookie<RoleType>("role")
const currentProjectId = getCookie("currentProject")
const currentUserId = getCookie("user")
const hasValidateTeams = ref(true)

const { data: currentPhase, refetch: refetchCurrentPhase } = useQuery({
	queryKey: ["project"], queryFn: async() => (await (getProjectById(currentProjectId))).phase
})
const { data: nbStudents } = useQuery({ queryKey: ["nb-students"], queryFn: async() => (await getAllStudents()).length })
const { data: nbTeams, refetch: refetchTeams } = useQuery({ queryKey: ["nb-teams"], queryFn: async() => (await getTeams(currentProjectId)).length })

const displayButtons = computed(() => role === "PROJECT_LEADER" && nbStudents.value && nbStudents.value > 0
	&& nbTeams.value && nbTeams.value > 0 && currentPhase.value && currentPhase.value === "COMPOSING")

const displayPrepublishedButton = computed(() => role === "SUPERVISING_STAFF" && currentPhase.value === "PREPUBLISHED" && !hasValidateTeams.value)

const generateTeams = computed(() => role === "PROJECT_LEADER" && currentPhase.value === "COMPOSING")
const displayTeams = computed(() => (role === "PROJECT_LEADER" || (role === "SUPERVISING_STAFF" && currentPhase.value !== "COMPOSING")
	|| role === "OPTION_LEADER" || (role === "OPTION_STUDENT" && currentPhase.value !== "COMPOSING")))

const handleValidTeams = async() => {
	hasValidateTeams.value = await userHasValidateTeams(currentUserId, validateTeamDescription)
}

onMounted(async() => {
	if (currentUserId) {
		hasValidateTeams.value = await userHasValidateTeams(currentUserId, validateTeamDescription)
	}
})

//const currenrUser = await getUserById(currentUserId)
//const exampleUser = await getUserById("10")

//const notification: Notification = {
////	id: 100,
//	message: "La composition des équipes a été prépubliée.",
//	checked: false,
//	type: "CREATE_TEAMS",
//	userTo: exampleUser,
//	userFrom: currenrUser
//}

//TO DO
//const { mutate } = useMutation({ mutationKey: ["prepublish-teams"], mutationFn: async() => {
//	await addNotification(notification)
//} })

</script>

<template>
	<SidebarTemplate>
		<Header title="Équipes">
			<DeleteTeamsDialog v-if="displayButtons" @delete:teams="refetchTeams">
				<Button variant="outline">Supprimer les équipes</Button>
			</DeleteTeamsDialog>
			<PrepublishDialog v-if="displayButtons" @prepublish:teams="refetchCurrentPhase" @create:notifications="mutate">
				<Button variant="default">Prépublier</Button>
			</PrepublishDialog>
			<SignalTeamDialog v-if="displayPrepublishedButton" @signal:teams="refetchTeams" :currentUserId="currentUserId">
				<Button variant="outline">Signaler</Button>
			</SignalTeamDialog>
			<ValidTeamDialog v-if="displayPrepublishedButton" @valid:teams="handleValidTeams" :currentUserId="currentUserId">
				<Button variant="default">Valider</Button>
			</ValidTeamDialog>
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