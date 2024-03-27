<script setup lang="ts">
import { computed } from "vue"
import { Button } from "@/components/ui/button"
import { cn } from "@/utils/utils"

const props = defineProps<{
	link: string
	selected?: boolean
	class?: string
}>()

const style = computed(() => {
	return cn(
		"w-full my-1",
		"flex flex-row items-center justify-start gap-2",
		props.class
	)
})
</script>

<template>
	<RouterLink :to="link">
		<Button :variant="selected ? 'secondary' : 'ghost'" :class="style">
			<slot />
		</Button>
	</RouterLink>
</template>