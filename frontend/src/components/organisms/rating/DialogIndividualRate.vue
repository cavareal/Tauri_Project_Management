<script setup lang="ts">

import { CustomDialog } from "@/components/molecules/dialog"
import { Button } from "@/components/ui/button"
import { Row } from "@/components/atoms/containers"
import { Input } from "@/components/ui/input"
import { ref } from "vue"
import { DialogClose } from "@/components/ui/dialog"
import { useMutation, useQuery } from "@tanstack/vue-query"
import type { GradeType } from "@/types/grade-type"
import { getGradeTypeByName } from "@/services/grade-type-service"
import { createGrade } from "@/services/grade-service"
import { createToast } from "@/utils/toast"
import { hasPermission } from "@/services/user-service"
import { getStudentsByTeamId } from "@/services/student-service"
import LoadingButton from "../../molecules/buttons/LoadingButton.vue"

const open = ref(false)
const DIALOG_DESCRIPTION = "Vous pouvez attribuer une note individuelle à chaque étudiant"

const gradeIndividual = hasPermission("GRADE_INDIVIDUAL_PERFORMANCE")

const props = defineProps<{
	title: string,
	description : string,
	teamId : string,
	sprintId : string,
	gradeTypeString : string
}>()

const { data: gradeType, ...gradeTypeQuery } = useQuery<GradeType, Error>({
	queryKey: ["grade-type"],
	queryFn: () => getGradeTypeByName(props.gradeTypeString)
})


const { data: teamStudents, refetch } = useQuery({ queryKey: ["team-students"], queryFn: async() => {
	if (!props.teamId) return
	return await getStudentsByTeamId(Number(props.teamId))
} })

let marks = ref<{ studentId: number; mark: number; }[]>([])


const { mutate, isPending, error } = useMutation({ mutationKey: ["create-grade"], mutationFn: async() => {
	let gradesAdded = 0
	for (let i = 0; i < marks.value.length; i++) {
		await createGrade({
			value: Number(marks.value[i].mark),
			gradeTypeId: gradeType.value.id,
			teamId: null,
			sprintId: Number(props.sprintId),
			comment: null,
			studentId: marks.value[i].studentId,
			isValid: false
		})
			.then(() => {
				marks.value[i].mark = 0
				gradesAdded++
				if (gradesAdded === marks.value.length) {
					open.value = false
				}
			})
			.then(() => createToast("La note a bien été enregistrée."))
	}
} })

const handleInput = (event: InputEvent, index: number, studentId : number) => {
	const inputNote = parseInt((event.target as HTMLInputElement).value)
	if (!marks.value[index]) {
		marks.value[index] = { studentId: 0, mark: 0 }
	}
	if (inputNote > 20) {
		marks.value[index].mark = 20
	} else if (inputNote < 0) {
		marks.value[index].mark = 0
	} else {
		marks.value[index].mark = Number(inputNote)
	}
	marks.value[index].studentId = studentId
}


const handleTriggerClick = async() => {
	await gradeTypeQuery.refetch()
	await refetch()
}

</script>

<template>
	<CustomDialog title="Notes individuelles" :description="DIALOG_DESCRIPTION" class="w-full">
		<template #trigger>
			<Button variant="default" @click="handleTriggerClick">Voir les notes</Button>
		</template>
		<div class="flex">
			<Row class="flex-wrap">
				<Row v-for="(student, index) in teamStudents" :key="student.id" class="grid grid-cols-[2fr,1fr] items-center mb-2 w-1/2">
				<Label :for="student.name" class="ml-2">{{ student.name }}</Label>
					<Input type="number" min="0" max="20"  @input="handleInput($event, index, student.id)" />
				</Row>
			</Row>
		</div>
		<template #footer>
			<DialogClose>
				<Button variant="outline">Annuler</Button>
			</DialogClose>
			<DialogClose>
				<LoadingButton type="submit" :loading="isPending" @click="mutate">
					Confirmer
				</LoadingButton>
			</DialogClose>
		</template>
	</CustomDialog>
</template>
<style scoped>

</style>