<script setup lang="ts">

import { ref } from "vue"
import { SidebarTemplate } from "@/components/templates"
import { Header } from "@/components/molecules/header"
import { Column } from "@/components/atoms/containers"
import { ListChecks } from "lucide-vue-next"
import { hasPermission } from "@/services/user/user.service"
import { NotAuthorized } from "@/components/organisms/errors"
import Rating from "@/components/organisms/Rate/Rating.vue"
import { SprintSelect, TeamSelect } from "../molecules/select"

const teamId = ref<string | null>(null)
const sprintId = ref<string | null>(null)

</script>

<template>
	<SidebarTemplate>
		<NotAuthorized v-if="!hasPermission('RATING_PAGE')" />
		<Column v-else class="gap-4">
			<Header title="Évaluations">
				<SprintSelect v-model="sprintId" />
				<TeamSelect v-model="teamId" />
			</Header>

			<Column v-if="teamId !== null && sprintId !== null" class="gap-4">
				<Rating :teamId="teamId" :sprintId="sprintId" />
			</Column>

            <Column v-else class="items-center py-4 gap-2 border border-gray-300 border-dashed rounded-lg">
				<ListChecks class="size-12 stroke-1 text-dark-blue" />
                <p class="text-dark-blue text-sm">Vous n'avez pas sélectionné de sprint et/ou une équipe à évaluer.</p>
            </Column>
		</Column>
	</SidebarTemplate>
</template>