<script setup lang="ts">
import { Accordion, AccordionItem, AccordionTrigger } from "@/components/ui/accordion"
import type { Team } from "@/type/Team"
import { getTeams } from "@/service/TeamService"
import { ref, onMounted } from "vue"
import { getStudentsByTeamId } from "@/service/StudentService"
import TeamAccordionContent from "@/components/organisms/Teams/TeamAccordionContent.vue"

const teams = ref<Team[]>([])

onMounted(async() => {
	const data = await getTeams()
	teams.value = data
	console.log(data)
})

console.log(getStudentsByTeamId(1))

</script>

<template>
	<Accordion type="multiple" class="w-full">
    <AccordionItem v-for="team in teams" :key="team.id" :value="team.name">
    <AccordionTrigger>{{ team.name }}</AccordionTrigger>
      <TeamAccordionContent :teamId="team.id" :leader="team.leaderId.name"/>
    </AccordionItem>
  </Accordion>
</template>

<style scoped>

</style>