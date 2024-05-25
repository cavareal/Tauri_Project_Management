<script setup lang="ts">

import { Accordion, AccordionItem, AccordionTrigger } from "@/components/ui/accordion"
import { getTeams } from "@/services/team/team.service"
import TeamAccordionContent from "@/components/organisms/teams/TeamAccordionContent.vue"
import { Button } from "@/components/ui/button"
import { Pencil } from "lucide-vue-next"
import EditTeamDialog from "./EditTeamDialog.vue"
import { Row } from "@/components/atoms/containers"
import { useQuery, useQueryClient } from "@tanstack/vue-query"
import { ref } from "vue"
import { StudentSchema, type Student } from "@/types/student"
import { updateStudent, getStudentsByTeamId } from "@/services/student/student.service"
import { cn } from "@/utils/style"
import { Loading } from "@/components/organisms/loading"
import { hasPermission } from "@/services/user/user.service"
import { sendManyNotifications } from "@/services/notification/notification.service"
import { getCurrentPhase } from "@/services/project/project.service"

const queryClient = useQueryClient()

const dragging = ref<number | null>(null)
const students = ref<Record<number, Student[]>>()

const { data: currentPhase } = useQuery({ queryKey: ["current-phase"], queryFn: getCurrentPhase })

const { data: teams, refetch: refetchTeams, isLoading } = useQuery({ queryKey: ["teams"], queryFn: async() => {
	const teams = await getTeams()

	students.value = {}
	await Promise.all(teams.map(async(team) => {
		const teamStudents = await getStudentsByTeamId(team.id)
		students.value = { ...students.value, [team.id]: teamStudents }
	}))

	return teams
} })

const handleDrop = async(event: DragEvent, teamId: number) => {
	event.preventDefault()
	dragging.value = null
	if (!students.value || !currentPhase) return

	const data = event.dataTransfer?.getData("text/plain")
	if (!data) return
	const studentData = StudentSchema.safeParse(JSON.parse(data))
	if (!studentData.success) return
	const student = studentData.data

	const originTeam = teams.value?.find(team => students.value?.[team.id]?.find(s => s.id === student.id))
	if (!originTeam || originTeam.id === teamId) return

	students.value = {
		...students.value,
		[originTeam.id]: students.value[originTeam.id].filter(s => s.id !== student.id),
		[teamId]: [...(students.value[teamId] ?? []), student].sort((a, b) => a.id - b.id)
	}
	await updateStudent(student.id.toString(), { teamId })
		.then(() => queryClient.invalidateQueries({ queryKey: ["criteria", teamId] }))
		.then(() => queryClient.invalidateQueries({ queryKey: ["criteria", originTeam.id] }))
		.then(() => queryClient.invalidateQueries({ queryKey: ["average", teamId] }))
		.then(() => queryClient.invalidateQueries({ queryKey: ["average", originTeam.id] }))
		.then(() => {
			if (currentPhase.value === "COMPOSING") return
			void sendManyNotifications(`L'étudiant ${student.name} a été déplacé de l'équipe "${originTeam.name}" à l'équipe "${teams.value?.find(t => t.id === teamId)?.name}".`)
		})
}

const handleDragEnter = (event: DragEvent, teamId: number) => {
	event.preventDefault()
	if (event.dataTransfer) event.dataTransfer.dropEffect = "move"
	dragging.value = teamId
}

const handleDragLeave = (event: DragEvent) => {
	event.preventDefault()
	if (event.dataTransfer) event.dataTransfer.dropEffect = "move"
	dragging.value = null
}

const style = (teamId: number) => cn(
	"border-[1px] border-x-transparent border-t-transparent px-4",
	{ "border-dashed rounded-md border-x-light-blue border-t-light-blue border-b-light-blue": dragging.value === teamId }
)

const canEdit = hasPermission("TEAM_MANAGEMENT")

</script>

<template>
	<Loading v-if="isLoading" />
	<Accordion v-else type="multiple" :default-value="teams && teams.map(team => team.id.toString())">
		<Row v-for="team in teams" :key="team.id" class="w-full items-start gap-8">
			<AccordionItem :value="team.id.toString()" class="flex-1" :class="style(team.id)"
				v-on:drop="(e: DragEvent) => handleDrop(e, team.id)"
				v-on:dragenter="(e: DragEvent) => handleDragEnter(e, team.id)"
				v-on:dragover="(e: DragEvent) => handleDragEnter(e, team.id)"
				v-on:dragleave="handleDragLeave"
			>
				<AccordionTrigger>
					{{ team.name }}
					{{ team.leader?.name ? `(${team.leader.name})` : "" }}
				</AccordionTrigger>
				<TeamAccordionContent :team-id="team.id" :students="(students && students[team.id]) ?? null" />
			</AccordionItem>

			<EditTeamDialog v-if="canEdit" :team="team" @edit:team="refetchTeams">
				<Button variant="outline" size="icon" class="mt-2">
					<Pencil class="w-4" />
				</Button>
			</EditTeamDialog>
		</Row>
	</Accordion>
</template>