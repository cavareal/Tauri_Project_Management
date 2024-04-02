<script setup lang="ts">
import { ref, onMounted, defineProps } from "vue"
import { getStudentsByTeamId } from "@/service/StudentService"
import type { Student } from "@/type/Student"
import { AccordionContent } from "@/components/ui/accordion"
import { TableCell } from "@/components/ui/table"

const students = ref<Student[]>([])

const props = defineProps({
	teamId: {
		type: Number,
		required: true
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
      <TableCell>{{ student.name }}</TableCell>
      <TableCell>{{ student.teamRole }}</TableCell>
      <TableCell>{{ student.gender }}</TableCell>
    </AccordionContent>
  </div>
</template>