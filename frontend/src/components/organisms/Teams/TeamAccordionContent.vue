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

	//TO-DO: Delete default when the BDD is ready to handle null values
	leader: {
		type: String,
		required: false,
		default: "Leader"
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
  <div>
  <AccordionContent id="accordionStudents">
    <AccordionContent >
      <div class="flex font-thin">
        <div class="w-[25%]">Nom</div>
        <div class="w-[25%]">Prénom</div>
        <div v-if="phase!='PREPUBLISHED'" class="w-[25%]">Rôle</div>
        <div class="w-[25%]">Sexe</div>
        <div class="w-[25%]">Bachelore</div>
      </div>
      <Separator/>
    </AccordionContent>
    <div v-for="(student, i) in students" :key="i" :value="student">
      <AccordionContent>
        <div class="flex">
          <div class="w-[25%]">{{getNom( student.name )}}</div>
          <div class="w-[25%]">{{ getPrenom( student.name ) }}</div>
          <div v-if="phase!='PREPUBLISHED'" class="w-[25%]">{{ student.teamRole }}</div>
          <div class="w-[25%]">{{ student.gender }}</div>
          <div class="w-[25%]">
            <p v-if="student.bachelor">Oui</p>
            <p v-else>Non</p>
          </div>
        </div>
        <Separator/>
      </AccordionContent>
    </div>
    <AccordionContent>
      <div class="flex">
        <div class="font-bold">{{ leader }}</div>
      </div>
    </AccordionContent>
  </AccordionContent>
  <AccordionContent v-if="criteria" id="accordionCriteria">
    <div class="flex flex-col">
      <div class="flex flex-row">
        <isCheck :isCheck="criteria.validCriteriaWoman"/>
        <div>Nombre de femme : {{criteria.nbWomans}}</div>
      </div>
      <div class="flex flex-row">
        <isCheck :isCheck="criteria.validCriteriaBachelor"/>
        <div>Nombre de Bachelor : {{ criteria.nbBachelors }}</div>
      </div>
    </div>
  </AccordionContent>
  </div>
</template>