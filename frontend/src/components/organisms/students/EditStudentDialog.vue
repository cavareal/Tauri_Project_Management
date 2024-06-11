<script setup lang="ts">

import { ErrorText } from "@/components/atoms/texts"
import { LoadingButton } from "@/components/molecules/buttons"
import { CustomDialog, DialogClose } from "@/components/molecules/dialog"
import { Button } from "@/components/ui/button"
import { createToast } from "@/utils/toast"
import { createStudent } from "@/services/student"
import { useMutation, useQuery } from "@tanstack/vue-query"
import { ref, watch } from "vue"
import { Row } from "@/components/atoms/containers"
import { Input } from "@/components/ui/input"
import { cn } from "@/utils/style"
import { Select, SelectContent, SelectGroup, SelectItem, SelectTrigger, SelectValue } from "@/components/ui/select"
import { Switch } from "@/components/ui/switch"
import { updateStudent } from "@/services/student"
import type { Student } from "@/types/student"
import type { Grade } from "@/types/grade"
import { getUserByName } from "@/services/user"
import { updateGrade } from "@/services/grade"
import {
	AlertDialog,
	AlertDialogAction,
	AlertDialogCancel,
	AlertDialogContent,
	AlertDialogDescription,
	AlertDialogFooter,
	AlertDialogHeader,
	AlertDialogTitle,
	AlertDialogTrigger
} from "@/components/ui/alert-dialog"

const props = defineProps<{
	student: Student
	grade : number
}>()

const open = ref(false)
const rowClass = cn("grid grid-cols-2 items-center mb-2 justify-between")
const gendered = ref("")
const newGrade = ref("")
let nameParts = props.student.name.split(" ")
let firstName = nameParts[0]
let lastName = nameParts.slice(1).join(" ")
let fullName = ref(props.student.name)
const bachelor = ref(props.student.bachelor)


const DIALOG_TITLE = "Modification d'un étudiant"
const DIALOG_DESCRIPTION = "Vous pouvez modifier un étudiant en remplissant les champs suivants."


const { mutate, isPending, error } = useMutation({ mutationKey: ["add-student"], mutationFn: async() => {
	await updateStudent(props.student.id.toString(), {
		name: fullName,
		gender: gendered.value,
		bachelor: bachelor.value
	})
	await updateGrade(Number(props.grade.id), {
		value: Number(newGrade.value),
		teamId: null,
		sprintId: null,
		studentId: props.student.id,
		comment: null
	})
		.then(() => newGrade.value = "")
		.then(() => lastName = "")
		.then(() => firstName  = "")
		.then(() => gendered.value = "")
		.then(() => bachelor.value = false)
		.then(() => createToast("L'étudiant a bien été modifié."))
		.then(() => open.value = false)
} })

const onGradeChange = (value: string | number) => {
	if (Number(value) > 20) {
		newGrade.value = "20"
		return
	}
	if (Number(value) < 0) {
		newGrade.value = "0"
		return
	}
	newGrade.value = value.toString()
}

watch([lastName, firstName], () => {
	fullName.value = lastName + " " + firstName
	useQuery({ queryKey: ["user"], queryFn: () => getUserByName(fullName.value) })
})

</script>

<template>
<!--	<CustomDialog :title="DIALOG_TITLE" :description="DIALOG_DESCRIPTION" v-model:open="open">-->
<!--		<template #trigger>-->
<!--			<slot @click.stop/>-->
<!--		</template>-->

<!--		<ErrorText v-if="error">Une erreur est survenue.</ErrorText>-->
<!--		<Row :class="rowClass">-->
<!--			<Label>Nom : </Label>-->
<!--			<Input v-model="lastName" class="w-full"/>-->
<!--		</Row>-->
<!--		<Row :class="rowClass">-->
<!--			<Label>Prénom : </Label>-->
<!--			<Input v-model="firstName" class="w-full"/>-->
<!--		</Row>-->
<!--		<Row :class="rowClass">-->
<!--			<Label>Genre : </Label>-->
<!--			<Select v-model="gendered">-->
<!--				<SelectTrigger >-->
<!--					<SelectValue placeholder="Genre" />-->
<!--				</SelectTrigger>-->
<!--				<SelectContent>-->
<!--					<SelectGroup>-->
<!--						<SelectItem value='MAN'>Homme</SelectItem>-->
<!--						<SelectItem value='WOMAN'>Femme</SelectItem>-->
<!--					</SelectGroup>-->
<!--				</SelectContent>-->
<!--			</Select>-->
<!--		</Row>-->
<!--		<Row :class="rowClass">-->
<!--			<Label>Bachelor :</Label>-->
<!--			<div class="flex justify-end">-->
<!--&lt;!&ndash;				<Switch id="Bachelor" :checked="bachelor" @update:checked="value => bachelor = value"/>&ndash;&gt;-->
<!--			</div>-->
<!--		</Row>-->
<!--		<Row :class="rowClass">-->
<!--			<Label>Moyenne :</Label>-->
<!--			<Input  type="number" min="0" max="20"  @update:model-value="onGradeChange"/>-->
<!--		</Row>-->

<!--		<template #footer>-->
<!--			<DialogClose v-if="!isPending">-->
<!--				<Button variant="outline">Annuler</Button>-->
<!--			</DialogClose>-->
<!--			<LoadingButton type="submit" @click="mutate" :loading="isPending">-->
<!--				Ajouter-->
<!--			</LoadingButton>-->
<!--		</template>-->
<!--	</CustomDialog>-->
	<AlertDialog>
		<AlertDialogTrigger as-child>
			<Button variant="outline">
				Show Dialog
			</Button>
		</AlertDialogTrigger>
		<AlertDialogContent>
			<AlertDialogHeader>
				<AlertDialogTitle>Are you absolutely sure?</AlertDialogTitle>
				<AlertDialogDescription>
					This action cannot be undone. This will permanently delete your
					account and remove your data from our servers.
				</AlertDialogDescription>
			</AlertDialogHeader>
			<AlertDialogFooter>
				<AlertDialogCancel>Cancel</AlertDialogCancel>
				<AlertDialogAction>Continue</AlertDialogAction>
			</AlertDialogFooter>
		</AlertDialogContent>
	</AlertDialog>
</template>