<script setup lang="ts">
import { computed } from "vue"
import { Button } from "@/components/ui/button"
import { cn } from "@/utils/utils"
import { useRoute } from "vue-router"

const route = useRoute()

const props = defineProps<{
	link?: string | null
	class?: string
}>()

const selected = computed(() => props.link && route.path === props.link)

const style = computed(() => {
	return cn(
		"w-full my-1",
		"flex flex-row items-center justify-start gap-2",
		"text-white hover:bg-white/10 transition-colors",
		{ "bg-white/20": selected.value },
		props.class
	)
})
</script>

<template>
	<RouterLink :to="link ?? ''">
		<Button variant="ghost" :class="style">
			<slot />
		</Button>
	</RouterLink>
</template>