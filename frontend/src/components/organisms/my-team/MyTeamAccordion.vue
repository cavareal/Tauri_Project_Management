<script setup lang="ts">
import { Accordion, AccordionItem, AccordionTrigger } from "@/components/ui/accordion"
import TeamAccordionContent from "@/components/organisms/teams/TeamAccordionContent.vue"
import type { Team } from "@/types/team"
import type { Student } from "@/types/student"
import { onMounted, ref } from "vue"
import { getStudentsByTeamId } from "@/services/student-service"

const props = defineProps({
	team: {
		type: Object as () => Team,
		required: true
	},
	phase: {
		type: String,
		required: true
	}
})

const students = ref<Student[]>([])

onMounted(async() => {
	students.value = await getStudentsByTeamId(props.team.id)
})
</script>

<template>
  <Accordion type="multiple">
    <AccordionItem :value="props.team.name" class="flex-1">
      <AccordionTrigger>
        {{ props.team.name }}
        {{ props.team.leader?.name ? `(${props.team.leader.name})` : "" }}
      </AccordionTrigger>
      <TeamAccordionContent :teamId="props.team.id" :leader="props.team.leader?.name" :phase="props.phase"  :students="students"/>
    </AccordionItem>
  </Accordion>
</template>

<style scoped>

</style>