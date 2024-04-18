<script setup lang="ts">

import { DialogClose } from "@/components/ui/dialog"
import { Button } from "@/components/ui/button"
import { deleteAllStudents } from "@/services/student-service"
import { useMutation } from "@/utils/api/api.util"
import LoadingButton from "@/components/molecules/buttons/LoadingButton.vue"
import { ref } from "vue"
import { CustomDialog } from "@/components/molecules/dialog"

const DIALOG_TITLE = "Supprimer les étudiants"
const DIALOG_DESCRIPTION = "Êtes-vous bien sûr de vouloir supprimer tous les étudiants de la base de données ?"

const open = ref(false)

const emits = defineEmits(["delete:students"])

const { loading, mutate } = useMutation(async() => {
	await deleteAllStudents()
		.then(() => open.value = false)
		.then(() => emits("delete:students"))
})

</script>

<template>
	<CustomDialog :title="DIALOG_TITLE" :description="DIALOG_DESCRIPTION" v-model:open="open">
		<template #trigger>
			<slot />
		</template>

		<template #footer>
			<DialogClose v-if="!loading">
				<Button variant="outline">Annuler</Button>
			</DialogClose>
			<LoadingButton type="submit" @click="mutate" :loading="loading">
				Confirmer
			</LoadingButton>
		</template>
	</CustomDialog>
</template>