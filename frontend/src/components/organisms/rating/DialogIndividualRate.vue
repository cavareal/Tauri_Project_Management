<script setup lang="ts">

import { CustomDialog } from "@/components/molecules/dialog"
import { Button } from "@/components/ui/button"
import { Column, Row } from "@/components/atoms/containers"
import { Input } from "@/components/ui/input"
import { ref } from "vue"
import { DialogClose } from "@/components/ui/dialog"
import { useMutation, useQuery } from "@tanstack/vue-query"
import type { GradeType } from "@/types/grade-type"
import { getGradeTypeByName } from "@/services/grade-type-service"
import { createGrade } from "@/services/grade-service"
import { createToast } from "@/utils/toast"

let mark = ref(["0", "0", "0", "0", "0", "0", "0", "0"])
const names = ["Alice", "Bob", "Charlie", "David", "Emma", "Frank", "Grace", "Henry"]
const firstColumn = names.slice(0, 4)
const secondColumn = names.slice(4)
const open = ref(false)

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

</script>

<template>
	<CustomDialog title="Notes individuelles" description="Vous pouvez attribuer une note individuelle à chaque étudiant">
		<template #trigger>
			<Button variant="default">Voir les notes</Button>
		</template>
		<div class="flex">
			<Column>
				<Row v-for="(name, index) in firstColumn" :key="index" class="grid grid-cols-3 items-center gap-4 mb-2">
					<Label>{{ name }}</Label>
					<Input v-model="mark[index]" type="number" min="0" max="20" @input="handleInput($event, index)"/>
				</Row>
			</Column>
			<Column>
				<Row v-for="(name, index) in secondColumn" :key="index" class="grid grid-cols-3 items-center gap-4 mb-2">
					<Label>{{ name }}</Label>
					<Input v-model="mark[index + 4]" type="number" min="0" max="20" @input="handleInput($event,index + 4)"/>
				</Row>
			</Column>
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