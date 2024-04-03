<script setup lang="ts">
import { computed } from "vue"
import { cn } from "@/utils/utils"
import { useRoute } from "vue-router"
import { ButtonLink } from "@/components/molecules/button-link"

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
		"text-white hover:bg-white/10 hover:text-white transition-colors",
		{ "bg-white/20": selected.value },
		props.class
	)
})
</script>

<template>
	<ButtonLink :link="link" :class="style" variant="ghost">
		<slot />
	</ButtonLink>
</template>