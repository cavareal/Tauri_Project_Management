<script setup lang="ts">

import type { Grade } from "@/types/grade"
import { getGradeTypeDescription, type GradeTypeName } from "@/types/grade-type"
import { ErrorText, InfoText, Subtitle } from "@/components/atoms/texts"
import { CheckIcon, Loader } from "@/components/atoms/icons"
import { Input } from "@/components/ui/input"
import { Textarea } from "@/components/ui/textarea"
import { Column, Row } from "@/components/atoms/containers"
import type { Student } from "@/types/student"
import { useMutation, useQuery, useQueryClient } from "@tanstack/vue-query"
import { onMounted, ref, watch } from "vue"
import { createOrUpdateGrade, getRatedGrade } from "@/services/grade"
import { getStudentsByTeamId } from "@/services/student"
import { createToast } from "@/utils/toast"
import { downloadGradeScaleTXT, getGradeTypeByName } from "@/services/grade-type"
import { Button } from "@/components/ui/button"
import {  getTeamStudentsCommentsBySprintAndAuthor } from "@/services/feedback"
import type { Feedback } from "@/types/feedback"

const props = defineProps<{
	gradeTypeName: GradeTypeName,
	teamId: string,
	sprintId: string,
	allGrades: Grade[],
	/*canGradeIndividualPerformance : boolean,
	canCommentIndividualPerformance : boolean,
	canGradeLimitedBonus : boolean,
	canValidateLimitedBonus : boolean
	canGradeUnlimitedBonus : boolean,
	canCommentUnlimitedBonus : boolean*/
}>()

const queryClient = useQueryClient()
const status = ref<"IDLE" | "LOADING" | "DONE">("IDLE")
const teamStudents = ref<Student[] | null>(null)
const studentComments = ref<Feedback[] | null>(null)
const grades = ref(teamStudents.value ? Array(teamStudents.value.length).fill("") : [])
const comments = ref(teamStudents.value ? Array(teamStudents.value.length).fill("") : [])
const feedbacks = ref(teamStudents.value ? Array(teamStudents.value.length).fill("") : [])
const oldValues = ref({ grades: JSON.parse(JSON.stringify(grades.value)), comments: JSON.parse(JSON.stringify(comments.value)), feedbacks: JSON.parse(JSON.stringify(feedbacks.value)) })
const isGradeScaleUploaded = ref(false)

const updateGrades = async() => {
	for (let i = 0; i < teamStudents.value.length; i++) {
		const grade = getRatedGrade(props.allGrades, {
			sprintId: Number(props.sprintId),
			teamId: null,
			studentId: teamStudents.value[i] ? Number(teamStudents.value[i].id) : null,
			gradeTypeName: props.gradeTypeName
		})

		grades.value[i] = grade?.value?.toString() ?? ""
		let filteredComment = studentComments.value?.filter(comment => !comment.feedback && comment.student.id === teamStudents.value[i].id)
		let filteredFeedback = studentComments.value?.filter(comment => comment.feedback && comment.student.id === teamStudents.value[i].id)
		comments.value[i] = (filteredComment && filteredComment.length > 0) ? filteredComment[0].content : ""
		feedbacks.value[i] = (filteredFeedback && filteredFeedback.length > 0) ? filteredFeedback[0].content : ""
		oldValues.value = {	grades: JSON.parse(JSON.stringify(grades.value)), comments: JSON.parse(JSON.stringify(comments.value)), feedbacks: JSON.parse(JSON.stringify(feedbacks.value)) }
	}
	console.log(grades.value)
	console.log(comments.value)
	console.log(feedbacks.value)
	status.value = "IDLE"
}

const onGradeChange = (value: string | number, index: number) => {
	const parsedValue = Number(value)
	if (value === "" || isNaN(parsedValue)) {
		grades.value[index] = ""
		return
	}
	if (parsedValue > 20) {
		grades.value[index] = "20"
		return
	}
	if (parsedValue < 0) {
		grades.value[index] = "0"
		return
	}
	grades.value[index] = parsedValue.toString()
}

