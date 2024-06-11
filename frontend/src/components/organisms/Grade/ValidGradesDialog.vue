<script setup lang="ts">

import { Button } from "@/components/ui/button"
import { CustomDialog, DialogClose } from "@/components/molecules/dialog"
import { ref, watch } from "vue"
import { useMutation, useQuery } from "@tanstack/vue-query"
import { ErrorText } from "@/components/atoms/texts"
import { LoadingButton } from "@/components/molecules/buttons"
import { createToast } from "@/utils/toast"
import { setGradesConfirmation, getIndividualGradesByTeam } from "@/services/grade/grade.service"
import { setBonusesTeamValidation, getValidationBonusesByTeam } from "@/services/bonus/bonus.service"
import { Cookies } from "@/utils/cookie"

const emits = defineEmits(["valid:individual-grades"])
const open = ref(false)

const props = defineProps<{
	selectedTeam?: string
	selectedSprint?: string
}>()


const selectedTeam = ref(props.selectedTeam)
const selectedSprint = ref(props.selectedSprint)


const fetchIndividualGradesByTeam = async() => {
	if (selectedTeam.value && selectedSprint.value) {
		return getIndividualGradesByTeam(Number(selectedSprint.value), Number(selectedTeam.value))
	}
}


const fetchValidationBonusesByTeam = async() => {
	if (selectedTeam.value) {
		return getValidationBonusesByTeam(Number(selectedTeam.value), Number(selectedSprint.value))
	}
}


const { data: individualsGradeByTeam, refetch: refetchIndividualGradesByTeam } = useQuery({
	queryKey: ["individual-grade-team", selectedTeam, selectedSprint],
	queryFn: fetchIndividualGradesByTeam,
	enabled: !!selectedTeam.value && !!selectedSprint.value
})


const { data: validationBonusesByTeam, refetch: refetchValidationBonusesByTeam } = useQuery({
	queryKey: ["validation-bonuses-team", selectedTeam],
	queryFn: fetchValidationBonusesByTeam,
	enabled: !!selectedTeam.value
})


watch(
	() => [props.selectedTeam, props.selectedSprint],
	([newTeam, newSprint]) => {
		selectedTeam.value = newTeam
		selectedSprint.value = newSprint
		refetchIndividualGradesByTeam()
		refetchValidationBonusesByTeam()
	}
)


const { mutate: mutateIndividual, isPending: isPendingIndividual, error: errorIndividual } = useMutation({
	mutationKey: ["individual-grades"], mutationFn: async() => {
		console.log(props)
		await setGradesConfirmation(Number(props.selectedTeam), Number(props.selectedSprint))
			.then(() => open.value = false)
			.then(() => emits("valid:individual-grades"))
			.then(() => createToast("Les notes individuelles ont été validées."))
	}
})


const { mutate: mutateBonuses, isPending: isPendingBonuses, error: errorBonuses } = useMutation({
	mutationKey: ["individual-grades"], mutationFn: async() => {
		console.log(props)
		await setBonusesTeamValidation(Number(props.selectedTeam), Number(props.selectedSprint), Cookies.getUserId())
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

		<ErrorText v-if="errorBonuses || errorIndividual" class="mb-2">Une erreur est survenue.</ErrorText>

		individualsGradeByTeam : {{ individualsGradeByTeam }}

		Mmebres ayant validé le bonus (students + ss) : {{ validationBonusesByTeam }}
		<template #footer>
			<DialogClose v-if="!isPendingBonuses || !isPendingIndividual">
				<Button variant="outline">Annuler</Button>
			</DialogClose>
			<LoadingButton type="submit" @click="mutateBonuses" :loading="isPendingBonuses">
				Valider les bonus limités
			</LoadingButton>
			<LoadingButton v-if="Cookies.getRole() == 'SUPERVISING_STAFF'" type="submit" @click="mutateIndividual" :loading="isPendingIndividual">
				Valider les notes individuelles
			</LoadingButton>
		</template>
	</CustomDialog>
</template>