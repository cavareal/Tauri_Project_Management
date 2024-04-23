<script setup lang="ts">

import { Accordion, AccordionItem, AccordionTrigger } from "@/components/ui/accordion"
import { getTeams } from "@/services/team-service"
import TeamAccordionContent from "@/components/organisms/teams/TeamAccordionContent.vue"
import { getCookie } from "@/utils/cookie"
import { Button } from "@/components/ui/button"
import { Pencil } from "lucide-vue-next"
import EditTeamDialog from "./EditTeamDialog.vue"
import { Row } from "@/components/atoms/containers"
import { useQuery } from "@tanstack/vue-query"
import { PageSkeleton } from "@/components/atoms/skeletons"
import type { ProjectPhase } from "@/types/project"

const role = getCookie("role")

const props = defineProps<{
	phase: ProjectPhase
}>()

const { data: teams, refetch: refetchTeams, isLoading, isFetching } = useQuery({ queryKey: ["teams"], queryFn: getTeams })

</script>

<template>
	<PageSkeleton v-if="isLoading || isFetching" />
	<Accordion v-else type="multiple">
		<Row v-for="team in teams" :key="team.id" class="w-full items-start gap-8">
			<AccordionItem :value="team.id.toString()" class="flex-1">
				<AccordionTrigger>
					{{ team.name }}
					{{ team.leader?.name ? `(${team.leader.name})` : "" }}
				</AccordionTrigger>
				<TeamAccordionContent :team-id="team.id" :phase="props.phase" />
			</AccordionItem>

			<EditTeamDialog v-if="role === 'PROJECT_LEADER'" :team="team" @edit:team="refetchTeams">
				<Button variant="outline" size="icon" class="mt-2">
					<Pencil class="w-4" />
				</Button>
			</EditTeamDialog>
		</Row>
	</Accordion>
</template>