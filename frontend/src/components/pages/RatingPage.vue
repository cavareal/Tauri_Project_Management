<script setup lang="ts">

import { ref } from "vue"
import { SidebarTemplate } from "@/components/templates"
import { Header } from "@/components/molecules/header"
import { Column } from "@/components/atoms/containers"
import { hasPermission } from "@/services/user/user.service"
import { NotAuthorized } from "@/components/organisms/errors"
import Rating from "@/components/organisms/Rate/Rating.vue"
import { SprintSelect, TeamSelect } from "../molecules/select"
import GradeNotSelected from "../organisms/Grade/GradeNotSelected.vue"

const teamId = ref<string | null>(null)
const sprintId = ref<string | null>(null)

</script>

<template>
	<SidebarTemplate>
		<NotAuthorized v-if="!hasPermission('RATING_PAGE')" />
		<Column v-else class="gap-4 h-full">
			<Header title="Évaluations">
				<SprintSelect v-model="sprintId" />
				<TeamSelect v-model="teamId" />
			</Header>

			<Column v-if="teamId !== null && sprintId !== null" class="gap-4">
				<Rating :teamId="teamId" :sprintId="sprintId" />
			</Column>

            <GradeNotSelected v-else />
		</Column>
	</SidebarTemplate>
</template>