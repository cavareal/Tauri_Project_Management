<script setup lang="ts">
import { Accordion, AccordionItem, AccordionTrigger } from "@/components/ui/accordion"
import type { Team } from "@/types/team"
import { getTeams } from "@/services/team-service"
import { ref, onMounted } from "vue"
import TeamAccordionContent from "@/components/organisms/teams/TeamAccordionContent.vue"

const teams = ref<Team[]>([])

onMounted(async() => {
	const data = await getTeams()
	teams.value = data
})

</script>

<template>
	<Accordion type="multiple" class="w-full">
		<AccordionItem v-for="team in teams" :key="team.id" :value="team.name">
			<AccordionTrigger>{{ team.name }}</AccordionTrigger>
			<TeamAccordionContent :teamId="team.id" :leader="team.leader?.name" />
		</AccordionItem>
	</Accordion>
</template>

<style scoped></style>