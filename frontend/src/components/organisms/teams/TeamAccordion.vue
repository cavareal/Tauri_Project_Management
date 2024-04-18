<script setup lang="ts">
import { Accordion, AccordionItem, AccordionTrigger } from "@/components/ui/accordion"
import type { Team } from "@/types/team"
import { getTeams } from "@/services/team-service"
import { ref, onMounted } from "vue"
import TeamAccordionContent from "@/components/organisms/teams/TeamAccordionContent.vue"
import { getCookie } from "@/utils/cookie"
import { Button } from "@/components/ui/button"
import { Pencil } from "lucide-vue-next"
import Row from "@/components/atoms/containers/Row.vue"
import { EditTeamDialog } from "."

const role = getCookie("role")

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
})

</script>

<template>
	<Accordion type="multiple">
		<Row v-for="team in teams" :key="team.id" class="w-full items-start gap-8">

			<AccordionItem :value="team.name" class="flex-1">
				<AccordionTrigger>
					{{ team.name }}
					{{ team.leader?.name ? `(${team.leader.name})` : "" }}
				</AccordionTrigger>
				<TeamAccordionContent :teamId="team.id" :leader="team.leader?.name" :phase="props.phase" />
			</AccordionItem>

			<EditTeamDialog v-if="role === 'PROJECT_LEADER'" :teamId="team.id">
				<Button variant="outline" size="icon" class="mt-2">
					<Pencil class="w-4" />
				</Button>
			</EditTeamDialog>
		</Row>
	</Accordion>
</template>

<style scoped></style>