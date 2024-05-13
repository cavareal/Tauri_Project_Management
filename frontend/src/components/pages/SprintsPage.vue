<script setup lang="ts">

import { SidebarTemplate } from "@/components/templates"
import { Cookies } from "@/utils/cookie"
import AddSprint from "@/components/organisms/sprints/AddSprint.vue"
import EditSprint from "@/components/organisms/sprints/EditSprint.vue"
import { Error, NotAuthorized } from "@/components/organisms/errors"
import { Header } from "@/components/molecules/header"
import { useQuery } from "@tanstack/vue-query"
import { getAllSprints } from "@/services/sprint-service"
import { onMounted, ref } from "vue"
import { CalendarDate, parseDate } from '@internationalized/date'
import { PageSkeleton } from "@/components/atoms/skeletons"
import { ActionSection } from "@/components/molecules/action-section"

const token = Cookies.getToken()
const role = Cookies.getRole()

const IS_SPRINT = "Cliquez-ici pour ajouter un sprint"
const ISNT_SPRINT = "Vous n'avez pas encore crée de sprint, cliquez-ici pour ajouter le premier"

const lastSprintEndDate = ref<CalendarDate>();
const lastSprintOrder = ref<number>(0);

const { data: sprints, error: error, refetch: getSprints, isLoading, isFetching } = useQuery({ queryKey: ["sprints"], queryFn: async () => {
		const newSprints = await getAllSprints()

		if(newSprints.length != 0){
			lastSprintEndDate.value = formatDate(newSprints[newSprints.length - 1].endDate)
			lastSprintOrder.value = newSprints[newSprints.length - 1].sprintOrder
		}
		return newSprints
	}
})




onMounted(async () => {
	await getSprints()
})


function formatDate(date: Date) {
	const day = String(date.getDate()).padStart(2, '0');
	const month = String(date.getMonth() + 1).padStart(2, '0');
	const year = date.getFullYear();
	return new CalendarDate(Number(year), Number(month), Number(day));
}

</script>

<template>
	<SidebarTemplate>
		<Header title="Sprints" />
		<PageSkeleton v-if="isLoading || isFetching" />
		<div v-else-if="token && (role === 'PROJECT_LEADER' || role === 'OPTION_LEADER')">
			<EditSprint v-if="sprints && sprints.length > 0" v-for="sprint in sprints" :sprint="sprint"
				@edit:sprint="getSprints" @delete:sprint="getSprints" />
			<AddSprint v-if="sprints && sprints.length > 0" :title="IS_SPRINT" :lastSprintEndDate="lastSprintEndDate"
				:lastSprintOrder="lastSprintOrder" @add:sprint="getSprints" />
			<AddSprint v-else :title="ISNT_SPRINT" :lastSprintEndDate="undefined" :lastSprintOrder="lastSprintOrder"
				@add:sprint="getSprints" />
		</div>
		<div v-else-if="token && (role === 'OPTION_STUDENT')">
			<ActionSection class="mb-5" v-for="sprint in sprints" :title="'Sprint ' + sprint.sprintOrder + ' : du ' + formatDate(sprint.startDate) + ' au ' + formatDate(sprint.endDate)" :description="sprint.endType">
			</ActionSection>
		</div>
		<NotAuthorized v-else />
		<Error v-if="error" />
	</SidebarTemplate>
</template>