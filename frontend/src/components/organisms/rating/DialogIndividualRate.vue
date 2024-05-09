<script setup lang="ts">

import { CustomDialog } from "@/components/molecules/dialog"
import { Button } from "@/components/ui/button"
import { Column, Row } from "@/components/atoms/containers"
import { Input } from "@/components/ui/input"
import { onMounted, ref } from "vue"
import { DialogClose } from "@/components/ui/dialog"
import { useMutation, useQuery } from "@tanstack/vue-query"
import type { GradeType } from "@/types/grade-type"
import { getGradeTypeByName } from "@/services/grade-type-service"
import { createGrade } from "@/services/grade-service"
import { createToast } from "@/utils/toast"
import { getTeamById, getTeamByUserId } from "@/services/team-service"
import type { Team } from "@/types/team"
import { hasPermission } from "@/services/user-service"
import { getStudentsByTeamId } from "@/services/student-service"
import { Cookies } from "@/utils/cookie"

let mark = ref(["0", "0", "0", "0", "0", "0", "0", "0"])
const open = ref(false)
const currentTeam = ref<Team>()

const DIALOG_DESCRIPTION = "Vous pouvez attribuer une note individuelle à chaque étudiant"

const gradeIndividual = hasPermission("GRADE_INDIVIDUAL_PERFORMANCE")

const props = defineProps<{
	title: string,
	description : string,
	teamId : string,
	sprintId : string,
	gradeTypeString : string
}>()

const { data: gradeType } = useQuery<GradeType, Error>({
	queryKey: ["grade-type"],
	queryFn: () => getGradeTypeByName(props.gradeTypeString)
})

const { data: teamStudents, refetch } = useQuery({ queryKey: ["team-students"], queryFn: async() => {
	if (!props.teamId) return
	return await getStudentsByTeamId(Number(props.teamId))
} })

onMounted(async() => {
	if (props.teamId) return
	currentTeam.value = await getTeamById(Number(props.teamId))
})

const { mutate, isPending, error } = useMutation({ mutationKey: ["create-grade"], mutationFn: async() => {
	await createGrade({
		value: Number(mark.value),
		gradeTypeId: gradeType.value.id,
		teamId: Number(props.teamId),
		sprintId: Number(props.sprintId),
		comment: null,
		studentId: null
	})
	//.then(() => mark.value = "")
		.then(() => createToast("La note a bien été enregistrée."))
		.then(() => open.value = false)
} })
const handleInput = (event: InputEvent, index: number) => {
	const inputNote = parseInt((event.target as HTMLInputElement).value)
	if (inputNote > 20) {
		mark.value[index] = String(20)
	} else if (inputNote < 0) {
		mark.value[index] = String(0)
	} else {
		mark.value[index] = String(inputNote)
	}
}


const handleTriggerClick = async() => {
	await refetch()
}

</script>

<template>
	<CustomDialog title="Notes individuelles" :description="DIALOG_DESCRIPTION">
		<template #trigger>
			<Button variant="default" @click="handleTriggerClick">Voir les notes</Button>
		</template>
		<div class="flex">
			<Row class="flex-wrap">
				<Row v-for="(student, index) in teamStudents" :key="student.id" class="grid grid-cols-3 items-center gap-4 mb-2 w-1/2">
					<Label>{{ student.name }}</Label>
					<Input type="number" @input="handleInput($event, index)" />
				</Row>
			</Row>
		</div>
		<template #footer>
			<DialogClose>
				<Button variant="default">Valider</Button>
			</DialogClose>
		</template>
	</CustomDialog>
</template>
<style scoped>

</style>