<script setup lang="ts">

import { Button } from "@/components/ui/button"
import { deleteAllStudents } from "@/services/student-service"
import LoadingButton from "@/components/molecules/buttons/LoadingButton.vue"
import { ref } from "vue"
import { CustomDialog, DialogClose } from "@/components/molecules/dialog"
import { useMutation } from "@tanstack/vue-query"
import { ErrorText } from "@/components/atoms/texts"
import { getCookie } from "@/utils/cookie"

const open = ref(false)
const emits = defineEmits(["delete:students"])
const currentProjectId = getCookie("currentProject")

const { mutate, isPending, error } = useMutation({ mutationKey: ["delete-students"], mutationFn: async() => {
	await deleteAllStudents(currentProjectId)
		.then(() => open.value = false)
		.then(() => emits("delete:students"))
} })

const DIALOG_TITLE = "Supprimer les étudiants"
const DIALOG_DESCRIPTION = "Êtes-vous bien sûr de vouloir supprimer tous les étudiants de la base de données ?"

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