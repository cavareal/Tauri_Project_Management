<script setup lang="ts">
import { Accordion, AccordionItem, AccordionTrigger } from "@/components/ui/accordion"
import type { Team } from "@/type/Team"
import { getTeams } from "@/service/TeamService"
import { ref, onMounted, defineProps } from "vue"
import TeamAccordionContent from "@/components/organisms/Teams/TeamAccordionContent.vue"

const teams = ref<Team[]>([])

const props = defineProps({
	phase: {
		type: String,
		required: true
	}
})

onMounted(async() => {
	const data = await getTeams()
	teams.value = data
	console.log(data)
})

</script>

<template>
	<Accordion type="multiple" class="w-full">
    <AccordionItem v-for="team in teams" :key="team.id" :value="team.name">
    <AccordionTrigger>{{ team.name }}</AccordionTrigger>
        <TeamAccordionContent :teamId="team.id" :leader="team.leader?.name" :phase="phase"/>
    </AccordionItem>
  </Accordion>
</template>

<style scoped>

</style>