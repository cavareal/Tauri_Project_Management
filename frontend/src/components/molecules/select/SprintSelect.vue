<script setup lang="ts">

import { useQuery } from "@tanstack/vue-query"
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from "@/components/ui/select"
import Skeleton from "@/components/ui/skeleton/Skeleton.vue"
import { cn } from "@/utils/style"
import { getCurrentSprint, getSprints } from "@/services/sprint"
import { formatSprintEndType } from "@/types/sprint"

defineProps<{
	modelValue: string | null
}>()

// eslint-disable-next-line func-call-spacing
const emit = defineEmits<{
	(e: "update:modelValue", value: string): void
}>()

const onValueChange = (newValue: string) => {
	emit("update:modelValue", newValue)
}

const { data: sprints } = useQuery({
	queryKey: ["sprints"],
	queryFn: async() => {
		let sprints = await getSprints()
		sprints = sprints.filter(sprint => sprint.endType === "NORMAL_SPRINT" || sprint.endType === "FINAL_SPRINT")
		if (sprints && sprints.length > 0) {
			const currentSprint = getCurrentSprint(sprints)
			onValueChange(currentSprint?.id.toString() ?? sprints[0].id.toString())
		}
		return sprints
	}
})

const style = cn("w-56")

</script>

<template>
	<Skeleton v-if="!sprints || sprints.length === 0" :class="style" />

	<Select v-else :model-value="modelValue ?? undefined" @update:model-value="onValueChange">
		<SelectTrigger :class="style">
			<SelectValue />
		</SelectTrigger>
		<SelectContent>
			<SelectItem v-for="sprint in sprints" :key="sprint.id" :value="sprint.id.toString()">
				Sprint {{ sprint.sprintOrder }} ({{ formatSprintEndType(sprint.endType) }})
			</SelectItem>
		</SelectContent>
	</Select>
</template>