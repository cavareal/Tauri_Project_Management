<script setup lang="ts">
import { ref, onMounted, defineProps } from "vue"
import { getStudentsByTeamId } from "@/services/student-service"
import type { Student } from "@/types/student"
import { AccordionContent } from "@/components/ui/accordion"

const students = ref<Student[]>([])

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
	}
})

onMounted(async() => {
	const data = await getStudentsByTeamId(props.teamId)
	students.value = data
})
</script>

<template>
  <div v-for="(student, i) in students" :key="i" :value="student">
    <AccordionContent>
      <div class="flex">
        <div class="flex-grow">{{ student.name }}</div>
        <div class="flex-grow">{{ student.teamRole }}</div>
        <div class="flex-grow">{{ student.gender }}</div>
      </div>
    </AccordionContent>
  </div>
  <AccordionContent>
    <div class="flex">
      <div class="font-bold">{{ leader }}</div>
    </div>
  </AccordionContent>
</template>