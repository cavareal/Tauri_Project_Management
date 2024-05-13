<script setup lang="ts">

import { DialogClose } from "@/components/ui/dialog"
import { Button } from "@/components/ui/button"
import { CustomDialog } from "@/components/molecules/dialog"
import { ErrorText, Text } from "@/components/atoms/texts"
import { LoadingButton } from "@/components/molecules/buttons"
import { Textarea } from "@/components/ui/textarea"
import { ref, computed } from "vue"
import { useMutation, useQueryClient } from "@tanstack/vue-query"
import { createToast } from "@/utils/toast"
import type { Team } from "@/types/team"
import { createFeedback } from "@/services/feedback-service"

const props = defineProps<{
	selectedTeamId: number,
	selectedSprintId: number
}>()

const emits = defineEmits(["feedback:added"])
const client = useQueryClient()

const open = ref(false)
const feedback = ref("")

const isDisabled = computed(() => feedback.value === "")

const { mutate, isPending, error } = useMutation({
	mutationKey: ["add-feedback"], mutationFn: async() => {
		await createFeedback(props.selectedTeamId, feedback.value, props.selectedSprintId)
			.then(() => open.value = false)
			.then(() => emits("feedback:added"))
			.then(() => client.invalidateQueries({
				queryKey: ["feedbacks", props.selectedTeamId, props.selectedSprintId]
			}))
			.then(() => feedback.value = "")
			.then(() => createToast("Le feedback a été enregistré."))
	}
})

const DIALOG_TITLE = "Donner un feedback"
const DIALOG_DESCRIPTION = "Envoyer un feedback à l'équipe sélectionné sur le déroulement du sprint"

const getTeamName = (team: Team) => {
	if (team) {
		return team.name!
	}
	return ""
}

const getTeamID = (team: Team) => {
	if (team) {
		return team.id.toString()
	}
	return ""
}
</script>

<template>
	<CustomDialog :title="DIALOG_TITLE" :description="DIALOG_DESCRIPTION" v-model:open="open">
		<template #trigger>
			<slot />
		</template>

		<Text class="-mb-2">Votre feedback</Text>
		<Textarea v-model="feedback" placeholder="Ajouter un feedback" class="max-h-64"></Textarea>
		<ErrorText v-if="error" class="mt-2">Une erreur est survenue.</ErrorText>

		<template #footer>
			<DialogClose v-if="!isPending">
				<Button variant="outline">Annuler</Button>
			</DialogClose>
			<LoadingButton type="submit" @click="mutate" :loading="isPending" :disabled="isDisabled">
				Confirmer
			</LoadingButton>
		</template>
	</CustomDialog>
</template>

<style scoped></style>