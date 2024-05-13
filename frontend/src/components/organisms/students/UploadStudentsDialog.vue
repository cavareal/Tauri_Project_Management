<script setup lang="ts">

import { ref } from "vue"
import LoadingButton from "@/components/molecules/buttons/LoadingButton.vue"
import UploadArea from "@/components/molecules/upload-area/UploadArea.vue"
import ErrorText from "@/components/atoms/texts/ErrorText.vue"
import { importStudentFile } from "@/services/student-service"
import { CustomDialog, DialogClose } from "@/components/molecules/dialog"
import { Button } from "@/components/ui/button"
import { useMutation } from "@tanstack/vue-query"
import { createToast } from "@/utils/toast"

const DIALOG_TITLE = "Importer les étudiants"
const DIALOG_DESCRIPTION
	= "Pour importer les étudiants, il suffit de déposer le fichier qui contient toutes leurs informations ainsi que leurs notes."

const open = ref(false)

const emits = defineEmits(["import:students"])

const file = ref<File | null>(null)

const { error, isPending, mutate: upload } = useMutation({ mutationKey: ["import-students"], mutationFn: async() => {
	if (!file.value) return
	await importStudentFile(file.value)
		.then(() => open.value = false)
		.then(() => emits("import:students"))
		.then(() => createToast("Les étudiants ont été importés."))
} })

</script>

<template>
	<CustomDialog :title="DIALOG_TITLE" :description="DIALOG_DESCRIPTION" v-model:open="open">
		<template #trigger>
			<slot />
		</template>

		<UploadArea v-model="file" />
		<ErrorText v-if="error" class="mb-2">Une erreur est survenue lors de l'importation du fichier.</ErrorText>

		<template #footer>
			<DialogClose v-if="!isPending">
				<Button variant="outline">Annuler</Button>
			</DialogClose>
			<LoadingButton type="submit" class="flex items-center" :loading="isPending" @click="upload">
				Continuer
			</LoadingButton>
		</template>
	</CustomDialog>
</template>