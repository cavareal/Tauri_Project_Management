<script setup lang="ts">

import { ref } from "vue"
import { Button } from "@/components/ui/button"
import { Input } from "@/components/ui/input"
import { Label } from "@/components/ui/label"
import { generateTeams } from "@/services/team-service"
import { CustomDialog, DialogClose } from "@/components/molecules/dialog"
import { LoadingButton } from "@/components/molecules/buttons"
import { useMutation } from "@tanstack/vue-query"
import { ErrorText } from "@/components/atoms/texts"
import { Column, Row } from "@/components/atoms/containers"

const nbTeams = ref("6")
const womenPerTeam = ref("1")

const open = ref(false)

const emits = defineEmits(["generate:teams"])

defineProps<{
	nbStudents: number
}>()

const { mutate, isPending, error } = useMutation({ mutationKey: ["generate-teams"], mutationFn: async() => {
	await generateTeams(nbTeams.value, womenPerTeam.value)
		.then(() => open.value = false)
		.then(() => emits("generate:teams"))
} })

const DIALOG_TITLE = "Générer les équipes"
const DIALOG_DESCRIPTION = "Modifiez les paramètres de génération, puis cliquez sur le bouton pour générer automatiquement les équipes."

</script>

<template>
	<CustomDialog :title="DIALOG_TITLE" :description="DIALOG_DESCRIPTION" v-model:open="open">
		<template #trigger>
			<slot />
		</template>

		<Column class="items-stretch gap-2">
			<Row class="items-center">
				<Label for="nbTeams" class="w-3/5 text-left">Nombre d'équipes</Label>
				<Input id="nbTeams" type="number" v-model="nbTeams" class="w-2/5" :min="0" :max="nbStudents" />
			</Row>
			<Row class="items-center">
				<Label for="womenPerTeam" class="w-3/5 text-left">Nombre de femmes par équipe</Label>
				<Input id="womenPerTeam" type="number" v-model="womenPerTeam" class="w-2/5" :min="0" :max="nbStudents" />
			</Row>
		</Column>
		<ErrorText v-if="error" class="mb-2">Une erreur est survenue.</ErrorText>

		<template #footer>
			<DialogClose>
				<Button variant="outline">Annuler</Button>
			</DialogClose>
			<LoadingButton type="submit" class="flex items-center" :loading="isPending" @click="mutate">
				Générer les équipes
			</LoadingButton>
		</template>
	</CustomDialog>
</template>