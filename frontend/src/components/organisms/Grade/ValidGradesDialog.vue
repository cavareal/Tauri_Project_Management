<script setup lang="ts">

import { Button } from "@/components/ui/button"
import { CustomDialog, DialogClose } from "@/components/molecules/dialog"
import { ref } from "vue"
import { useMutation } from "@tanstack/vue-query"
import { ErrorText } from "@/components/atoms/texts"
import { LoadingButton } from "@/components/molecules/buttons"
import { setGradesConfirmation } from "@/services/grade"
import { createToast } from "@/utils/toast"

const emits = defineEmits(["valid:individual-grades"])
const open = ref(false)

const props = defineProps<{
	selectedTeam?: string
	selectedSprint?: string
}>()

const { mutate, isPending, error } = useMutation({
	mutationKey: ["individual-grades"], mutationFn: async() => {
		console.log(props)
		await setGradesConfirmation(Number(props.selectedTeam), Number(props.selectedSprint))
			.then(() => open.value = false)
			.then(() => emits("valid:individual-grades"))
			.then(() => createToast("Les notes individuelles ont été validées."))
	}
})

const DIALOG_TITLE = "Valider les notes individuelles"
const DIALOG_DESCRIPTION
	= "Êtes-vous bien sûr de valider les notes individuelles de chacun des membres de votre équipes ?"

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