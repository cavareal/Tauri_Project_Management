<script setup lang="ts">

import { CustomDialog, DialogClose } from "@/components/molecules/dialog"
import { useQuery } from "@tanstack/vue-query"
import { getTeams } from "@/services/team-service"
import { ErrorText } from "@/components/atoms/texts"
import { Input } from "@/components/ui/input"
import { Label } from "@/components/ui/label"
import { Button } from "@/components/ui/button"
import LoadingButton from "../../molecules/buttons/LoadingButton.vue"
import { getCookie } from "@/utils/cookie"
import { type Ref, ref } from "vue"
import { addGradeToTeam } from "@/services/grade-service"

const userId = getCookie("user")
const selectedTeam = ref("")
let note = ref("")
const token = getCookie("token")

const props = defineProps<{
	title: string,
	description : string,
	gradeType : string
}>()

const evaluations: Ref<Record<string, { team: string, gradeType : string,  grade: number }[]>> = ref({})

const { data: teams, isLoading, error } = useQuery({ queryKey: ["teams"], queryFn: getTeams })

const addEvaluation = () => {
	if (!evaluations.value[selectedTeam.value]) {
		evaluations.value[selectedTeam.value] = []
	}
	const teamIndex = evaluations.value[selectedTeam.value].findIndex(e => e.team === selectedTeam.value)
	if (teamIndex !== -1) {
		evaluations.value[selectedTeam.value][teamIndex].grade = Number(note.value)
	} else {
		evaluations.value[selectedTeam.value].push({
			team: selectedTeam.value,
			gradeType: props.gradeType,
			grade: Number(note.value)
		})
	}
}
const handleNoteInput = (event: InputEvent) => {
	const inputNote = parseInt((event.target as HTMLInputElement).value)
	if (inputNote > 20) {
		note.value = String(20)
	} else {
		note.value = String(inputNote)
	}
}
const sendGrades = () => {
	void addEvaluation(),
	console.log(evaluations.value),
	void addGradeToTeam(userId, evaluations, token)
}

</script>

<template>
	<CustomDialog :title=title :description=description>
		<template #trigger>
			<Button variant="default">Noter une équipe</Button>
		</template>

		<ErrorText v-if="error" class="mb-2">Une erreur est survenue.</ErrorText>

		<div class="grid gap-4 py-4">
			<div class="grid grid-cols-3 items-center gap-4">
				<Label for="equipe">Equipe :</Label>
				<select v-model="selectedTeam">
					<option value="" disabled selected hidden>Choisir une équipe</option>
					<option v-for="team in teams" :key="team.id">
						{{ team.name }}
					</option>
				</select>
			</div>
			<div class="grid grid-cols-3 items-center gap-4">
				<Label for="note">Note :</Label>
				<Input id="note" type="number" min="0" max="20" v-model="note" @input="handleNoteInput"/>
			</div>
		</div>
		<template #footer>
			<DialogClose v-if="!isLoading">
				<Button variant="outline">Annuler</Button>
			</DialogClose>
			<DialogClose>
				<LoadingButton type="submit" @click="sendGrades" :loading="isLoading">
					Confirmer
				</LoadingButton>
			</DialogClose>
		</template>
	</CustomDialog>
</template>

<style scoped>

</style>