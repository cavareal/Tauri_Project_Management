<script setup lang="ts">
import { computed } from "vue"
import { ResizableHandle, ResizablePanel, ResizablePanelGroup } from "@/components/ui/resizable/index"
import Sidebar from "@/components/organisms/Sidebar.vue"
import { ScrollArea, ScrollBar } from "@/components/ui/scroll-area/index"
import { cn } from "@/utils/utils"

const props = defineProps<{
	className?: string
}>()

const style = computed(() => {
	return cn(
		"h-screen w-screen",
		"flex flex-row items-stretch justify-start",
		props.className
	)
})
</script>

<template>
	<section :class="style">
		<Sidebar />
		<ScrollArea class="flex-1">
			<slot />
			<ScrollBar />
		</ScrollArea>
	</section>
</template>