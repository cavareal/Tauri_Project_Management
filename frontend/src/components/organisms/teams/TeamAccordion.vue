<script setup lang="ts">

import { Accordion, AccordionItem, AccordionTrigger } from "@/components/ui/accordion"
import { getTeams } from "@/services/team-service"
import TeamAccordionContent from "@/components/organisms/teams/TeamAccordionContent.vue"
import { getCookie } from "@/utils/cookie"
import { Button } from "@/components/ui/button"
import { Pencil } from "lucide-vue-next"
import EditTeamDialog from "./EditTeamDialog.vue"
import { Row } from "@/components/atoms/containers"
import { useQuery, useQueryClient } from "@tanstack/vue-query"
import { PageSkeleton } from "@/components/atoms/skeletons"
import type { ProjectPhase } from "@/types/project"
import { ref } from "vue"
import { StudentSchema, type Student } from "@/types/student"
import { changeStudentTeam, getStudentsByTeamId } from "@/services/student-service"
import { cn } from "@/utils/style"

const role = getCookie("role")
const currentProject = getCookie("currentProject")

const props = defineProps<{
	phase: ProjectPhase
}>()

const queryClient = useQueryClient()

const dragging = ref<number | null>(null)
const students = ref<Record<number, Student[]>>()

const { data: teams, refetch: refetchTeams, isLoading, isFetching } = useQuery({ queryKey: ["teams"], queryFn: async() => {
	const teams = await getTeams(currentProject)

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
	if (!students.value) return

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
	await changeStudentTeam(student.id, teamId)
		.then(() => queryClient.invalidateQueries({ queryKey: ["criteria", teamId] }))
		.then(() => queryClient.invalidateQueries({ queryKey: ["criteria", originTeam.id] }))
		.then(() => queryClient.invalidateQueries({ queryKey: ["average", teamId] }))
		.then(() => queryClient.invalidateQueries({ queryKey: ["average", originTeam.id] }))
}

const handleDragEnter = (event: DragEvent, teamId: number) => {
	event.preventDefault()
	if (event.dataTransfer) event.dataTransfer.dropEffect = "move"
	dragging.value = teamId
}

const handleDragOver = (event: DragEvent, teamId: number) => {
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

</script>

<template>
	<PageSkeleton v-if="isLoading || isFetching" />
	<Accordion v-else type="multiple">
		<Row v-for="team in teams" :key="team.id" class="w-full items-start gap-8">
			<AccordionItem :value="team.id.toString()" class="flex-1" :class="style(team.id)"
				v-on:drop="(e: DragEvent) => handleDrop(e, team.id)"
				v-on:dragenter="(e: DragEvent) => handleDragEnter(e, team.id)"
				v-on:dragover="(e: DragEvent) => handleDragOver(e, team.id)"
				v-on:dragleave="handleDragLeave"
			>
				<AccordionTrigger>
					{{ team.name }}
					{{ team.leader?.name ? `(${team.leader.name})` : "" }}
				</AccordionTrigger>
				<TeamAccordionContent :team-id="team.id" :phase="props.phase" :students="(students && students[team.id]) ?? null" />
			</AccordionItem>

			<EditTeamDialog v-if="role === 'PROJECT_LEADER'" :team="team" @edit:team="refetchTeams">
				<Button variant="outline" size="icon" class="mt-2">
					<Pencil class="w-4" />
				</Button>
			</EditTeamDialog>
		</Row>
	</Accordion>
</template>