const checkGradeScaleUploaded = async() => {
	try {
		const gradeType = await getGradeTypeByName(props.gradeTypeName)
		isGradeScaleUploaded.value = !!gradeType.scaleTXTBlob // Check if the grade scale is present
	} catch (error) {
		console.error("Error checking grade scale:", error)
		isGradeScaleUploaded.value = false
	}
}

const { mutate, isPending, isError } = useMutation({
	mutationFn: async(index: number) => {
		status.value = "LOADING"

		if (grades.value[index] !== oldValues.value.grades[index]) {
			await createOrUpdateGrade({
				value: Number(grades.value[index]),
				comment: null,
				sprintId: Number(props.sprintId),
				teamId: null,
				studentId: teamStudents.value[index].id,
				gradeTypeName: props.gradeTypeName
			})
		} else if (comments.value[index] !== oldValues.value.comments[index]) {
			return
		} else if (feedbacks.value[index] !== oldValues.value.feedbacks[index]) {
			return
		}


		createToast("La note a bien été enregistrée.")
		oldValues.value = { grades: JSON.parse(JSON.stringify(grades.value)), comments: JSON.parse(JSON.stringify(comments.value)), feedbacks: JSON.parse(JSON.stringify(feedbacks.value)) }
		queryClient.invalidateQueries({ queryKey: ["all-rated-grades"] })
	}
})

watch(isPending, (newValue) => {
	if (!newValue && status.value === "LOADING") {
		status.value = "DONE"
	}
})

watch(status, (newValue) => {
	if (newValue === "DONE") {
		setTimeout(() => {
			status.value = "IDLE"
		}, 5000)
	}
})

watch(() => [props.teamId, props.sprintId], () => {
	updateGrades()
})

onMounted(async() => {
	teamStudents.value = await getStudentsByTeamId(Number(props.teamId), true)
	studentComments.value = await getTeamStudentsCommentsBySprintAndAuthor(props.sprintId, props.teamId)
	updateGrades()
	checkGradeScaleUploaded()
})

const download = useMutation({
	mutationFn: async() => {
		await downloadGradeScaleTXT(props.gradeTypeName)
			.then(() => createToast("Le fichier a été téléchargé."))
	},
	onError: () => createToast("Erreur lors du téléchargement du fichier.")
})

</script>

<template>
	<!--Trucs à mettre : Bonus / Malus Limités et Illimités (Grade + justificatif pr illimité) + Performance Individuelle (Grade + commentaire)-->

	<Column class="items-stretch justify-start gap-3 border rounded-md p-6 bg-white">
		<Row class="items-center justify-between gap-2">
			<Row class="items-center justify-start gap-2">
				<slot/>
				<Subtitle>{{ gradeTypeName }}</Subtitle>
			</Row>
			<Loader v-if="status === 'LOADING'" class="size-4 stroke-slate-500"/>
			<CheckIcon v-if="status === 'DONE'" :checked="!isError"/>
		</Row>

		<Row class="items-center justify-between gap-6">
			<InfoText class="flex-1">{{ getGradeTypeDescription(gradeTypeName) }}</InfoText>
		</Row>

		<Row class="flex-wrap">
			<Row class="w-2/3 mb-4 pr-4" v-for="(student, index) in teamStudents" :key="student.id">
				<Row class="items-center justify-between gap-2">
					<Subtitle>{{ student.name }}</Subtitle>
					<Input class="w-16" type="number" min="0" max="20" v-model="grades[index]" @update:model-value="value => onGradeChange(value, index)" :disabled="isPending" v-on:blur="mutate(index)" />
					<Textarea class="resize-none" v-model="comments[index]"  placeholder="Ajouter un commentaire" :disabled="isPending" v-on:blur="mutate(index)" />
					<Textarea class="resize-none" v-model="feedbacks[index]"  placeholder="Ajouter un feedback" :disabled="isPending" v-on:blur="mutate(index)" />
				</Row>
			</Row>
		</Row>
		<ErrorText v-if="status === 'DONE' && isError">Une erreur est survenue.</ErrorText>
		<Row class="items-center justify-end mt-4" v-if="isGradeScaleUploaded">
			<Button variant="outline" @click="download.mutate">
				Télécharger le barème
			</Button>
		</Row>
	</Column>
</template>

<style scoped>

</style>