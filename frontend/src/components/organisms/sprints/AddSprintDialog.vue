<script setup lang="ts">

import { ref } from "vue"
import LoadingButton from "@/components/molecules/buttons/LoadingButton.vue"
import UploadArea from "@/components/molecules/upload-area/UploadArea.vue"
import ErrorText from "@/components/atoms/texts/ErrorText.vue"
import { importStudentFile } from "@/services/student-service"
import { CustomDialog, DialogClose } from "@/components/molecules/dialog"
import { Button } from "@/components/ui/button"
import { Calendar } from "@/components/molecules/calendar/Calendar.vue"

import { useMutation } from "@tanstack/vue-query"

const DIALOG_TITLE = "Ajouter un sprint"
const DIALOG_DESCRIPTION = "Pour ajouter un sprint, vous devez spécifier les dates de début et de fin, ainsi que le type de sprint."

const open = ref(false)

const emits = defineEmits(["add:sprint"])

const file = ref<File | null>(null)

const { error, isPending, mutate: upload } = useMutation({ mutationKey: ["add-sprint"], mutationFn: async() => {
	if (!file.value) return
	await importStudentFile(file.value)
		.then(() => open.value = false)
		.then(() => emits("add:sprint"))
} })

</script>

<template>
	<CustomDialog :title="DIALOG_TITLE" :description="DIALOG_DESCRIPTION" v-model:open="open">
		<template #trigger>
			<slot />
		</template>

		<div>
			<Calendar />
		</div>
		
		<ErrorText v-if="error" class="mb-2">Une erreur est survenue lors de l'ajout du sprint.</ErrorText>

		<template #footer>
			<DialogClose>
				<Button variant="outline">Annuler</Button>
			</DialogClose>
			<LoadingButton type="submit" class="flex items-center" :loading="isPending" @click="upload">
				Continuer
			</LoadingButton>
		</template>
	</CustomDialog>
</template>