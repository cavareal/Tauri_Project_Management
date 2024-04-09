<script setup lang="ts">
import { ref, onMounted, defineProps } from "vue"
import { getStudentsByTeamId } from "@/services/student-service"
import type { Student } from "@/types/student"
import { AccordionContent } from "@/components/ui/accordion"
import type { Criteria } from "@/types/criteria"
import { Separator } from "@/components/ui/separator"
import { separateStringOnFirstSpace } from "@/utils/utils"
import { getCriteria } from "@/services/team-service"
import IsCheck from "@/components/atoms/isCheck.vue"

const students = ref<Student[]>([])

const criteria = ref<Criteria>()

const props = defineProps({
	teamId: {
		type: Number,
		required: true
	},

	leader: {
		type: String,
		required: false
	},

	phase: {
		type: String,
		required: true
	}
})

onMounted(async() => {
	const data = await getStudentsByTeamId(props.teamId)
	students.value = data
	const criteriaData = await getCriteria(props.teamId)
	criteria.value = criteriaData
})

function getNom(nomPrenom: string) {
	return separateStringOnFirstSpace(nomPrenom)[0]
}

function getPrenom(nomPrenom: string) {
	return separateStringOnFirstSpace(nomPrenom)[1]
}


</script>

<template>
	<AccordionContent class="w-full flex mb-4 items-start">
		<div id="accordionStudents" class="pr-10 flex-grow flex-1 w-full">
			<div>
				<div class="flex font-thin">
					<div class="w-[25%]">Nom</div>
					<div class="w-[25%]">Prénom</div>
					<div v-if="phase != 'PREPUBLISHED'" class="w-[25%]">Rôle</div>
					<div class="w-[25%]">Sexe</div>
					<div class="w-[25%]">Bachelor</div>
				</div>
				<Separator />
			</div>
			<div v-for="(student, i) in students" :key="i" :value="student">
				<div>
					<div class="flex">
						<div class="w-[25%]">{{ getNom(student.name) }}</div>
						<div class="w-[25%]">{{ getPrenom(student.name) }}</div>
						<div v-if="phase != 'PREPUBLISHED'" class="w-[25%]">{{ student.teamRole }}</div>
						<div class="w-[25%]">{{ student.gender === "MAN" ? "Homme" : "Femme" }}</div>
						<div class="w-[25%]">
							<p v-if="student.bachelor">Oui</p>
							<p v-else>Non</p>
						</div>
					</div>
					<Separator />
				</div>
			</div>
		</div>
		<div v-if="criteria" id="accordionCriteria" class="w-auto border rounded pb-2">
			<div class="flex flex-col p-3 pb-0">
				<div>
					Critères de génération
				</div>
				<div class="flex flex-row">
					<isCheck :isCheck="criteria.validCriteriaWoman" class="pr-1" />
					<div>Nombre de femmes : {{ criteria.nbWomans }}</div>
				</div>
				<div class="flex flex-row">
					<isCheck :isCheck="criteria.validCriteriaBachelor" class="pr-1" />
					<div>Nombre de bachelors : {{ criteria.nbBachelors }}</div>
				</div>
			</div>
		</div>
	</AccordionContent>
</template>