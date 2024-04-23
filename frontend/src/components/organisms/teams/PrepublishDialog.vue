<script setup lang="ts">

import { ErrorText } from "@/components/atoms/texts"
import { LoadingButton } from "@/components/molecules/buttons"
import { CustomDialog, DialogClose } from "@/components/molecules/dialog"
import { Button } from "@/components/ui/button"
import { setCurrentPhase } from "@/services/project-service"
import { useMutation } from "@tanstack/vue-query"
import { ref } from "vue"

const emits = defineEmits(["prepublish:teams"])
const open = ref(false)

const { mutate, error, isPending } = useMutation({ mutationKey: ["prepublish-teams"], mutationFn: async() => {
	await setCurrentPhase("PREPUBLISHED")
		.then(() => open.value = false)
		.then(() => emits("prepublish:teams"))
} })

const DIALOG_TITLE = "Prépublier les équipes"
const DIALOG_DESCRIPTION = "Êtes-vous bien sûr de vouloir prépublier la composition des équipes ?"

</script>

<template>
	<CustomDialog :title="DIALOG_TITLE" :description="DIALOG_DESCRIPTION" v-model:open="open">
		<template #trigger>
			<slot />
		</template>

		<ErrorText v-if="error" class="mb-2">Une erreur est survenue.</ErrorText>

		<template #footer>
			<DialogClose v-if="!isPending">
				<Button variant="outline">Annuler</Button>
			</DialogClose>
			<LoadingButton type="submit" @click="mutate" :loading="isPending">
				Confirmer
			</LoadingButton>
		</template>
	</CustomDialog>
</template>