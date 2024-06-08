<script setup lang="ts">

import { SidebarTemplate } from "@/components/templates"
import NotAutorized from "../organisms/errors/NotAuthorized.vue"
import { ref } from "vue"
import { hasPermission } from "@/services/user"
import { useQuery } from "@tanstack/vue-query"
import { Header } from "@/components/molecules/header"
import { Column } from "@/components/atoms/containers"
import { ListChecks } from "lucide-vue-next"
import Grade from "@/components/organisms/Grade/GradeTable.vue"
import { Button } from "@/components/ui/button"
import ValidGradesDialog from "@/components/organisms/Grade/ValidGradesDialog.vue"
import { getGradesConfirmation } from "@/services/grade"
import ExportGrades from "../organisms/Grade/ExportGrades.vue"
import { SprintSelect, TeamSelect } from "../molecules/select"
import { Cookies } from "@/utils/cookie"
import { getTeamByUserId } from "@/services/team"
import { TeamSelect2 } from "@/components/molecules/select"

const teamId = ref<string | null>(null)
const sprintId = ref<string | null>(null)

const authorized = hasPermission("GRADES_PAGE")
const canConfirmOwnTeamGrade = hasPermission("GRADE_CONFIRMATION")

const { data: ssTeam } = useQuery({ queryKey: ["team", Cookies.getUserId()], queryFn: () => getTeamByUserId(Cookies.getUserId()) })

const { data: isGradesConfirmed, refetch: refetchGradesConfirmation } = useQuery({
	queryKey: ["grades-confirmation", sprintId.value, teamId.value],
	queryFn: async() => {
		if (sprintId.value === null || teamId.value === null) return false
		if (ssTeam.value?.id !== undefined) {
			return await getGradesConfirmation(parseInt(sprintId.value), parseInt(teamId.value), ssTeam.value?.id)
		}
	}
})

const canViewAllOg = hasPermission("VIEW_ALL_ORAL_GRADES")
const canViewAllWg = hasPermission("VIEW_ALL_WRITING_GRADES")

</script>

<template>
	<SidebarTemplate>
		<NotAuthorized v-if="!authorized" />
		<Column v-else class="gap-4">
			<Header title="Notes">
				<SprintSelect v-model="sprintId" />
				<TeamSelect v-model="teamId" v-if="canViewAllWg || canViewAllOg" />
				<TeamSelect2 v-model="teamId" v-else />
				<ValidGradesDialog
					v-if="teamId !== null && sprintId !== null && canConfirmOwnTeamGrade && isGradesConfirmed && ssTeam?.id.toString() == teamId"
					@valid:individual-grades="refetchGradesConfirmation" :selectedTeam="teamId"
					:selectedSprint="sprintId">
					<Button variant="default">Valider les notes individuelles</Button>
				</ValidGradesDialog>

				<ExportGrades v-if="hasPermission('EXPORT_INDIVIDUAL_GRADES')">
					<Button variant="default">Exporter</Button>
				</ExportGrades>
			</Header>
			<Column v-if="teamId !== null && sprintId !== null">
				<Grade v-if="authorized" :teamId="teamId ?? ''" :sprintId="sprintId ?? ''" />
				<NotAutorized v-else />
			</Column>
			<Column v-else class="items-center py-4 gap-2 border border-gray-300 border-dashed rounded-lg">
				<ListChecks class="size-12 stroke-1 text-dark-blue" />
				<p class="text-dark-blue text-sm">Vous n'avez pas sélectionné de sprint et/ou une équipe.</p>
			</Column>
		</Column>
	</SidebarTemplate>
</template>