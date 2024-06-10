<script setup lang="ts">

import { Table, TableBody, TableCell, TableHead, TableHeader, TableRow } from "@/components/ui/table"
import { Column } from "@/components/atoms/containers"
import { cn } from "@/utils/style"
import { Subtitle } from "@/components/atoms/texts"
import type { Sprint } from "@/types/sprint"
import { extractNames } from "@/utils/string"
import { useQuery } from "@tanstack/vue-query"
import { getPresentationOrder, getTeamByUserId } from "@/services/team"
import { Cookies } from "@/utils/cookie"
import { GripVertical } from "lucide-vue-next"

const props = defineProps<{
	sprint: Sprint
}>()

const userId = Cookies.getUserId()

const { data: students } = useQuery({
	queryKey: ["presentation-order-current-team", props.sprint.id],
	queryFn: async() => {
		const team = await getTeamByUserId(userId)
		if (!team) return undefined
		return getPresentationOrder(team.id.toString(), props.sprint.id.toString())
	}
})

const rowClass = cn("py-2 h-auto")

</script>

<template>
	<Column class="border bg-white rounded-md px-4 pb-4 flex-1">
		<Subtitle class="py-4">
			Ordre de passage du sprint {{ sprint.sprintOrder }}
		</Subtitle>
		<Table class="flex-1">
			<TableHeader>
				<TableRow>
					<TableHead :class="rowClass" class="w-1"></TableHead>
					<TableHead :class="rowClass" class="w-20">Ordre</TableHead>
					<TableHead :class="rowClass" class="min-w-28">Nom</TableHead>
					<TableHead :class="rowClass" class="min-w-28">Prénom</TableHead>
				</TableRow>
			</TableHeader>

			<TableBody>
				<TableRow v-for="(student, i) in students" :key="student.id">
					<TableCell :class="rowClass">
						<GripVertical class="h-4 cursor-move" />
					</TableCell>
					<TableCell :class="rowClass">{{ i + 1 }}</TableCell>
					<TableCell :class="rowClass">{{ extractNames(student.name).lastName }}</TableCell>
					<TableCell :class="rowClass">{{ extractNames(student.name).firstName }}</TableCell>
				</TableRow>
			</TableBody>
		</Table>
	</Column>
</template>