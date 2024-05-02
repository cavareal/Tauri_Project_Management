<script setup lang="ts">

import { ref, watch } from "vue"
import LoadingButton from "@/components/molecules/buttons/LoadingButton.vue"
import UploadArea from "@/components/molecules/upload-area/UploadArea.vue"
import ErrorText from "@/components/atoms/texts/ErrorText.vue"
import { importStudentFile } from "@/services/student-service"
import { CustomDialog, DialogClose } from "@/components/molecules/dialog"
import { Button } from "@/components/ui/button"
import Calendar from "@/components/molecules/calendar/Calendar.vue"
import { Column, Row } from "@/components/atoms/containers"
import {
  Select,
} from '@/components/ui/select'
import { useMutation } from "@tanstack/vue-query"
import { CalendarDate } from '@internationalized/date'

const DIALOG_TITLE = "Ajouter un sprint"
const DIALOG_DESCRIPTION = "Pour ajouter un sprint, vous devez spécifier les dates de début et de fin, ainsi que le type de sprint."

const open = ref(false)

const emits = defineEmits(["add:sprint"])

const startDate = ref<CalendarDate | null>(null)
const endDate = ref<CalendarDate | null>(null)




</script>

<template>
	<CustomDialog :title="DIALOG_TITLE" :description="DIALOG_DESCRIPTION" v-model:open="open">
		<template #trigger>
			<slot />
		</template>

		<div>
			<Row class="flex items-center my-2">
				<Label class="w-1/2">Date de début</Label>
				<Calendar v-model="startDate" />
			</Row>
			<Row class="flex items-center my-2">
				<Label class="w-1/2">Date de fin</Label>
				<Calendar v-model="endDate" :min-value="startDate" />
			</Row>
			<Row class="flex items-center my-2">
				<Label class="w-1/2">Type de fin de sprint</Label>
				<Select></Select>
			</Row>
		</div>

	<!--
		<ErrorText v-if="error" class="mb-2">Une erreur est survenue lors de l'ajout du sprint.</ErrorText>
	-->	

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